package Space;

import java.awt.*;

/**
 * Clase de bloque, que sirve para la representación de los objetos del mapa y del juego
 */
public class Bloque {
    int x;
    int y;
    int ancho;
    int alto;
    Image imagen;
    public boolean vivo = true;
    boolean usado = false;

    /**
     * @param x coordenada x del objeto
     * @param y coordenada y del objeto
     * @param ancho ancho del objeto
     * @param alto alto del objeto
     * @param imagen imagen del objeto(aliens, nave, balas)
     * @apiNote Constructor de la clase, para tener todos los atributos a cada entidad
     */
    public Bloque(int x, int y, int ancho, int alto, Image imagen) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.imagen = imagen;
    }
}