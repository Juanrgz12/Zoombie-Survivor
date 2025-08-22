/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Clase Player
 * Representa al jugador del juego con:
 * - Posición (x, y)
 * - Vida
 * - Imagen (GIF o PNG)
 * - Dirección de movimiento y orientación (ángulo)
 * - Colisiones
 * - Disparo direccional
 */
public class Player {
    private int x, y;                  // Coordenadas del jugador en pantalla
    private int ancho = 90, alto = 100;// Tamaño del jugador
    private int vida = 200;            // Vida inicial del jugador
    private final int VIDA_MAXIMA = 200; // Vida máxima
    private Image imagenJugador;       // Imagen GIF del jugador

    // Orientación
    private double angulo = 0;         // Ángulo de rotación en radianes
    private String direccionActual = "DERECHA"; // Última dirección usada

    /**
     * Constructor de Player
     * @param x posición inicial en X
     * @param y posición inicial en Y
     */
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        // Cargar imagen GIF del jugador desde la ruta especificada
        imagenJugador = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\soldado.gif").getImage();
    }

    /**
     * Mover jugador en el plano
     * @param dx desplazamiento horizontal
     * @param dy desplazamiento vertical
     */
    public void mover(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Posición inicial de la bala (en la punta del arma)
     * @return {x, y} de salida de la bala
     */
    public int[] getPosicionDisparo() {
        int offset = ancho / 2;
        int px = (int)(x + ancho / 2 + Math.cos(angulo) * offset);
        int py = (int)(y + alto / 2 + Math.sin(angulo) * offset);
        return new int[]{px, py};
    }

    /**
     * Vector de movimiento de la bala según dirección del jugador
     * @param velocidad velocidad de la bala
     * @return {dx, dy}
     */
    public int[] getVectorDisparo(int velocidad) {
        int vx = (int)(Math.cos(angulo) * velocidad);
        int vy = (int)(Math.sin(angulo) * velocidad);
        return new int[]{vx, vy};
    }

    /**
     * Dibujar al jugador en pantalla con la orientación correcta
     */
    public void dibujar(Graphics g) {
        if (imagenJugador != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.rotate(angulo, x + ancho / 2, y + alto / 2);
            g2d.drawImage(imagenJugador, x, y, ancho, alto, null);
            g2d.dispose();
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, ancho, alto);
        }
    }

    /**
     * Reducir vida del jugador
     */
    public void perderVida(int d) {
        vida -= d;
        if (vida < 0) vida = 0;
    }

    /**
     * Aumentar vida del jugador (nuevo método)
     * @param cantidad cantidad de vida a añadir
     */
    public void aumentarVida(int cantidad) {
        vida += cantidad;
        if (vida > VIDA_MAXIMA) vida = VIDA_MAXIMA;
    }

    // ====== Getters básicos ======
    public int getVida() { return vida; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
    public Rectangle getBounds() { return new Rectangle(x, y, ancho, alto); }

    /**
     * Cambia la dirección del jugador y actualiza el ángulo.
     */
    public void setDireccion(String dir) {
        direccionActual = dir; // Guardar última dirección
        switch(dir) {
            case "ARRIBA":           angulo = -Math.PI / 2; break;
            case "ABAJO":            angulo = Math.PI / 2; break;
            case "IZQUIERDA":        angulo = Math.PI; break;
            case "DERECHA":          angulo = 0; break;
            case "ARRIBA_DERECHA":   angulo = -Math.PI / 4; break;
            case "ARRIBA_IZQUIERDA": angulo = -3*Math.PI / 4; break;
            case "ABAJO_DERECHA":    angulo = Math.PI / 4; break;
            case "ABAJO_IZQUIERDA":  angulo = 3*Math.PI / 4; break;
        }
    }

    // ====== Getters de orientación ======
    public String getDireccion() { return direccionActual; }
    public double getAngulo() { return angulo; }
}
