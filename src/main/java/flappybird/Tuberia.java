package flappybird;

import java.awt.*;

public class Tuberia {
    int tuberiaX = 360;
    int tuberiaY = 0;
    int tuberiaAncho = 64;
    int tuberiaAlto = 512;
    Image imagen;
    boolean pasado = false;

    Tuberia(Image imagen) {
        this.imagen = imagen;
    }
}
