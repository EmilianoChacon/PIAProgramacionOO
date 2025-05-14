package pac_man;

import java.awt.*;

/**
 * @apiNote Clase hija que extiende la clase Bloque, para tener separados los atributos de los fantasmas
 */
public class Fantasma extends Bloque{
    Image imgOriginal;
    boolean  soloOjos = false; // booleano para los fantasmas, a la hora de ser comidos
    public boolean vulnerabilidad;

    /**
     * @param image Imagen actual del fantasma
     * @param x posición en la coordenada x del fantasma
     * @param y posición en la coordenada y del fantasma
     * @param ancho tamaño en ancho del fantasma
     * @param alto tamaño en alto del fantasma
     * @apiNote Constructor similar al de la clase Bloque, con dos nuevos atributos para los fantasmas
     */
    public Fantasma(Image image, int x, int y, int ancho, int alto) {
        super(image, x, y, ancho, alto);
        this.imgOriginal = image;
        vulnerabilidad = false;
    }

    /**
     * Méto.do para reiniciar los fantasmas después del efecto de las pastillas especiales
     */
//Este méto. Do es para después de las pastillas especiales
//Para que los fantasmas vuelvan a su estado normal
    public void reiniciarfant(){
        this.img = this.imgOriginal;
    }


}
