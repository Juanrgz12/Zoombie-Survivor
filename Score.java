/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */
import javax.swing.*;             // Para componentes de GUI
import java.awt.*;                // Para colores, fuentes y cursores
import java.awt.event.*;          // Para manejo de eventos de mouse y teclado
import java.util.Stack;           // Para manejar la pila de partidas
import javax.sound.sampled.*;     // Para reproducir sonido
import java.io.File;              // Para manejar archivos de sonido

/**
 * Clase Score
 * Muestra las últimas partidas guardadas con puntaje y colisiones.
 * Utiliza RegistroPartidas.Partida para acceder a los datos de cada partida.
 * Incluye fondo gráfico, sonido en loop y opción de salir por mouse o teclado.
 */
public class Score extends JFrame {

    // ------------------ Atributos ------------------
    private Stack<RegistroPartidas.Partida> ultimasPartidas; // Pila de partidas de RegistroPartidas
    private JLabel fondoLabel;                               // Imagen de fondo
    private Clip clip;                                       // Sonido en loop

    /**
     * Constructor
     * @param ultimasPartidas pila de partidas (RegistroPartidas.Partida)
     */
    public Score(Stack<RegistroPartidas.Partida> ultimasPartidas) {
        this.ultimasPartidas = ultimasPartidas;

        // ------------------ Configuración de ventana ------------------
        setTitle("Scores - Dead Zone Survival"); // Título de la ventana
        setSize(600, 600);                       // Tamaño de la ventana
        setLocationRelativeTo(null);             // Centrar la ventana en pantalla
        setLayout(null);                         // Layout manual para posicionamiento libre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana

        // ------------------ Cargar imagen de fondo ------------------
        ImageIcon fondoIcon = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\Score.jpg");
        fondoLabel = new JLabel(fondoIcon);
        fondoLabel.setBounds(0, 0, 600, 600);    // Posición y tamaño del fondo
        fondoLabel.setLayout(null);              // Permite añadir componentes encima del fondo
        add(fondoLabel);                         // Añadir fondo a la ventana

        // ------------------ Mostrar puntajes ------------------
        int yPos = 50;                           // posición inicial vertical
        int posicion = ultimasPartidas.size();   // numeración descendente
        for (RegistroPartidas.Partida p : ultimasPartidas) {
            JLabel lbl = new JLabel(
                posicion + ". " + p.getNombreJugador() + 
                " - " + p.getPuntaje() + 
                " (Colisiones: " + p.getColisiones() + ")"
            );
            lbl.setForeground(Color.WHITE);              // Color del texto
            lbl.setFont(new Font("Arial", Font.BOLD, 20)); // Fuente y tamaño
            lbl.setBounds(50, yPos, 500, 30);           // Posición en la ventana
            fondoLabel.add(lbl);                         // Añadir al fondo
            yPos += 40;                                  // Espacio entre puntajes
            posicion--;                                  // Decrementar numeración
        }

        // ------------------ Label de Exit ------------------
        JLabel lblExit = new JLabel("Exit  (Esc)");
        lblExit.setBounds(480, 30, 100, 20);           // esquina superior derecha
        lblExit.setForeground(Color.white);            // color inicial
        lblExit.setFont(new Font("Arial", Font.BOLD, 20));
        lblExit.setHorizontalAlignment(SwingConstants.CENTER);
        lblExit.setCursor(new Cursor(Cursor.HAND_CURSOR)); // cursor de mano
        fondoLabel.add(lblExit);                        // añadir al fondo

        // ------------------ Mouse Listener (click en Exit) ------------------
        lblExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // cerrar ventana al hacer click
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblExit.setForeground(Color.YELLOW); // resalta al pasar mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblExit.setForeground(Color.RED); // rojo cuando no hay hover
            }
        });

        // ------------------ Key Listener (Enter/Esc) ------------------
        fondoLabel.setFocusable(true);               // permitir capturar teclado
        fondoLabel.requestFocusInWindow();
        fondoLabel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                    dispose(); // cerrar ventana al presionar Enter o Esc
                }
            }
        });

        // ------------------ Reproducir sonido al abrir ------------------
        // reproducirSonido("C:\\Users\\s-had\\OneDrive\\Escritorio\\comjuego\\sound.wav");

        setVisible(true); // hacer visible la ventana
    }

    // ------------------ Método para reproducir sonido ------------------
    private void reproducirSonido(String ruta) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // loop infinito
            clip.start();                      // iniciar sonido
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ Detener sonido al cerrar ventana ------------------
    @Override
    public void dispose() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        super.dispose();
    }
}
