/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */
import javax.swing.*;          
import java.awt.*;             
import java.awt.event.*;       
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;

/**
 * Clase ZombieSurvivor
 * Panel principal del juego donde se dibuja el fondo y se maneja la lógica.
 */
public class ZombieSurvivor extends JPanel {

    // ------------------ Ajustes del panel ------------------
    private int panelWidth = 800;           // Ancho del panel
    private int panelHeight = 600;          // Alto del panel
    private Color backgroundColor = Color.BLACK; // Color de fondo por defecto

    // Fondo con imagen
    private Image fondoImagen; 

    // ------------------ Bucle principal del juego ------------------
    private Timer gameTimer;                // Timer para actualizar el juego
    private int fps = 60;                   // Frames por segundo

    // ------------------ Constructor ------------------
    public ZombieSurvivor() {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setFocusable(true);
        this.requestFocusInWindow();

        // Cargar la imagen de fondo (ruta absoluta o relativa)
        fondoImagen = new ImageIcon("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\poster.png").getImage();

        // ------------------ Configurar timer del juego ------------------
        gameTimer = new Timer(1000 / fps, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame(); // Actualiza la lógica del juego
                repaint();    // Redibuja el panel
            }
        });
        gameTimer.start();
    }

    // ------------------ Métodos del juego ------------------
    private void updateGame() {
        // TODO: Lógica del juego (enemigos, colisiones, puntaje)
        // Aquí podrías agregar llamadas a sonidos, por ejemplo:
        // si ocurre un disparo: sonidos.reproducirEfecto("ruta_disparo.wav");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar fondo con imagen escalada
        if (fondoImagen != null) {
            g.drawImage(fondoImagen, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // TODO: Dibujar elementos del juego
    }

    // ------------------ Métodos de ajustes ------------------
    public void setPanelSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        this.setPreferredSize(new Dimension(width, height));
        this.revalidate();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    public void setFPS(int fps) {
        this.fps = fps;
        gameTimer.setDelay(1000 / fps);
    }

    public void setFondoImagen(String ruta) {
        fondoImagen = new ImageIcon(ruta).getImage();
        repaint();
    }

    // ------------------ Método para iniciar el juego ------------------
    public void iniciarJuego() {
        JOptionPane.showMessageDialog(this, "¡El juego ha iniciado!");
        // Aquí se puede iniciar enemigos, timers adicionales, puntaje, etc.
        // También puedes reproducir música de fondo aquí si no la iniciaste en el constructor:
        // sonidos.reproducirFondo("ruta_de_tu_musica.wav");
    }
}
