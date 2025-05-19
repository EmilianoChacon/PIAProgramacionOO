package flappybird;

import reproductor.ReproductorSonido;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * @apiNote Clase de flappybird para el juego de dicho nombre, primero se declaran unos cuantos atributos que
 * servirán para la lógica del juego, y las declaraciones de algunas cosas
 */
/*También uno de los juegos sencillos, no me causo tanto problema, aunque se me hizo curioso
 como se maneja para que el pajaro caiga y suba con los saltos, al agregar un efecto de "gravedad"
  que solo cambia la velocidad del pájaro para que se vaya hacia abajo*/
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int ancho = 360;
    public int alto = 640;

    private Image fondoImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
    private final Image fondoImg2 = new ImageIcon(getClass().getResource("/flappybirdbg2.png")).getImage();
    private final Image fondoImg3 = new ImageIcon(getClass().getResource("/flappybirdbg3.png")).getImage();
    private final Image fondoImg4 = new ImageIcon(getClass().getResource("/flappybirdbg4.png")).getImage();
    private final Image pajaroImg = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
    private final Image tuberiaarribaImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
    private final Image tuberiaabajoImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();




    public Pajaro pajaro;
    double velocidadX = -4;
    public int velocidadY = 0;
    int gravedad = 1;

    public ArrayList<Tuberia> tuberias;
    Random rand = new Random();

    public Timer loop;
    public Timer colocacionTuberias;
    public int delayColocacion;

    public boolean gameOver = false;
    public double puntaje = 0;
    public int maxPuntaje = 0;
    int temaActual = 1;


    /**
     * @apiNote Este es el constructor de la clase, sirve para declarar y agregar muchas cosas
     * como las imágenes, los sonidos crear la ventana del juego y los timers necesarios para la lógica del juego
     * 2 timers, uno para controlar la colocacion de tuberias al azar y el otro para los frames del juego
     */
    public FlappyBird(){
        setPreferredSize(new Dimension(ancho, alto));

        setFocusable(true);
        addKeyListener(this);

        pajaro = new Pajaro(pajaroImg);

        tuberias = new ArrayList<Tuberia>();


        colocacionTuberias = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colocarTuberias();
            }
        });
        colocacionTuberias.start();

        loop = new Timer(1000/60, this);
        loop.start();

    }


    /**
     * @apiNote Méto.do para generar las tuberias, se usa un random() para colocar la tuberia en el eje Y
     * y junto con una variable de espacio, colocamos la otra tuberia con el espacio necesario
     */
    /*Es un metodo curioso, que con unos numeros aleatorios se van colocando en altura aleatoria
    * se crea un espacio exacto entre la tuberia de arriba y de abajo*/
    public void colocarTuberias(){
        int randomTuberiaY = (int) (0 - 512/4 - Math.random()*512/2);
        int espacio = alto/4;

        Tuberia tuberiaArriba = new Tuberia(tuberiaarribaImg);
        tuberiaArriba.tuberiaY = randomTuberiaY;
        tuberias.add(tuberiaArriba);

        Tuberia tuberiaAbajo = new Tuberia(tuberiaabajoImg);
        tuberiaAbajo.tuberiaY = tuberiaArriba.tuberiaY + espacio + 512;
        tuberias.add(tuberiaAbajo);
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Metodo para dibujar las cosas en la ventana
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Metodo en el que se dibujan todas las cosas, el fondo, el pájaro, las tuberias y el texto
     * de gameover y de puntucacion
     */
    public void draw(Graphics g) {
        //System.out.println("Prueba fps");
        g.drawImage(fondoImg, 0, 0, ancho, alto,null);
        g.drawImage(pajaro.imagen,pajaro.x,pajaro.y,pajaro.ancho,pajaro.alto,null);

        for(int i = 0; i < tuberias.size(); i++){
            Tuberia tuberia = tuberias.get(i);
            g.drawImage(tuberia.imagen,tuberia.tuberiaX,tuberia.tuberiaY,tuberia.tuberiaAncho,tuberia.tuberiaAlto,null);

        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if(gameOver){
            g.drawString("Perdiste:( Puntaje: " + String.valueOf((int)puntaje), 10, 35);
            if((int)puntaje > maxPuntaje){
                maxPuntaje = (int)puntaje;
            }
            g.drawString("Puntaje maximo: " + String.valueOf(maxPuntaje), 10, 65);

        }
        else{
            g.drawString("Puntaje: " + String.valueOf((int)puntaje), 10, 35);
        }
    }

    /**
     * @apiNote méto.do en donde está toda la lógica del juego
     * Primero se va actualizando la velocidad del pajaro para que se pueda mover
     * luego se revisa si el pajaro cruzó las tuberias para recibir la puntuacion, cambiar la velocidad
     * y cambiar el fondo en casos especificos
     * Tambien se revisa la colisión del pajaro con las tuberías y con el fondo para el gameOver
     */
    public void movimiento(){
        velocidadY += gravedad;
        pajaro.y += velocidadY;
        pajaro.y = Math.max(pajaro.y,0);

        for(int i = 0; i < tuberias.size(); i++){
            Tuberia tuberia = tuberias.get(i);
            tuberia.tuberiaX += velocidadX;

            if(!tuberia.pasado && pajaro.x > tuberia.tuberiaX + tuberia.tuberiaAncho){

                tuberia.pasado = true;
                puntaje += 0.5;
                //Esto lo agregué para poner el sonido cada que se pasa por un par de tuberías
                if(puntaje % 1 == 0){
                    ReproductorSonido.play("point");
                    //Esta parte es para hacer el juego un poco más dificil,
                    // cada que se llega a un 30 puntos más, la velocidad del juego aumenta hasta un maximo,
                    // para hacerlo más complejo y desafiante principalmente
                    if ((int)puntaje % 30 == 0 && (int)puntaje != 0) {
                        velocidadX = Math.max(velocidadX - 0.5, -10);
                        delayColocacion = Math.max(700, 1500 + (int)velocidadX * 100);
                        colocacionTuberias.setDelay(delayColocacion);
                    }
                    if ((int)puntaje % 50 == 0 && (int)puntaje != 0) {
                        ReproductorSonido.play("logro");
                        if(temaActual == 4){
                            temaActual = 1;
                        }
                        else temaActual ++;
                        switch (temaActual) {
                            case 1:
                                fondoImg = fondoImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();;
                                break;
                            case 2:
                                fondoImg = fondoImg2;
                                break;
                            case 3:
                                fondoImg = fondoImg3;
                                break;
                            case 4:
                                fondoImg = fondoImg4;
                                break;
                        }
                    }
                }



            }

            if(colision(pajaro,tuberia)){
                ReproductorSonido.play("hit");
                gameOver = true;
            }


        }

        if(pajaro.y > alto){
            ReproductorSonido.play("die");
            gameOver = true;

        }
    }

    /**
     * @param p Objeto del pájaro, para detectar la colisión
     * @param t Objeto de tubería, para detectar la colisión con el pájaro
     * @return Regresa un booleano, en caso de colision es true, si no es false
     */
    public boolean colision(Pajaro p, Tuberia t){
        return p.x < t.tuberiaX + t.tuberiaAncho &&
                p.x + p.ancho > t.tuberiaX &&
                p.y < t.tuberiaY +t.tuberiaAlto &&
                p.y + p.alto > t.tuberiaY;

    }

    /**
     * @param e the event to be processed
     * @apiNote méto.do del actionListener para que, con el timer declarado se repitan estos méto.dos
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        movimiento();
        repaint();
        if(gameOver){
            colocacionTuberias.stop();
            loop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @param e the event to be processed
     * @apiNote Méto.do del KeyListener, principalmente para detectar la tecla del espacio
     * y simular tanto el movimiento del pájaro como el reinicio de la partida
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            ReproductorSonido.play("wing");
            velocidadY = -9;
            if(gameOver){
                velocidadX = -4;
                pajaro.y = 640/2;
                velocidadY = 0;
                tuberias.clear();
                puntaje = 0.0;
                gameOver = false;
                loop.start();
                delayColocacion = 1500;
                colocacionTuberias.setDelay(delayColocacion);
                colocacionTuberias.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
