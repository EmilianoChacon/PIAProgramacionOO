package flappybird;

import java.awt.*;

public class Tuberia {
    public int tuberiaX = 360;
    public int tuberiaY = 0;
    int tuberiaAncho = 64;
    int tuberiaAlto = 512;
    Image imagen;
    boolean pasado = false;

    public Tuberia(Image imagen) {
        this.imagen = imagen;
    }
}
