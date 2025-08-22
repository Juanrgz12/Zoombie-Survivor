/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */
import java.awt.Image;        // Para manejar imágenes y GIFs
import java.awt.Graphics;     // Para dibujar imágenes en el panel
import java.awt.Color;        // Para dibujar rectángulos si no hay imagen
import java.awt.Rectangle;    // Para colisiones
import javax.swing.ImageIcon; // Para cargar GIFs o imágenes

/**
 * Clase Zombie
 * Representa un enemigo normal del jugador.
 * Incluye:
 * - Movimiento hacia el jugador
 * - GIF animado
 * - Colisiones centradas
 * - Vida y daño
 * - Ataque con cooldown y sonido
 */
public class Zombie {

    // ------------------ Variables de posición, tamaño y estado ------------------
    private double x, y;              // Posición del zombie (double para permitir velocidad < 1)
    private int ancho, alto;          // Tamaño del zombie (ajustable)
    private double velocidad = 1;   // Velocidad de movimiento (ahora sí se nota)
    private int vida = 100;           // Vida del zombie
    private Image imagenZombie;       // Imagen o GIF del zombie
    private int contadorMov = 0;      // Contador para ralentizar el movimiento

    // ------------------ Variables de ataque ------------------
    private long ultimoAtaque = 0;    // Momento del último ataque (ms)
    private int cooldown = 1000;      // Tiempo de espera entre mordidas (1 seg)

    // ------------------ Constructor ------------------
    /**
     * Crea un zombie en la posición x,y y carga su GIF.
     * @param x posición horizontal inicial
     * @param y posición vertical inicial
     */
    public Zombie(int x, int y) {
        this.x = x; // Guardar posición X
        this.y = y; // Guardar posición Y

        // Cargar GIF del zombie desde la ruta indicada
        ImageIcon icon = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\zombie.GIF");

        // Reducir el GIF a la tercera parte (factor = 3)
        int factor =10; //tamaño de zombie
        ancho = icon.getIconWidth() / factor;
        alto = icon.getIconHeight() / factor;
        imagenZombie = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT);
    }

    // ------------------ Movimiento hacia el jugador ------------------
    /**
     * Mueve el zombie hacia la posición px, py (jugador).
     * Ralentiza el movimiento usando un contador interno.
     */
    public void moverHacia(int px, int py) {
        contadorMov++; // Incrementar contador

        // Solo mover en ticks pares (ralentiza movimiento)
        if (contadorMov % 2 != 0) return;

        // Movimiento horizontal
        if (x < px) x += velocidad;
        if (x > px) x -= velocidad;

        // Movimiento vertical
        if (y < py) y += velocidad;
        if (y > py) y -= velocidad;
    }

    // ------------------ Dibujar zombie ------------------
    /**
     * Dibuja el GIF del zombie en el panel.
     * @param g objeto Graphics del JPanel
     */
    public void dibujar(Graphics g) {
        if (imagenZombie != null) {
            // Convertimos x,y a int al dibujar
            g.drawImage(imagenZombie, (int)x, (int)y, ancho, alto, null);
        } else {
            g.setColor(Color.GREEN); 
            g.fillRect((int)x, (int)y, ancho, alto);
        }
    }

    // ------------------ Colisión con jugador ------------------
    /**
     * Verifica si el zombie colisiona con el jugador.
     * @param p objeto Player
     * @return true si hay colisión
     */
    public boolean colision(Player p) {
        int colX = (int)x + 3;
        int colY = (int)y + 3;
        int colAncho = ancho - 6;
        int colAlto = alto - 6;

        Rectangle r1 = new Rectangle(colX, colY, colAncho, colAlto);
        Rectangle r2 = new Rectangle(p.getX(), p.getY(), p.getAncho(), p.getAlto());
        return r1.intersects(r2);
    }

    // ------------------ Colisión con bala ------------------
    /**
     * Verifica si el zombie colisiona con una bala.
     * @param b objeto Bala
     * @return true si hay colisión
     */
    public boolean colision(Bala b) {
        int colX = (int)x + 3;
        int colY = (int)y + 3;
        int colAncho = ancho - 6;
        int colAlto = alto - 6;

        Rectangle r1 = new Rectangle(colX, colY, colAncho, colAlto);
        Rectangle r2 = b.getBounds();
        return r1.intersects(r2);
    }

    // ------------------ Recibir daño ------------------
    /**
     * Reduce la vida del zombie.
     * @param d cantidad de daño a recibir
     */
    public void recibirDanio(int d) {
        vida -= d;
    }

    // ------------------ Verificar si está vivo ------------------
    /**
     * @return true si el zombie sigue vivo
     */
    public boolean estaVivo() {
        return vida > 0;
    }

    // ------------------ Ataque al jugador ------------------
    /**
     * El zombie intenta atacar al jugador.
     * Solo muerde si hay colisión y pasó el cooldown.
     * @param jugador objeto Player
     * @return true si el ataque fue exitoso
     */
    public boolean atacarJugador(Player jugador) {
        Rectangle r1 = new Rectangle((int)x, (int)y, ancho, alto);
        Rectangle r2 = jugador.getBounds();

        if (r1.intersects(r2)) {
            long ahora = System.currentTimeMillis(); 
            if (ahora - ultimoAtaque > cooldown) {   
                ultimoAtaque = ahora; 

                // Reproducir sonido de mordida
                Sonidos s = new Sonidos();
                s.reproducirEfecto("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\mordida1.wav");

                return true; 
            }
        }
        return false; 
    }

    // ------------------ Getters ------------------
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
}
