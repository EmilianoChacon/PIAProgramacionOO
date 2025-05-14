package pac_man;

import java.awt.*;

/**
 * @apiNote Clase principal que representa casi todos los objetos del juego, con sus métodos
 * y atributos necesarios
 */
public class Bloque{
    private final int tamcasillas = 32;
    int x;
    int y;
    int ancho;
    int alto;
    Image img;
    int iniciox;
    int inicioy;
    char direccion = 'U';
    int velocidadx = 0;
    int velocidady = 0;


    /**
     * @param image Imagen actual del bloque en cuestión
     * @param x posición en la coordenada x del bloque
     * @param y posición en la coordenada y del bloque
     * @param ancho tamaño en ancho del bloque
     * @param alto tamaño en alto del bloque
     * @apiNote Constructor para definir todos los atributos de cada bloque
     */
    public Bloque(Image image, int x, int y, int ancho, int alto){
    this.img = image;
    this.x = x;
    this.y = y;
    this.ancho = ancho;
    this.alto = alto;
    this.iniciox = x;
    this.inicioy = y;

    }

    /**
     * @param a Bloque a para checar la colisión
     * @param b Bloque b para checar la colisión
     * @return Regresa True en caso de una colisión entre dos entidades y False en caso contario
     * @apiNote Méto, do para la detección de colisión entre dos entidades para evitar sobre-posición
     */
    public boolean colision(Bloque a, Bloque b){
        return a.x < b.x + b.ancho && a.y < b.y + b.alto &&
                a.x + a.ancho > b.x && a.y + a.alto > b.y;
    }
//Este es el méto.do para que los fantasmas se muevan aleatoriamente cada que choquen con un muro
public void actdireccion(char direccion){
    char prevdireccion = this.direccion;
    this.direccion = direccion;
    actvelocidad();
    this.x += velocidadx;
    this.y += velocidady;
    for(Bloque muro: PacMan.muros){
        if(colision(this, muro)){
            this.x -= this.velocidadx;
            this.y -= this.velocidady;
            this.direccion = prevdireccion;
            actvelocidad();
        }
    }
}

    /**
     * Méto. do para cambiar la dirección de movimiento de los objetos como los fantasmas o Pac-man
     */
//Funcion que sirve tanto para los fantasmas como para Pac-man a la hora de moverse
public void actvelocidad(){
    if(direccion == 'U'){
        velocidadx = 0;
        velocidady = -tamcasillas/4;
    }
    else if(direccion == 'D'){
        velocidadx = 0;
        velocidady = tamcasillas/4;
    }
    else if(direccion == 'L'){
        velocidadx = -tamcasillas/4;
        velocidady = 0;
    }
    else if(direccion == 'R'){
        velocidadx = tamcasillas/4;
        velocidady = 0;
    }
}

    /**
     * Méto. do para reiniciar las posiciones de cada entidad que es capaz de moverse
     */
//funciones para reiniciar las posiciones de todos
public void reiniciar(){
    this.x = this.iniciox;
    this.y = this.inicioy;
}


    }