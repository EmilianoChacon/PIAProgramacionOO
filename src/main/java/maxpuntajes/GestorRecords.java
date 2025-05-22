package maxpuntajes;

import java.io.*;
import java.util.*;

/**
 * @apiNote Clase simple con el objetivo de crear el archivo donde se guardan los récords o puntajes máximos de cada juego
 */
public class GestorRecords {
    private final String archivo = "records.txt";
    private final Map<String, Integer> records = new HashMap<>();

    /**
     * Constructor del gestor, llama al méto.do de cargarRecords para crear el archivo e inicializar
     * los puntajes y de leer los puntajes en el archivo para tenerlos en el HashMap
     */
    public GestorRecords() {
        cargarRecords();
    }

    /**
     * @apiNote Méto.do con la función de crear el archivo para almacenar los puntajes, leerlos y almacenarlos
     * en el hashmap para su lectura y actualización
     */
    private void cargarRecords() {
        try {
            File file = new File(archivo);
            if (!file.exists()) {
                file.createNewFile();
                inicializarRecords();
                guardarRecords();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(":");
                    if (partes.length == 2) {
                        records.put(partes[0], Integer.parseInt(partes[1]));
                    }
                }
                br.close();
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * @apiNote Méto.do cuya función es inicializar los récords de los juegos a 0 en caso de
     * que el archivo no haya sido creado aún y no haya récords
     */
    private void inicializarRecords() {
        records.put("FlappyBird", 0);
        records.put("Snake", 0);
        records.put("PacMan", 0);
        records.put("SpaceInvaders", 0);
    }

    /**
     * @param juego Nombre del juego al que se quiere ver su récord o puntaje máximo
     * @return Regresa el puntaje máximo correspondiente al juego indicado
     * @apiNote Méto.do para obtener el valor del puntaje máximo actual en cada juego
     */
    public int getRecord(String juego) {
        if(!records.containsKey(juego)) {
            return 0;
        }
        return records.getOrDefault(juego, 0);
    }

    /**
     * @param juego Nombre del juego a actualizar el puntaje
     * @param nuevoPuntaje Puntaje a guardar como nuevo récord en el hashmap
     * @apiNote Méto.do para actualizar el puntaje máximo de cada juego en caso de que se supere al anterior
     * En caso de que si se guarde uno nuevo, se guarda automáticamente en el archivo
     */
    public void actualizarRecord(String juego, int nuevoPuntaje) {
        int actual = records.getOrDefault(juego, 0);
        if (nuevoPuntaje > actual) {
            records.put(juego, nuevoPuntaje);
            guardarRecords();
        }
    }

    /**
     * @apiNote Méto.do para guardar los Récords nuevos y actualizados de cada juego en el archivo creado anteriormente
     */
    private void guardarRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Map.Entry<String, Integer> entry : records.entrySet()) {
                bw.write(entry.getKey() + ":" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
