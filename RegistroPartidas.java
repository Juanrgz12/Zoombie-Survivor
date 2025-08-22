/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuego.zombiesurvivor;

/**
 *
 * @author s-had
 */

/**
 * Clase RegistroPartidas
 * Guarda las últimas N partidas jugadas con puntaje y total de colisiones.
 * Se integra con Score para mostrar los resultados.
 */
import java.util.Stack; // Importamos la clase Stack para manejar la pila de partidas

/**
 * Clase RegistroPartidas
 * Maneja el registro de partidas recientes del juego.
 */
public class RegistroPartidas {

    private Stack<Partida> ultimasPartidas; // Pila que almacena las partidas recientes
    private int maxPartidas;                // Máximo número de partidas a guardar

    /**
     * Constructor
     * @param maxPartidas número máximo de partidas a guardar
     */
    public RegistroPartidas(int maxPartidas) {
        this.maxPartidas = maxPartidas;       // Guardamos el límite de partidas
        this.ultimasPartidas = new Stack<>(); // Inicializamos la pila de partidas
    }

    /**
     * Agregar una partida al registro
     * @param nombreJugador nombre del jugador
     * @param puntaje puntaje obtenido
     * @param colisiones total de colisiones (zombies o jefe)
     */
    public void agregarPartida(String nombreJugador, int puntaje, int colisiones) {
        ultimasPartidas.push(new Partida(nombreJugador, puntaje, colisiones)); // Creamos y añadimos la partida

        // Mantener solo las últimas maxPartidas en la pila
        while (ultimasPartidas.size() > maxPartidas) { 
            ultimasPartidas.remove(0); // Eliminamos la partida más antigua si excede el límite
        }
    }

    /**
     * Obtener la pila de últimas partidas
     * @return Stack<Partida>
     */
    public Stack<Partida> getUltimasPartidas() {
        return ultimasPartidas; // Retornamos la pila de partidas
    }

    // ------------------ Clase interna Partida ------------------
    public static class Partida {
        private final String nombreJugador; // Nombre del jugador
        private final int puntaje;          // Puntaje obtenido en la partida
        private final int colisiones;       // Número de colisiones durante la partida

        public Partida(String nombreJugador, int puntaje, int colisiones) {
            this.nombreJugador = nombreJugador; // Guardamos el nombre del jugador
            this.puntaje = puntaje;             // Guardamos el puntaje obtenido
            this.colisiones = colisiones;       // Guardamos el número de colisiones
        }

        public String getNombreJugador() { return nombreJugador; } // Retorna el nombre del jugador
        public int getPuntaje() { return puntaje; }               // Retorna el puntaje
        public int getColisiones() { return colisiones; }         // Retorna las colisiones

        @Override
        public String toString() {
            // Formato de texto para mostrar la partida
            return nombreJugador + " - Puntaje: " + puntaje + " - Colisiones: " + colisiones;
        }
    }
}
