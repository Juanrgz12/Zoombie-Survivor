/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */
import java.awt.Image;         
import java.awt.Graphics;      
import java.awt.Graphics2D;    // Necesario para rotar imágenes
import java.awt.Color;         
import java.awt.Rectangle;     
import java.awt.geom.AffineTransform; // Para aplicar rotaciones
import javax.swing.ImageIcon;  

/**
 * Clase Bala
 * Representa la bala disparada por el jugador.
 * Se mueve en una dirección fija y su imagen se rota
 * para apuntar hacia donde va.
 */
public class Bala {

    // ------------------ Propiedades ------------------
    private double x, y;          // Posición actual de la bala
    private int ancho, alto;      // Tamaño de la bala
    private double dx, dy;        // Vector de movimiento
    private Image imagenBala;     // Imagen de la bala (GIF o PNG)
    private double angulo;        // Ángulo de la bala en radianes

    // ------------------ Constructor ------------------
    public Bala(int x, int y, double dx, double dy, int ancho, int alto, String rutaImagen) {
        this.x = x;               
        this.y = y;               
        this.dx = dx;             
        this.dy = dy;             
        this.ancho = ancho;       
        this.alto = alto;         

        // Calcular el ángulo de la bala a partir de dx y dy
        this.angulo = Math.atan2(dy, dx);

        // Intentar cargar imagen
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                imagenBala = new ImageIcon(rutaImagen).getImage();
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen de la bala: " + rutaImagen);
                imagenBala = null;
            }
        }
    }

    // ------------------ Movimiento ------------------
    public void mover() {
        x += dx;
        y += dy;
    }

    public boolean estaEnPantalla(int width, int height) {
        return x + ancho > 0 && x < width && y + alto > 0 && y < height;
    }

    // ------------------ Dibujar con rotación ------------------
    public void dibujar(Graphics g) {
        if (imagenBala != null) {
            Graphics2D g2d = (Graphics2D) g;

            // Guardar estado del contexto gráfico
            AffineTransform old = g2d.getTransform();

            // Crear transformación: mover al centro de la bala, rotar, y luego dibujar
            g2d.translate(x + ancho / 2, y + alto / 2);
            g2d.rotate(angulo);
            g2d.drawImage(imagenBala, -ancho / 2, -alto / 2, ancho, alto, null);

            // Restaurar el estado del contexto
            g2d.setTransform(old);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect((int)x, (int)y, ancho, alto);
        }
    }

    // ------------------ Colisiones ------------------
    public boolean colision(Zombie z) {
        return getBounds().intersects(
            new Rectangle(z.getX(), z.getY(), z.getAncho(), z.getAlto())
        );
    }

    public boolean colision(JefeZombie j) {
        if (j == null) return false;
        return getBounds().intersects(
            new Rectangle(j.getX(), j.getY(), j.getAncho(), j.getAlto())
        );
    }

    // ------------------ Getters ------------------
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public Rectangle getBounds() { 
        return new Rectangle((int)x, (int)y, ancho, alto); 
    }
}