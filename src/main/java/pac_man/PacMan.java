package pac_man;

import reproductor.ReproductorSonido;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.awt.*;

public class PacMan extends JPanel implements ActionListener,KeyListener{
    /*Fácilmente, uno de los juegos más complejos y que más tiempo me tomó, por el problema con los
     * fantasmas y el hecho de que el tutorial venía incompleto, asi que me tocó hacer algunas cosas
     * teniendo en cuenta la lógica del juego*/
    int comidos = 0; //esto lo agregué, ya que en el juego original la puntuación por comer
    // fantasmas se iba multiplicando después de cada fantasma comido
    int siguienteVidaExtra = 10000; // Y esto para cada ciertos puntos agregarle una vida al jugador



    private final int filas = 21;
    private final int columnas = 19;
    private final int tamcasillas = 32;
    private final int ancho = columnas * tamcasillas;

    private final Image imagenmuro = new ImageIcon(getClass().getResource("/wall.png")).getImage();
    private final Image fantOjos = new ImageIcon(getClass().getResource("/ghosts_eyes.png")).getImage();
    private final Image fantAzul = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
    private final Image fantRojo = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();
    private final Image fantNaranja = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
    private final Image fantRosa = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
    private final Image comidaEspImg = new ImageIcon(getClass().getResource("/powerFood.png")).getImage();
    private final Image fantAsustado = new ImageIcon(getClass().getResource("/scaredGhost.png")).getImage();

    private final Image pacmanUpImg = new ImageIcon(getClass().getResource("/pacmanup.png")).getImage();
    private final Image pacmanDownImg = new ImageIcon(getClass().getResource("/pacmandown.png")).getImage();
    private final Image pacmanLeftImg = new ImageIcon(getClass().getResource("/pacmanleft.png")).getImage();
    private final Image pacmanRightImg = new ImageIcon(getClass().getResource("/pacman-closed.png")).getImage();
    private final Image pacmanImg = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();


    public static HashSet<Bloque> muros;
    public HashSet<Bloque> comidas;
    public ArrayList<Fantasma> fantasmas;
    public HashSet<Bloque> comidasEsp;
    public Bloque Pacman;

    public Timer loop;
    char[] direcciones = {'U','D','L','R'};
    Random rand = new Random();
    public int puntaje = 0;
    public int maxpuntaje = 0;
    public int vidas = 3;
    boolean gameover = false;


