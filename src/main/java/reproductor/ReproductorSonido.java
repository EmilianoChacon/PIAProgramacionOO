package reproductor;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReproductorSonido {

    public static final Map<String, Clip> clips = new HashMap<>();

    /**
     * @param id Identificador para el sonido cargado al hashmap de sonidos, para una reproducción más rápida y eficiente
     * @param path Ruta del sonido, básicamente la ubicación del sonido en las carpetas
     * @apiNote Este metodo sirve para cargar los sonidos, asegurar que estan en su lugar y principalmente
     *          tenerlos organizados y bien ubicados para su futuro uso
     */
    public static void loadSound(String id, String path) {
        try {
            File archivo = new File(path);


            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clips.put(id, clip);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param id Identificador del sonido o música a reproducir
     * @apiNote Métod.do para reproducir los sonidos previamente cargados, con unas comprobaciones
     * para que los sonidos no se sobrepongan y se reproduzca un mismo clip a la vez
     */
    public static void play(String id) {
        Clip clip = clips.get(id);
        if (clip != null) {
            if (!clip.isRunning()) {
                clip.setFramePosition(0); // Vuelve al inicio
                clip.start();    // Detiene si está corriendo para reiniciarlo
            }// Reproduce
        } else {
            System.err.println("No se ha cargado el sonido con id: " + id);
        }
    }

}
