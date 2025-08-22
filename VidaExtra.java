/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wicho
 */
package videojuego.zombiesurvivor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class VidaExtra {
    private int x, y;        // Posición fija
    private int ancho = 40;
    private int alto = 40;
    private Image imagen;

    /**
     * Constructor: posición aleatoria o fija
     */
    public VidaExtra() {
        // Posición aleatoria dentro de la ventana (puedes cambiarlo a fija si quieres)
        x = (int)(Math.random() * 1100);
        y = (int)(Math.random() * 600);

        imagen = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\vida.png").getImage();
    }

    /**
     * Dibujar en pantalla
     */
    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, ancho, alto, null);
    }

    /**
     * Devuelve el área para detectar colisión
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    /**
     * Detecta si el jugador toca la vida
     */
    public boolean colision(Player jugador) {
        return jugador.getBounds().intersects(getBounds());
    }
}
