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
 * Clase Sonidos
 * Maneja todos los sonidos del juego y permite control de volumen global.
 */
class Sonidos { // ya no es public, así no genera conflicto

    private Clip clipFondo;
    private float volumen = 0.5f; // Volumen global (0.0 a 1.0)// ------------------ Reproducir música de fondo ------------------
    public void reproducirFondo(String ruta) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
            clipFondo = AudioSystem.getClip();
            clipFondo.open(audio);
            clipFondo.loop(Clip.LOOP_CONTINUOUSLY);
            ajustarVolumen(clipFondo, volumen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------ Detener fondo ------------------
    public void detenerFondo() {
        if (clipFondo != null && clipFondo.isRunning()) {
            clipFondo.stop();
            clipFondo.close();
        }
    }

    // ------------------ Ajustar volumen global ------------------
    public void setVolumen(float v) {
        volumen = Math.max(0f, Math.min(1f, v));
        if (clipFondo != null) {
            ajustarVolumen(clipFondo, volumen);
        }
    }

    private void ajustarVolumen(Clip clip, float volumen) {
        if (clip != null) {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (20 * Math.log10(volumen <= 0 ? 0.0001 : volumen));
            control.setValue(dB);
        }
    }

    // ------------------ Reproducir efectos cortos ------------------
    public void reproducirEfecto(String ruta) {
        new Thread(() -> {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                ajustarVolumen(clip, volumen);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
