package Space;

import java.awt.*;

/**
 * @apiNote Clase hija de Bloque, principalmente para agregar el ángulo de Rotación para el giro de la estrella
 */
public class PowerUp extends Bloque{
    double anguloRotacion;
    /**
     * @param x      coordenada x del objeto
     * @param y      coordenada y del objeto
     * @param ancho  ancho del objeto
     * @param alto   alto del objeto
     * @param imagen imagen del objeto(powerUps)
     * @apiNote Constructor de la clase
     */
    public PowerUp(int x, int y, int ancho, int alto, Image imagen) {
        super(x, y, ancho, alto, imagen);
        this.anguloRotacion = 0;
    }
}
