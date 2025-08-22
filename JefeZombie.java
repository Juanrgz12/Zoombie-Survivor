/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */
import java.awt.*;      // Para dibujar gráficos en pantalla
import javax.swing.*;   // Para usar ImageIcon y cargar imágenes

/**
 * Clase JefeZombie
 * Representa al jefe final del juego.
 * Es más grande, más fuerte y da más puntos al ser eliminado.
 */
public class JefeZombie {

    private int x, y;              // Posición en pantalla del jefe
    private int ancho = 150;        // Ancho del jefe (más grande que un zombie normal)
    private int alto = 150;         // Alto del jefe
    private int velocidad = 1;     // Velocidad de movimiento (lento)
    private int vida = 200;        // Puntos de vida del jefe (muy resistente)
    private Image imagenJefe;      // Imagen del jefe en pantalla
    private Sonidos sonidos;       // Objeto para reproducir efectos de sonido

    // Variables para controlar el ataque con cooldown
    private long ultimoAtaque = 0;   // Momento en milisegundos del último ataque
    private int cooldown = 1500;     // Tiempo mínimo entre ataques (1.5 segundos)

    // ------------------ Constructor ------------------
    public JefeZombie(int x, int y) {
        this.x = x;                       // Asigna la posición inicial en X
        this.y = y;                       // Asigna la posición inicial en Y
        this.sonidos = new Sonidos();     // Inicializa el sistema de sonidos

        // Cargar la imagen del jefe desde archivo
        imagenJefe = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\JefeZombie.GIF").getImage();

        // Reproducir sonido de aparición del jefe al entrar en escena
        sonidos.reproducirEfecto("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\zombiegrande1.wav");
    }

    // ------------------ Métodos de movimiento ------------------
    public void moverHacia(int px, int py) {
        // Mueve al jefe en dirección al jugador (px, py)
        if (x < px) x += velocidad;  // Se mueve a la derecha
        if (x > px) x -= velocidad;  // Se mueve a la izquierda
        if (y < py) y += velocidad;  // Se mueve hacia abajo
        if (y > py) y -= velocidad;  // Se mueve hacia arriba
    }

    // ------------------ Dibujar al jefe ------------------
    public void dibujar(Graphics g) {
        if (imagenJefe != null) {
            // Dibuja la imagen del jefe en su posición
            g.drawImage(imagenJefe, x, y, ancho, alto, null);
        } else {
            // Si no hay imagen, dibuja un rectángulo rojo
            g.setColor(Color.RED);
            g.fillRect(x, y, ancho, alto);
        }

        // Dibujar la barra de vida encima del jefe
        g.setColor(Color.RED);
        g.fillRect(x, y - 10, vida / 2, 5);   // Barra proporcional a la vida
        g.setColor(Color.WHITE);
        g.drawRect(x, y - 10, 100, 5);        // Marco de la barra de vida
    }

    // ------------------ Daño recibido ------------------
    public void recibirDanio(int d) {
        vida -= d;   // Resta la cantidad de daño a la vida
        // Reproduce un sonido cuando recibe daño
        sonidos.reproducirEfecto("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\zombie.wav");
    }

    // ------------------ Estado del jefe ------------------
    public boolean estaVivo() {
        return vida > 0;   // Retorna true si aún tiene vida
    }

    // ------------------ Colisión con bala ------------------
    public boolean colision(Bala b) {
        Rectangle r1 = new Rectangle(x, y, ancho, alto); // Rectángulo del jefe
        Rectangle r2 = b.getBounds();                   // Rectángulo de la bala
        return r1.intersects(r2);                       // Retorna true si hay colisión
    }

    // ------------------ Colisión con jugador ------------------
    public boolean colision(Player p) {
        Rectangle r1 = new Rectangle(x, y, ancho, alto); // Rectángulo del jefe
        Rectangle r2 = p.getBounds();                    // Rectángulo del jugador
        return r1.intersects(r2);                        // Retorna true si hay colisión
    }

    // ------------------ ATAQUE AL JUGADOR ------------------
    public boolean atacarJugador(Player jugador) {
        Rectangle r1 = new Rectangle(x, y, ancho, alto);     // Rectángulo del jefe
        Rectangle r2 = jugador.getBounds();                  // Rectángulo del jugador

        // Verifica si el jefe toca al jugador
        if (r1.intersects(r2)) {
            long ahora = System.currentTimeMillis();         // Tiempo actual en milisegundos

            // Verifica si ya pasó el cooldown desde el último ataque
            if (ahora - ultimoAtaque > cooldown) {
                ultimoAtaque = ahora;    // Actualiza el último ataque

                // Reproducir sonido especial de mordida del jefe
                sonidos.reproducirEfecto("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\mordida_jefe.wav");

                return true;  // Retorna true si el ataque fue válido
            }
        }
        return false;  // Retorna false si no pudo atacar
    }

    // ------------------ Getters ------------------
    public int getX() { return x; }       // Devuelve la posición X del jefe
    public int getY() { return y; }       // Devuelve la posición Y del jefe
    public int getAncho() { return ancho; } // Devuelve el ancho del jefe
    public int getAlto() { return alto; }   // Devuelve el alto del jefe
    public int getVida() { return vida; }   // Devuelve la vida restante del jefe
}
