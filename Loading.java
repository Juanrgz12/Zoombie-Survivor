/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

import javax.swing.*;               // Importa componentes Swing (JFrame, JLabel, etc.)
import java.awt.*;                  // Importa AWT para colores, fuentes y layout
import java.util.Timer;             // Importa Timer para temporizador
import java.util.TimerTask;         // Importa TimerTask para tareas programadas
import videojuego.zombiesurvivor.RegistroPartidas; // Import para el registro de partidas

/**
 * Clase Loading
 * Muestra un GIF de carga con el texto "Loading..." animado sobre él.
 * Reproduce música de fondo usando la clase Sonidos.
 * Recibe RegistroPartidas y referencia al Menu para pasar el score al juego.
 */
public class Loading extends JFrame {

    private JLabel lblGifConTexto;       // JLabel que mostrará el GIF y el texto
    private int duracionSegundos = 6;    // Tiempo que dura el loading (en segundos)
    private String rutaGif = "C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\ciudadzombi.PNG"; // Ruta del GIF
    private Sonidos sonido;               // Objeto para reproducir sonido de fondo
    private String rutaMusica = "C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\zombiegrande1.WAV"; // Ruta de música

    private RegistroPartidas registro;   // Registro de partidas recibido desde el menú
    private Menu menuReferencia;         // ✅ Referencia al menú que llamó al loading (tipo Menu para actualizar scores)

    /**
     * Constructor
     * @param registro Registro de partidas compartido desde el menú
     * @param menuReferencia Menu que llamó al loading (antes era JFrame)
     */
    public Loading(RegistroPartidas registro, Menu menuReferencia) { // Constructor recibe registro y menú
        this.registro = registro;                 // Asigna el registro recibido
        this.menuReferencia = menuReferencia;     // Asigna la referencia al menú

        // ------------------ Configuración de la ventana ------------------
        setTitle("Cargando...");                  // Título de la ventana
        setSize(800, 600);                        // Tamaño de la ventana
        setLocationRelativeTo(null);              // Centrar ventana en pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar aplicación al cerrar ventana
        getContentPane().setBackground(Color.BLACK);    // Fondo negro
        setLayout(new BorderLayout());                  // Layout tipo BorderLayout

        // ------------------ JLabel con GIF y texto ------------------
        lblGifConTexto = new JLabel("Loading");          // Texto inicial
        lblGifConTexto.setIcon(new ImageIcon(rutaGif)); // Asigna el GIF al JLabel
        lblGifConTexto.setHorizontalTextPosition(JLabel.CENTER); // Texto centrado horizontal sobre el icono
        lblGifConTexto.setVerticalTextPosition(JLabel.CENTER);   // Texto centrado vertical sobre el icono
        lblGifConTexto.setFont(new Font("Arial", Font.BOLD, 48)); // Fuente y tamaño del texto
        lblGifConTexto.setForeground(Color.WHITE);                  // Color blanco al texto
        lblGifConTexto.setHorizontalAlignment(JLabel.CENTER);      // Centrar dentro del JLabel
        lblGifConTexto.setVerticalAlignment(JLabel.CENTER);        // Centrar verticalmente dentro del JLabel

        add(lblGifConTexto, BorderLayout.CENTER); // Agrega el JLabel al JFrame

        // ------------------ Sonido de fondo ------------------
        sonido = new Sonidos();                     // Crea instancia de Sonidos
        sonido.reproducirFondo(rutaMusica);        // Reproduce música de fondo

        setVisible(true);                           // Muestra la ventana

        // ------------------ Temporizador para animar puntos ------------------
        Timer timer = new Timer();                  // Crea un temporizador
        timer.schedule(new TimerTask() {           // Programa tarea que se ejecuta cada segundo
            int segundosTranscurridos = 0;         // Contador de segundos

            @Override
            public void run() {
                segundosTranscurridos++;          // Incrementa contador de segundos

                // Animación de puntos en el texto
                SwingUtilities.invokeLater(() -> { // Asegura que la actualización de JLabel se haga en el hilo de Swing
                    String puntos = "";            // Variable para los puntos animados
                    switch (segundosTranscurridos % 4) { // Ciclo 0,1,2,3 para animar ".", "..", "..."
                        case 0: puntos = ""; break;
                        case 1: puntos = "."; break;
                        case 2: puntos = ".."; break;
                        case 3: puntos = "..."; break;
                    }
                    lblGifConTexto.setText("Loading" + puntos); // Actualiza texto con puntos
                });

                // Finalizar loading cuando dure el tiempo establecido
                if (segundosTranscurridos >= duracionSegundos) {
                    timer.cancel();             // Detiene el temporizador
                    dispose();                  // Cierra ventana de loading
                    sonido.detenerFondo();      // Detiene la música de fondo

                    // ✅ Inicia PlayGamer pasando el registro y la referencia correcta al menú
                    SwingUtilities.invokeLater(() -> new PlayGamer(registro, menuReferencia));
                }
            }
        }, 0, 1000); // Ejecuta la tarea cada 1000 ms (1 segundo)
    }

    // ------------------ Métodos para configuración externa ------------------

    // Cambiar duración del loading
    public void setDuracionSegundos(int duracion) {
        this.duracionSegundos = duracion;
    }

    // Cambiar ruta del GIF
    public void setRutaGif(String rutaGif) {
        this.rutaGif = rutaGif;
        lblGifConTexto.setIcon(new ImageIcon(rutaGif));
    }

    // Cambiar fuente y tamaño del texto
    public void setFuenteTexto(Font fuente) {
        lblGifConTexto.setFont(fuente);
    }

    // Cambiar color del texto
    public void setColorTexto(Color color) {
        lblGifConTexto.setForeground(color);
    }
}

