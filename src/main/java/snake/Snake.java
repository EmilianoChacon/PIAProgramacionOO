package snake;

import reproductor.ReproductorSonido;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.Timer;
import java.util.TimerTask;

public class Snake extends JPanel implements ActionListener, KeyListener{
//Este es de los juegos que menos me costó programar y entender ya que solo se basa
// en el movimiento de la serpiente, las colisiones para el gameOver y la comida,
// generarla y que la serpiente se la coma
public int ancho;
    public int alto;
    public int casillaTam = 25;

    public casilla cabeza;
    public ArrayList<casilla> cuerpo;

    public casilla comida;
    Random rand;

    public Timer loop;
    public int velocidadx;
    public int velocidady;

    public boolean gameover = false;
    public int maxpuntaje;

    public casilla comidaEspecial;
    public boolean especialActiva = false;
    private boolean velocidadAumentada = false;

    /**
     * @param ancho ancho del JPanel creado para la ventana del juego
     * @param alto alto del JPanel creado para la ventana del juego
     * @apiNote Constructor para la clase, funciona como iniciador de to.do, desde la ventana,
     * las imagenes, los sonidos, la serpiente, y el inicio de los timers tanto de la generación de comida
     * como de la lógica del juego
     */
    public Snake(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        setPreferredSize(new Dimension(this.ancho, this.alto));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        cabeza = new casilla(5, 5);
        cuerpo = new ArrayList<>();
        comida = new casilla(10, 10);
        comidaEspecial = null;
        rand = new Random();
        colocarcomida();

        velocidadx = 0;
        velocidady = 0;

        loop = new Timer(100, this);
        loop.start();

    }
    //Un metodo sencillo para detectar la colision de ciertos objetos en los juegos
    // lo usare en varios juegos de seguro

    /**
     * @param casilla1 parametro 1 para detectar la colision con otra casilla
     * @param casilla2 casilla con la que podría colisionar la casilla1
     * @return se regresa true en caso de que haya colison y false en caso contrario
     * @apiNote Este méto.do compara las coordenadas de las dos casillas para detectar si hay una colisión
     */
    public boolean colision(casilla casilla1, casilla casilla2) {
        return(casilla1.x == casilla2.x && casilla1.y == casilla2.y);
    }


    /**
     * @apiNote Méto.do para colocar la comida mediante un random
     */
    public void colocarcomida(){
        if (especialActiva) return; // No colocar comida normal si hay una especial activa
        comida.x = rand.nextInt(ancho/casillaTam);
        comida.y = rand.nextInt(alto/casillaTam);
    }

    public void colocarComidaEspecial(){
        comidaEspecial = new casilla(rand.nextInt(ancho/casillaTam), rand.nextInt(alto/casillaTam));
        especialActiva = true;
    }


    /**
     * @apiNote Gran parte de la lógica del juego, aquí se checa la colisión con las comidas, para agrandar
     * el cuerpo de la serpiente, generar más comida, revisar la colisión de la serpiente con los bordes de la ventana
     * y con su propio cuerpo, para así generar el "gameOver" y parar el juego
     */
    public void movimiento(){
        if (!especialActiva && colision(cabeza, comida)) {
            cuerpo.add(new casilla(comida.x, comida.y));
            ReproductorSonido.play("SnakeEating");
            if(rand.nextInt(10) == 0){
                colocarComidaEspecial();
            }
            else  colocarcomida();

        }

        if (especialActiva && colision(cabeza, comidaEspecial)) {
            cuerpo.add(new casilla(comidaEspecial.x, comidaEspecial.y));
            ReproductorSonido.play("logro");

            loop.setDelay(50);
            Timer t = new Timer(3000, e -> loop.setDelay(100));
            t.setRepeats(false);
            t.start();

            especialActiva = false;
            comidaEspecial = null;

            colocarcomida();
        }


        for(int i = cuerpo.size() - 1; i >= 0; i--){
            casilla c = cuerpo.get(i);
            if(i == 0){
                c.x = cabeza.x;
                c.y = cabeza.y;
            }
            else{
                casilla c2 = cuerpo.get(i - 1);
                c.x = c2.x;
                c.y = c2.y;
            }
        }

        cabeza.x += velocidadx;
        cabeza.y += velocidady;
        for (casilla c : cuerpo) {
            if (colision(cabeza, c)) {
                ReproductorSonido.play("SnakeDeath");
                gameover = true;
            }
        }
        //Esto te da un poco de margen al chocar con las paredes,
        // pero esencialmente es el gameOver al chocar con los bordes
        if(cabeza.x < 0 || cabeza.x >= ancho / casillaTam ||
                cabeza.y < 0 || cabeza.y >= alto / casillaTam){
            ReproductorSonido.play("SnakeDeath");
            gameover = true;
        }
    }


    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Méto.do necesario para mostrar imagenes y dibujar en la ventana creada
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote méto.do par dibujar to.do, la serpiente y su cuerpo, la comida, y los mensajes que aparecen
     * en pantalla, osbre gameover y el puntaje
     */
    public void draw(Graphics g) {
        if (especialActiva && comidaEspecial != null) {
            g.setColor(Color.MAGENTA);
            g.fill3DRect(comidaEspecial.x * casillaTam, comidaEspecial.y * casillaTam, casillaTam, casillaTam, true);
        }
        else {
            g.setColor(Color.RED);
            g.fill3DRect(comida.x*casillaTam, comida.y*casillaTam, casillaTam, casillaTam,true);}


        g.setColor(Color.green);
        g.fill3DRect(cabeza.x*casillaTam, cabeza.y*casillaTam, casillaTam, casillaTam,true);

        for (casilla partecuerpo : cuerpo) {
            g.fill3DRect(partecuerpo.x * casillaTam, partecuerpo.y * casillaTam, casillaTam, casillaTam, true);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        if(gameover){
            g.setColor(Color.RED);
            g.drawString("Perdiste:( Puntaje: " + String.valueOf(cuerpo.size()), casillaTam - 16, casillaTam);
            g.drawString("Pulsa ENTER para volver a jugar ", casillaTam - 16, casillaTam + 50);
            if(cuerpo.size() > maxpuntaje){
                maxpuntaje = cuerpo.size();
            }
            g.drawString("Récord: " + String.valueOf(maxpuntaje), casillaTam + 200, casillaTam);
        }
        else{
            g.drawString("Puntaje: " + String.valueOf(cuerpo.size()), casillaTam - 16, casillaTam);
        }

    }


    /**
     * @param e the event to be processed
     * @apiNote méto.do del actionListener, sirve para que cada vez que el timer colocado en el constructor
     * se ejecute, se ejecuten estos meto.dos para que el juego "corra"
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        movimiento();
        repaint();
        if(gameover){
            loop.stop();

        }
    }

    /**
     * @param e the event to be processed
     * @apiNote Meto.do para detectar las teclas, para modificar el movimiento de la serpiente al jugar
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocidady != 1) {
            velocidady = -1;
            velocidadx = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocidady != -1) {
            velocidady = 1;
            velocidadx = 0;

        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocidadx != 1) {
            velocidadx = -1;
            velocidady = 0;
        }else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocidadx != -1) {
            velocidadx = 1;
            velocidady = 0;
        } else if (gameover && e.getKeyCode() == KeyEvent.VK_ENTER) {
            loop.setDelay(100);
            especialActiva = false;
            comidaEspecial = null;
            cabeza.x = 5;
            cabeza.y = 5;
            cuerpo.clear();
            gameover = false;
            colocarcomida();
            loop.start();
            velocidadx = 0;
            velocidady = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