    long iniciovulnerabilidad = 0;
    final int duracionVulnerabilidad = 8000;
    //Esta es la forma en que se genera el mapa, X son muros S pastillas especiales
    //b, p, o, "r" los fantasmas de sus colores respectivos, P es pacman y los espacios vacíos pastillas
    //las O son espacios vacios en el mapa
    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "XSXX XXX X XXX XXSX",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "XS X     P     X SX",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };


    /**
     * @apiNote Méto.do constructor, se genera la ventana del juego
     * y se inicia la lógica del juego con su Timer y el movimiento de los fantasmas
     */
    public PacMan() {
        int alto = filas * tamcasillas;
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);



        cargarMapa();
        for(Bloque fantasma: fantasmas){
            char nuevodireccion = direcciones[rand.nextInt(4)];
            fantasma.actdireccion(nuevodireccion);
        }

        loop = new Timer(50, this);
        loop.start();
        ReproductorSonido.play("inicio");


    }

    /**
     * Méto.do para cargar el mapa mediante una plantilla hecha con una matriz, se generan los muros
     * las comidas, las comidas especiales, los fantasmas, y pacman
     */
    //metodo para que el mapa cargue, se vuelva a generar las pastillas y
    // lo necesario al iniciar el juego o un nuevo nivel
    public void cargarMapa(){
        muros = new HashSet<>();
        comidas = new HashSet<>();
        fantasmas = new ArrayList<>();
        comidasEsp = new HashSet<>();

        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                String fila = tileMap[i];
                char tileMapchar = fila.charAt(j);

                int x = j * tamcasillas;
                int y = i * tamcasillas;

                if(tileMapchar == 'X'){
                    Bloque muro = new Bloque(imagenmuro,x,y,tamcasillas,tamcasillas);
                    muros.add(muro);
                }
                else if(tileMapchar == 'b'){
                    Fantasma fantasma = new Fantasma(fantAzul,x,y,tamcasillas,tamcasillas);
                    fantasmas.add(fantasma);
                }
                else if(tileMapchar == 'r'){
                    Fantasma fantasma = new Fantasma(fantRojo,x,y,tamcasillas,tamcasillas);
                    fantasmas.add(fantasma);
                } else if (tileMapchar == 'p') {
                    Fantasma fantasma = new Fantasma(fantRosa,x,y,tamcasillas,tamcasillas);
                    fantasmas.add(fantasma);
                }
                else if(tileMapchar == 'o'){
                    Fantasma fantasma = new Fantasma(fantNaranja,x,y,tamcasillas,tamcasillas);
                    fantasmas.add(fantasma);
                } else if (tileMapchar == 'P') {
                    Pacman = new Bloque(pacmanRightImg,x,y,tamcasillas,tamcasillas);
                }
                else if(tileMapchar == ' '){
                    Bloque comida = new Bloque(null,x + 14,y + 14,4,4);
                    comidas.add(comida);
                }
                else if(tileMapchar == 'S'){
                    Bloque comidaespecial= new Bloque(comidaEspImg, x + 8, y + 8,18,18);
                    comidasEsp.add(comidaespecial);
                }
            }
        }
    }



    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Metodo para dibujar las cosas en la ventana
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        dibujar(g);
    }
    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Mét.odo para dibujar todos los objetos del juego, pacman, los fantasmas, los muros,
     * los textos, las vidas, la puntuación y la comida
     */
    public void dibujar(Graphics g){
        g.drawImage(Pacman.img,Pacman.x, Pacman.y, Pacman.ancho, Pacman.alto,null);

        for(Bloque fantasma : fantasmas){
            g.drawImage(fantasma.img,fantasma.x,fantasma.y,fantasma.ancho,fantasma.alto,null);
        }

        for(Bloque muro : muros){
            g.drawImage(muro.img,muro.x,muro.y,muro.ancho,muro.alto,null);
        }

        for(Bloque comidaesp : comidasEsp){
            g.drawImage(comidaesp.img,comidaesp.x,comidaesp.y,comidaesp.ancho,comidaesp.alto,null);
        }

        g.setColor(Color.WHITE);
        for(Bloque comida : comidas){
            g.fillRect(comida.x, comida.y, comida.ancho, comida.alto);
        }

        g.setFont(new Font("Arial",Font.BOLD,18));
        if(gameover){
            if(puntaje > maxpuntaje){
                maxpuntaje = puntaje;
            }
            g.drawString("Perdiste:( Puntaje: " + String.valueOf(puntaje) + "       Record: " + String.valueOf(maxpuntaje),tamcasillas/2,tamcasillas/2);
            g.setColor(Color.RED);
            g.drawString("Presiona ENTER para reintentar",tamcasillas/2,tamcasillas * 2);
        }
        else{
            g.drawImage(pacmanImg,tamcasillas/2,(tamcasillas - Pacman.alto - 3),Pacman.ancho,Pacman.alto,null);
            g.drawString("       x" + String.valueOf(vidas) + " Puntaje: " + String.valueOf(puntaje),tamcasillas/2,tamcasillas/2);
        }

    }

    /**
     * @apiNote En este méto.do se calcula la lógica del juego.
     *    el movimiento de Pac-man, de los fantasmas, las colisiones con los muros y con la comida
     *    tanto la especial como la normal, incluso las muertes tanto de Pac-man, como la de los fantasmas
     */
    public void movimiento(){
        //Esta parte es para la columna en donde pacman puede ir de un lado al opuesto por el borde del mapa
        if (Pacman.x <=0 && Pacman.y == 9 *tamcasillas) {
            Pacman.x = ancho - tamcasillas;
        }else if(Pacman.x + Pacman.ancho >= ancho && Pacman.y == 9 *tamcasillas) {
            Pacman.x = 0;
        }
        Pacman.x += Pacman.velocidadx;
        Pacman.y += Pacman.velocidady;


        for(Bloque muro : muros){
            if(Pacman.colision(Pacman, muro)){
                Pacman.x -= Pacman.velocidadx;
                Pacman.y -= Pacman.velocidady;
                break;
            }
        }

        for(Fantasma fantasma : fantasmas){
            //Esta parte sirve cuando el efecto de las pastillas especiales está activo
            //Detecta si se come un fantasma, les pone el efecto de los ojos, suma puntos
            // y los mueve al centro
            if(Pacman.colision(Pacman,fantasma) && !fantasma.soloOjos){
                if(fantasma.vulnerabilidad){
                    comidos ++;
                    ReproductorSonido.play("comerfant");
                    puntaje += 500 * comidos;
                    if(puntaje >= siguienteVidaExtra && vidas < 5){
                        vidas ++;
                        siguienteVidaExtra += 10000;
                        ReproductorSonido.play("extralife");
                    }
                    fantasma.img = fantOjos;
                    fantasma.soloOjos = true;
                    fantasma.reiniciar();
                    fantasma.velocidadx = 0;
                    fantasma.velocidady = 0;
                }
                else{
                    vidas --;
                    ReproductorSonido.play("death");
                    if(vidas == 0){
                        gameover = true;
                        return;
                    }
                    reiniciarPosiciones();
                }
            }

            if(fantasma.y == tamcasillas * 9 && fantasma.direccion != 'U' && fantasma.direccion != 'D'){
                fantasma.actdireccion('U');
            }
            fantasma.x += fantasma.velocidadx;
            fantasma.y += fantasma.velocidady;
            for(Bloque muro : muros){
                if(fantasma.colision(fantasma,muro)){
                    fantasma.x -= fantasma.velocidadx;
                    fantasma.y -= fantasma.velocidady;
                    char nuevadireccion = direcciones[rand.nextInt(4)];
                    fantasma.actdireccion(nuevadireccion);
                } else if (fantasma.x <=0 && fantasma.y == 9 *tamcasillas) {
                    fantasma.x = ancho - tamcasillas;
                    char nuevadireccion = direcciones[rand.nextInt(4)];
                    fantasma.actdireccion(nuevadireccion);
                }
                else if(fantasma.x + fantasma.ancho >= ancho && fantasma.y == 9 *tamcasillas) {
                    fantasma.x = 0;
                    char nuevadireccion = direcciones[rand.nextInt(4)];
                    fantasma.actdireccion(nuevadireccion);
                }
            }
        }

        Bloque comidacomidaxd = null;
        for(Bloque comida : comidas){
            if(Pacman.colision(Pacman,comida)){
                ReproductorSonido.play("chomp");
                comidacomidaxd = comida;
                puntaje += 10;
                if(puntaje >= siguienteVidaExtra){
                    vidas ++;
                    siguienteVidaExtra += 10000;
                    ReproductorSonido.play("extralife");
                }
            }
        }
        comidas.remove(comidacomidaxd);
        //toda esta parte es para las pastillas especiales, detectar su colision, puntuacion
        //Convertir a los fantasmas en las imagenes azules y activar el tiempo en el que estos son vulnerables
        //
        Bloque comidaespcomida = null;
        for(Bloque comidaesp : comidasEsp){
            if(Pacman.colision(Pacman,comidaesp)){
                ReproductorSonido.play("comidaesp");
                comidaespcomida = comidaesp;
                iniciovulnerabilidad = System.currentTimeMillis();
                puntaje += 50;
                if(puntaje >= siguienteVidaExtra){
                    vidas ++;
                    siguienteVidaExtra += 10000;
                    ReproductorSonido.play("extralife");
                }

                for(Fantasma fantasma : fantasmas){
                    if(!fantasma.soloOjos){
                        fantasma.img = fantAsustado;
                        fantasma.vulnerabilidad = true;
                    }
                }
            }
        }
        comidasEsp.remove(comidaespcomida);
        //Aquí es para detectar el fin del modo de pastillas especiales, para devolver a los fantasmas a su estado inicial
        if(fantasmas.get(0).vulnerabilidad && System.currentTimeMillis() - iniciovulnerabilidad > duracionVulnerabilidad){
            comidos = 0;
            for(Fantasma fantasma : fantasmas){
                fantasma.vulnerabilidad = false;
                fantasma.soloOjos = false;
                fantasma.reiniciarfant();
                char nuevaDir = direcciones[rand.nextInt(4)];
                fantasma.actdireccion(nuevaDir);
            }
        }

        if(comidas.isEmpty() && comidasEsp.isEmpty()){
            if(vidas <= 3){
                ReproductorSonido.play("extralife");
                vidas++;
            }
            cargarMapa();
            reiniciarPosiciones();
            ReproductorSonido.play("inicio");
        }
    }

    /**
     * @param a primer bloque a detectar la colision
     * @param b segundo bloque a detectar la colisión
     * @return regresa true si hay colisión entre los dos objetos y false en caso de que no
     * @apiNote Méto.do para detectar colisiones entre todos los objetos del juego, la comida, los fantamas y con los muros
     */


    /**
     * @apiNote Méto.do para reiniciar las posiciones de los fantasmas y de pacman,
     * esto es en caso de gameOver o de que el jugador haya comido toda la comida
     */
    public void reiniciarPosiciones(){
        Pacman.reiniciar();
        Pacman.velocidadx = 0;
        Pacman.velocidady = 0;

        for(Bloque fantasma : fantasmas){
            fantasma.reiniciar();
            char nuevadireccion = direcciones[rand.nextInt(4)];
            fantasma.actdireccion(nuevadireccion);
        }
    }

    /**
     * @param e the event to be processed
     * @apiNote Méto.do del actionListener para que, con el timer declarado se repitan estos méto. Dos
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
     * @apiNote Méto.do del KeyListener, principalmente para detectar las flechas para el movimiento
     * de pac-man y el reinicio del juego en cso de gameover
     */
    @Override
    public void keyReleased(KeyEvent e) {

        if(gameover){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                //reiniciarPosiciones();
                vidas = 3;
                puntaje = 0;
                siguienteVidaExtra = 10000;
                gameover = false;
                cargarMapa();
                for(Bloque fantasma: fantasmas){
                    char nuevodireccion = direcciones[rand.nextInt(4)];
                    fantasma.actdireccion(nuevodireccion);
                }
                loop.start();
                ReproductorSonido.play("inicio");
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_UP){
            Pacman.actdireccion('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            Pacman.actdireccion('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            Pacman.actdireccion('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            Pacman.actdireccion('R');
        }

        switch (Pacman.direccion) {
            case 'U' -> Pacman.img = pacmanUpImg;
            case 'D' -> Pacman.img = pacmanDownImg;
            case 'L' -> Pacman.img = pacmanLeftImg;
            case 'R' -> Pacman.img = pacmanRightImg;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}


}