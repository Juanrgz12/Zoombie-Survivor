/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayGamer extends JFrame {

    private GamePanel gamePanel;          // Panel donde se dibuja y corre el juego
    private RegistroPartidas registro;    // Registro de partidas para guardar puntajes
    private Menu menuReferencia;          // Referencia al menú principal para volver

    public PlayGamer(RegistroPartidas registro, Menu menuReferencia) {
        this.registro = registro;
        this.menuReferencia = menuReferencia;

        setTitle("Zombie Survivor - Dead Zone");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
        gamePanel.iniciarJuego();
    }

    // ------------------ Clase interna GamePanel ------------------
    public class GamePanel extends JPanel implements ActionListener, KeyListener {

        private Player jugador;
        private List<Zombie> zombies;
        private JefeZombie jefe;
        private List<Bala> balas;
        private javax.swing.Timer timer;
        private Random random;
        private Image fondo;
        private ImageIcon corazonIcon;
        private List<VidaExtra> vidasExtras = new ArrayList<>();

        private boolean arriba, abajo, izquierda, derecha;
        private String ultimaDireccion = "DERECHA";
        private boolean pausado = false;

        private int opcionSeleccionada = 0;

        private JButton btnMenu, btnReiniciar, btnSalir;
        private int puntaje = 0;

        public GamePanel() {
            setFocusable(true);
            setLayout(null);
            addKeyListener(this);
            setBackground(Color.BLACK);

            jugador = new Player(600, 350);
            zombies = new ArrayList<>();
            balas = new ArrayList<>();
            random = new Random();

            spawnZombie();

            ImageIcon ii = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\dia1.png");
            fondo = ii.getImage();
            corazonIcon = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\corazon.gif");

            btnMenu = new JButton("Menu");
            btnReiniciar = new JButton("Reiniciar");
            btnSalir = new JButton("Salir");

            JButton[] botones = {btnMenu, btnReiniciar, btnSalir};
            int x = 1000;
            int y = 20;
            for (JButton b : botones) {
                b.setBounds(x, y, 150, 40);
                b.setVisible(false);
                b.setFocusable(false);
                add(b);
                y += 50;
            }

            btnMenu.addActionListener(ev -> {
                PlayGamer.this.dispose();
                menuReferencia.mostrar();
            });

            btnReiniciar.addActionListener(ev -> {
                PlayGamer.this.dispose();
                new PlayGamer(registro, menuReferencia);
            });

            btnSalir.addActionListener(ev -> System.exit(0));
        }

        public void iniciarJuego() {
            timer = new javax.swing.Timer(15, this);
            timer.start();
            requestFocusInWindow();
        }

        private void spawnZombie() {
            int panelWidth = getWidth() > 0 ? getWidth() : 1200;
            int panelHeight = getHeight() > 0 ? getHeight() : 700;

            int x = random.nextInt(panelWidth - 50);
            int y = random.nextInt(panelHeight - 50);
            zombies.add(new Zombie(x, y));

            if (random.nextInt(100) < 10 && jefe == null) {
                int jefeX = random.nextInt(panelWidth - 100);
                int jefeY = random.nextInt(panelHeight - 100);
                jefe = new JefeZombie(jefeX, jefeY);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (fondo != null) g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            else {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            jugador.dibujar(g);

            for (Zombie z : zombies) z.dibujar(g);

            if (jefe != null && jefe.estaVivo()) jefe.dibujar(g);

            for (Bala b : balas) b.dibujar(g);

            // Barra de vida
            int vidaMax = 200;
            int vidaActual = jugador.getVida();
            int barraX = 20;
            int barraY = 20;
            int barraAncho = 200;
            int anchoVida = (vidaActual * barraAncho) / vidaMax;

            g.setColor(Color.RED);
            g.fillRect(barraX, barraY, anchoVida, 20);

            g.setColor(Color.WHITE);
            g.drawRect(barraX, barraY, barraAncho, 20);

            if (corazonIcon != null) {
                int xCorazon = barraX + barraAncho + 10;
                int yCorazon = barraY - 5;
                g.drawImage(corazonIcon.getImage(), xCorazon, yCorazon, 40, 40, this);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Puntaje: " + puntaje, barraX, barraY + 50);

            // Pantalla de pausa
            if (pausado) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                g.drawString("PAUSA", getWidth() / 2 - 80, getHeight() / 2 - 100);

                String[] opciones = {"Menu", "Reiniciar", "Salir"};
                g.setFont(new Font("Arial", Font.BOLD, 32));

                int baseY = getHeight() / 2;
                for (int i = 0; i < opciones.length; i++) {
                    g.setColor(i == opcionSeleccionada ? Color.ORANGE : Color.WHITE);
                    g.drawString(opciones[i], getWidth() / 2 - 80, baseY + i * 50);
                }
            }

            // Dibujar todas las vidas extras
            for (VidaExtra v : vidasExtras) {
                v.dibujar(g);  // Se dibujan en su posición fija
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!pausado) {
                // Movimiento jugador
                int dx = 0, dy = 0;
                if (arriba) dy -= 5;
                if (abajo) dy += 5;
                if (izquierda) dx -= 5;
                if (derecha) dx += 5;
                jugador.mover(dx, dy);

                // Mover balas y eliminar fuera de pantalla
                balas.removeIf(b -> !b.estaEnPantalla(getWidth(), getHeight()));
                for (Bala b : balas) b.mover();

                // Zombies
                for (Zombie z : zombies) z.moverHacia(jugador.getX(), jugador.getY());
                if (jefe != null && jefe.estaVivo()) jefe.moverHacia(jugador.getX(), jugador.getY());

                // Atacar jugador
                for (Zombie z : zombies) if (z.atacarJugador(jugador)) jugador.perderVida(5);
                if (jefe != null && jefe.estaVivo() && jefe.atacarJugador(jugador)) jugador.perderVida(10);

                // Balas vs zombies y jefe
                List<Bala> balasAEliminar = new ArrayList<>();
                List<Zombie> zombiesMuertos = new ArrayList<>();

                for (Bala b : balas) {
                    boolean impacto = false;
                    for (Zombie z : zombies) {
                        if (b.colision(z)) {
                            z.recibirDanio(50);
                            if (!z.estaVivo()) zombiesMuertos.add(z);
                            puntaje += 1;
                            impacto = true;
                            break;
                        }
                    }
                    if (!impacto && jefe != null && jefe.estaVivo() && b.colision(jefe)) {
                        jefe.recibirDanio(20);
                        if (!jefe.estaVivo()) puntaje += 100;
                        impacto = true;
                    }
                    if (impacto) balasAEliminar.add(b);
                }

                balas.removeAll(balasAEliminar);
                zombies.removeAll(zombiesMuertos);

                // Spawn zombie
                if (random.nextInt(100) < 2) spawnZombie();

                // Spawn vida extra de forma controlada
                if (random.nextInt(800) < 1) {  //cambiar tiempo de aparicion de vidas
                    vidasExtras.add(new VidaExtra());
                }

                // Revisar colisión jugador con vidas extras
                List<VidaExtra> recogidas = new ArrayList<>();
                for (VidaExtra v : vidasExtras) {
                    if (v.colision(jugador)) {
                        jugador.aumentarVida(20);
                        recogidas.add(v);
                    }
                }
                vidasExtras.removeAll(recogidas);

                // Game Over
                if (jugador.getVida() <= 0) {
                    timer.stop();
                    ImageIcon gameOverIcon = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\ciudadzombie.jpg");
                    JOptionPane.showMessageDialog(this, "GAME OVER!!!", "Fin del Juego",
                            JOptionPane.PLAIN_MESSAGE, gameOverIcon);
                    String nombre = JOptionPane.showInputDialog("Ingresa tu nombre:");
                    if (nombre == null || nombre.isEmpty()) nombre = "Jugador";
                    registro.agregarPartida(nombre, puntaje, 0);
                    PlayGamer.this.dispose();
                    menuReferencia.actualizarScores();
                }
            }

            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_P:
                    pausado = !pausado;
                    opcionSeleccionada = 0;
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (pausado) opcionSeleccionada = (opcionSeleccionada - 1 + 3) % 3;
                    else { arriba = true; ultimaDireccion = "ARRIBA"; }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (pausado) opcionSeleccionada = (opcionSeleccionada + 1) % 3;
                    else { abajo = true; ultimaDireccion = "ABAJO"; }
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    izquierda = true; ultimaDireccion = "IZQUIERDA"; break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    derecha = true; ultimaDireccion = "DERECHA"; break;
                case KeyEvent.VK_SPACE:
                    if (!pausado) disparar(); break;
                case KeyEvent.VK_ENTER:
                    if (pausado) {
                        if (opcionSeleccionada == 0) {
                            PlayGamer.this.dispose();
                            menuReferencia.mostrar();
                        } else if (opcionSeleccionada == 1) {
                            PlayGamer.this.dispose();
                            new PlayGamer(registro, menuReferencia);
                        } else if (opcionSeleccionada == 2) {
                            System.exit(0);
                        }
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP: arriba = false; break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN: abajo = false; break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT: izquierda = false; break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT: derecha = false; break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) { }

        private void disparar() {
            if (ultimaDireccion == null) return;
            jugador.setDireccion(ultimaDireccion);
            int[] pos = jugador.getPosicionDisparo();
            int[] vector = jugador.getVectorDisparo(10);
            balas.add(new Bala(
                    pos[0], pos[1],
                    vector[0], vector[1],
                    35, 35,
                    "C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\bala.png"
            ));
        }
    } // Fin GamePanel
} // Fin PlayGamer

