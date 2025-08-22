/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

import javax.swing.*;      
import java.awt.*;          
import java.awt.event.*;    
import videojuego.zombiesurvivor.RegistroPartidas.Partida; 
import videojuego.zombiesurvivor.RegistroPartidas;         

/**
 * Clase Menu
 * Ventana principal del juego con etiquetas de texto clicables,
 * panel de juego y sonido de fondo.
 */
public class Menu {
    private JFrame ventana;               // Ventana principal del juego
    private ZombieSurvivor juegoPanel;    // Panel de juego donde se dibujan elementos
    private JLabel[] opciones;            // Array con las opciones del menú
    private int seleccionActual = 0;      // Opción actualmente seleccionada
    private Sonidos sonidos;              // Clase para manejar sonidos de fondo
    private RegistroPartidas registro;    // Registro centralizado de partidas jugadas
    private JLabel lblScore;              // Label que muestra el último score

    /**
     * Método que será llamado desde PlayGamer para actualizar scores
     * actualmente solo imprime un mensaje en consola
     */
    public void actualizarScores() {
        System.out.println("Actualizar scores llamado desde PlayGamer");
    }

    // ------------------ Constructor ------------------
    public Menu() {
        // ------------------ Inicializar ventana ------------------
        ventana = new JFrame("Dead Zone Survival");                  
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        ventana.setSize(800, 600);                                  
        ventana.setLocationRelativeTo(null);                        
        ventana.setLayout(new BorderLayout());                       

        // ------------------ Inicializar sonidos ------------------
        sonidos = new Sonidos();                                     
        sonidos.reproducirFondo("C:\\Users\\wicho\\OneDrive\\Desktop\\ZombieSurvivor\\src\\main\\resources\\horrormenu1.wav");

        // ------------------ Inicializar registro de partidas ------------------
        registro = new RegistroPartidas(10);                         

        // ------------------ Crear panel de juego ------------------
        juegoPanel = new ZombieSurvivor();                           
        juegoPanel.setLayout(null);                                  
        juegoPanel.requestFocusInWindow();                           

        // ------------------ Crear etiquetas de texto clicables ------------------
        JLabel lblIniciar = new JLabel("Iniciar Juego");             
        lblIniciar.setBounds(300, 300, 200, 50);                     
        lblIniciar.setForeground(Color.WHITE);                       
        lblIniciar.setFont(new Font("Arial", Font.BOLD, 24));        
        lblIniciar.setHorizontalAlignment(SwingConstants.CENTER);    
        lblIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));        
        lblIniciar.addMouseListener(new MouseAdapter() {             
            @Override
            public void mouseClicked(MouseEvent e) {                 
                sonidos.detenerFondo();                               
                new Loading(registro, Menu.this);                    
            }
        });

        JLabel lblSalir = new JLabel("Salir (Esc)");                 
        lblSalir.setBounds(290, 400, 200, 50);                      
        lblSalir.setForeground(Color.WHITE);                        
        lblSalir.setFont(new Font("Arial", Font.BOLD, 24));         
        lblSalir.setHorizontalAlignment(SwingConstants.CENTER);     
        lblSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));         
        lblSalir.addMouseListener(new MouseAdapter() {              
            @Override
            public void mouseClicked(MouseEvent e) {                
                sonidos.detenerFondo();                               
                System.exit(0);                                      
            }
        });

        // ------------------ Crear label Score ------------------
        lblScore = new JLabel("Score"); 
        lblScore.setBounds(330, 350, 250, 50); 
        lblScore.setForeground(Color.YELLOW); 
        lblScore.setFont(new Font("Arial", Font.BOLD, 24));
        lblScore.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        lblScore.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Score(registro.getUltimasPartidas());
            }
        });

        // ------------------ Inicializar array de opciones ------------------
        opciones = new JLabel[]{lblIniciar, lblSalir, lblScore};    
        actualizarSeleccion();                                      

        // ------------------ Agregar etiquetas al panel ------------------
        for (JLabel lbl : opciones) {
            juegoPanel.add(lbl);                                     
        }

        // ------------------ Configurar Key Bindings ------------------
        configurarTeclas();

        // ------------------ Agregar panel a la ventana ------------------
        ventana.add(juegoPanel, BorderLayout.CENTER);               
        ventana.setVisible(true);                                    
    }

    // ------------------ Actualizar selección ------------------
    private void actualizarSeleccion() {
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].setForeground(i == seleccionActual ? Color.YELLOW : Color.WHITE);
        }
    }

    // ------------------ Configurar Key Bindings ------------------
    private void configurarTeclas() {
        // Tecla UP
        juegoPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                  .put(KeyStroke.getKeyStroke("UP"), "subir");
        juegoPanel.getActionMap().put("subir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionActual = (seleccionActual - 1 + opciones.length) % opciones.length;
                actualizarSeleccion();
            }
        });

        // Tecla DOWN
        juegoPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                  .put(KeyStroke.getKeyStroke("DOWN"), "bajar");
        juegoPanel.getActionMap().put("bajar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionActual = (seleccionActual + 1) % opciones.length;
                actualizarSeleccion();
            }
        });

        // Tecla ENTER
        juegoPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                  .put(KeyStroke.getKeyStroke("ENTER"), "ejecutar");
        juegoPanel.getActionMap().put("ejecutar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel actual = opciones[seleccionActual];
                switch (actual.getText()) {
                    case "Iniciar Juego" -> iniciarJuego();
                    case "Salir (Esc)" -> salirJuego();
                    case "Score" -> new Score(registro.getUltimasPartidas());
                }
            }
        });

        // Tecla ESCAPE
        juegoPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                  .put(KeyStroke.getKeyStroke("ESCAPE"), "salir");
        juegoPanel.getActionMap().put("salir", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sonidos.detenerFondo();
                System.exit(0);
            }
        });
    }

    // ------------------ Iniciar juego ------------------
    private void iniciarJuego() {
        sonidos.detenerFondo();
        new Loading(registro, this);
    }

    // ------------------ Salir del juego ------------------
    private void salirJuego() {
        sonidos.detenerFondo();
        System.exit(0);
    }

    // ------------------ Mostrar ventana ------------------
    public void mostrar() {
        ventana.setVisible(true);
    }

    // ------------------ Main ------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}
