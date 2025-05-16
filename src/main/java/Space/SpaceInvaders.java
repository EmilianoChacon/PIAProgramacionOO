package Space;

import reproductor.ReproductorSonido;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

/**
 * @apiNote Clase con toda la lógica del juego a su nombre, sus méto. dos son la lógica del juego ensí
 */
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
//En general fue un juego sencillo, solo que a la hora de
//agregar sonidos e imagenes extras si hubo algunos problemas, pero nada del otro mundo
//Se me olvidó decir que en cada juego agregue el atributo de maxPuntaje para mostrarlos en el menú

    int tamcasillas = 32;
    int filas = 16;
    int columnas = 16;
    int ancho = columnas * tamcasillas;
    int alto = filas * tamcasillas;

    private final Image naveImagen = new ImageIcon(getClass().getResource("/ship.png")).getImage();
    private final Image alienImagen = new ImageIcon(getClass().getResource("/alien.png")).getImage();
    private final Image alienCyanImagen = new ImageIcon(getClass().getResource("/alien-cyan.png")).getImage();
    private final Image alienMagentaImagen = new ImageIcon(getClass().getResource("/alien-magenta.png")).getImage();
    private final Image alienAmarilloImagen = new ImageIcon(getClass().getResource("/alien-yellow.png")).getImage();
    private final Image alienexplosion = new ImageIcon(getClass().getResource("/alienexplosion.png")).getImage();

    ArrayList<Image> alienImagenes;

    int naveAncho = tamcasillas * 2;
    int naveAlto = tamcasillas;
    int navex = tamcasillas * columnas / 2 - tamcasillas;
    int navey = alto - tamcasillas * 2;
    int naveVelocidadx = tamcasillas;
    Bloque nave;

    ArrayList<Bloque> alienArray;
    int alienAncho = tamcasillas * 2;
    int alienAlto = tamcasillas;
    int alienX = tamcasillas ;
    int alienY = tamcasillas ;

    int aliensFilas = 2;
    int aliensColumnas = 3;
    int aliensCuenta = 0;
    int alienVelocidadx = 1;

    ArrayList<Bloque> balasArray;
    int balaAncho = tamcasillas/8;
    int balaAlto = tamcasillas/2;
    int balaVelocidadx = -10;
    ArrayList<Bloque> balasAliensArray;
    int balaAliensVelocidadx = -6;

    ArrayList<PowerUp> powerUpsArray = new ArrayList<>();
    int powerUpAncho = tamcasillas;
    int powerUpAlto = tamcasillas;
    int powerUpVelocidad = 2;
    boolean escudoActivo = false;
    int escudoRadio;
    boolean disparoDobleActivo = false;



    public Timer loop;

    int puntaje;
    public int maxpuntaje;
    boolean gameOver = false;
    boolean esperandoNuevaOleada = false;

    /**
     * @apiNote Constructor de la clase, crea la ventana, agrega los sonidos y las imágenes necesarias
     * inicializa algunos atributos necesarios además de iniciar el timer para los frames del juego y
     * la creación de Aliens
     */
    public SpaceInvaders(){
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);



        alienImagenes = new ArrayList<>();
        alienImagenes.add(alienImagen);
        alienImagenes.add(alienCyanImagen);
        alienImagenes.add(alienMagentaImagen);
        alienImagenes.add(alienAmarilloImagen);

        nave = new Bloque(navex, navey, naveAncho, naveAlto, naveImagen);
        escudoRadio = nave.ancho;

        alienArray = new ArrayList<>();
        balasArray = new ArrayList<>();
        balasAliensArray = new ArrayList<>();


        crearAliens();
        loop = new Timer(1000/60,this );
        ReproductorSonido.play("GetReady");
        loop.start();
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Méto.do para dibujar los componentes de la ventana, principalmente que se muestren
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        dibujar(g);
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     * @apiNote Méto.do para dibujar cada componente, la nave, los aliens, las balas tanto de los aliens
     * como de la nave, los poweUps, el escudo que se genera en el juego y los textos de puntuaciones
     */
    public void dibujar(Graphics g){
        g.drawImage(nave.imagen,nave.x,nave.y,nave.ancho,nave.alto,null);
        if (escudoActivo) {
            g.setColor(new Color(0, 255, 255, 128)); // color celeste semitransparente
            g.drawOval(nave.x + nave.ancho/2 - escudoRadio, nave.y + nave.alto/2 - escudoRadio, escudoRadio*2, escudoRadio*2);
        }


        for (Bloque alien : alienArray) {
            if (alien.vivo) {
                g.drawImage(alien.imagen, alien.x, alien.y, alien.ancho, alien.alto, null);
            }
        }

        g.setColor(Color.WHITE);
        for (Bloque bal : balasArray) {
            if (!bal.usado) {
                g.fillRect(bal.x, bal.y, bal.ancho, bal.alto);
            }
        }
        g.setColor(Color.RED);
        for(Bloque bal : balasAliensArray){
            g.fillRect(bal.x, bal.y, bal.ancho, bal.alto);
        }

        for (PowerUp powerUp : powerUpsArray) {
            Graphics2D g2 = (Graphics2D) g;

            // PowerUps
            AffineTransform original = g2.getTransform();

            double centerX = powerUp.x + (double) powerUp.ancho / 2;
            double centerY = powerUp.y + (double) powerUp.alto / 2;
            double angle = powerUp.anguloRotacion;

            g2.rotate(angle, centerX, centerY); // rotar alrededor del centro de la estrella


            int radiusOuter = powerUp.ancho / 2;
            int radiusInner = radiusOuter / 2;

            int[] xPoints = new int[10];
            int[] yPoints = new int[10];

            for (int j = 0; j < 10; j++) {
                double a = Math.PI / 5 * j;
                int radius = (j % 2 == 0) ? radiusOuter : radiusInner;
                xPoints[j] = (int) (centerX + radius * Math.sin(a));
                yPoints[j] = (int) (centerY - radius * Math.cos(a));
            }

            g2.setColor(Color.YELLOW);
            g2.fillPolygon(xPoints, yPoints, 10);
            g2.setColor(Color.ORANGE);
            g2.drawPolygon(xPoints, yPoints, 10);

            g2.setTransform(original);
        }


        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Perdiste:(    Puntaje: " + String.valueOf(puntaje), 10, 35);
            if(puntaje > maxpuntaje){
                maxpuntaje = puntaje;
            }
            g.drawString("Récord: " + String.valueOf(maxpuntaje), 10, 75);
            g.drawString("Pulsa ENTER para reintentar",10,105);
        }
        else{
            g.drawString("Puntaje: " + String.valueOf(puntaje), 10, 35);
        }
    }

    /**
     * Méto.do de toda la lógica del juego, esto controla el movimiento de la nave, los aliens,
     * las balas tanto del jugador como de las naves, el movimiento de los powerUps y los efectos en el jugador
     */
//En este metodo se calcula básicamente los movimientos de cada cosa, de la nave del jugador,
// de las balas y de los fantasmas
    public void movimiento(){
        ArrayList<Bloque> aliensQuePuedenDisparar = new ArrayList<>();
        for (int col = 0; col < aliensColumnas; col++) {
            Bloque alienMasBajo = null;
            for (Bloque alien : alienArray) {
                // Verificar si pertenece a esta columna
                if (alien.vivo && Math.abs(alien.x - (alienX + col * alienAncho)) < alienAncho / 2) {
                    if (alienMasBajo == null || alien.y > alienMasBajo.y) {
                        alienMasBajo = alien;
                    }
                }
            }
            if (alienMasBajo != null) {
                aliensQuePuedenDisparar.add(alienMasBajo);
            }
        }

        Random rand = new Random();
        for (Bloque alien : aliensQuePuedenDisparar) {
            if (alien.vivo && rand.nextDouble() < 0.003) { // probabilidad de disparo
                ReproductorSonido.play("DisparoAliens");
                Bloque balaAlien = new Bloque(alien.x + alien.ancho / 2, alien.y + alien.alto, balaAncho, balaAlto, null);
                balasAliensArray.add(balaAlien);
            }
        }


        for(int i = 0; i < alienArray.size(); i++){
            Bloque alien = alienArray.get(i);
            if(alien.vivo){
                alien.x += alienVelocidadx;

                if(alien.x + alienAncho >= ancho || alien.x <= 0){
                    alienVelocidadx *= -1;
                    alien.x += alienVelocidadx * 2;

                    for (Bloque bloque : alienArray) {
                        bloque.y += alienAlto;
                    }
                }
                if(alien.y >= nave.y){
                    ReproductorSonido.play("explosion");
                    ReproductorSonido.play("BTLNT");
                    gameOver = true;
                }
            }
        }

        for(int i = 0; i < balasArray.size(); i++){
            Bloque bal = balasArray.get(i);
            bal.y += balaVelocidadx;

            for(int j = 0; j < alienArray.size(); j++){
                Bloque alien = alienArray.get(j);
                if (!bal.usado && alien.vivo && colision(bal, alien)) {
                    bal.usado = true;
/*Esto podria ser lo más complejo que tuve que hacer en este juego, básicamente
* porque quería que se mostrara un efecto cuando murieran los aliens, me dio problemas
* principalmente por las nuevas oleadas, asi que tuve que retrasar tanto la muerte de los aliens
* como la detección para las nuevas oleadas de aliens, algo complejo que tuve que pensar un rato
*  */
                    alien.imagen = alienexplosion;
                    alien.usado = true;

                    final Timer muerteTimer = getMuerteTimer(alien);
                    muerteTimer.start();

                }

            }

        }

        while(!balasArray.isEmpty() && (balasArray.get(0).usado || balasArray.get(0).y < 0)){
            balasArray.remove(0);
        }
//Este es un for para que los aliens disparen, para agregar más dificultad al juego
        for(int i = 0; i < balasAliensArray.size(); i++) {
            Bloque bal = balasAliensArray.get(i);
            bal.y -= balaAliensVelocidadx;

            if(colision(bal, nave)){
                    if (!escudoActivo) {
                        ReproductorSonido.play("explosion");
                        ReproductorSonido.play("BTLNT");
                        gameOver = true;
                    }
                    balasAliensArray.remove(i);
            }
        }
        balasAliensArray.removeIf(b -> b.y > alto);



       /* Este era el codigo original del tutorial, lo dejo por si no funciona lo que intento hacer xd
       if(aliensCuenta == 0){
            puntaje += aliensColumnas * aliensFilas * 100;
            aliensColumnas = Math.min(aliensColumnas + 1, columnas/2 - 2);
            aliensFilas = Math.min(aliensFilas + 1, filas - 6);
            alienArray.clear();
            balasArray.clear();
            alienVelocidadx = 1;
            reproductor.ReproductorSonido.play("NEWLEVEL");
            crearAliens();
        }*/

        for (int i = 0; i < powerUpsArray.size(); i++) {
            PowerUp powerUp = powerUpsArray.get(i);
            powerUp.y += powerUpVelocidad;

            // Si toca la nave
            if (colision(powerUp, nave)) {
                activarPowerUp();
                powerUpsArray.remove(i);
                i--;
            }
            powerUp.anguloRotacion += 0.05;
            if (powerUp.anguloRotacion >= 2 * Math.PI) {
                powerUp.anguloRotacion -= 2 * Math.PI;
            }

            if (powerUp.y > alto) {
                powerUpsArray.remove(i);
                i--;
            }
        }
    }

    /**
     * @param alien Alien analizado para la muerte y crear el timer
     * @return regresa el timer en cuestión a la lógica del juego
     * @apiNote Este méto, do me sugirió intellij que lo hiciera, pero básicamente es el timer para
     * la muerte de los aliens, mostrar la imagen de explosión, los sonidos, generar los powerUps
     */
    private Timer getMuerteTimer(Bloque alien) {
        Timer muerteTimer = new Timer(75, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alien.vivo = false;
                puntaje += 100;
                aliensCuenta--;

                Random rand = new Random();
                if (rand.nextDouble() < 0.04) {
                    ReproductorSonido.play("logro");
                    PowerUp powerUp = new PowerUp(alien.x + alien.ancho/2 - powerUpAncho/2, alien.y, powerUpAncho, powerUpAlto, null);
                    powerUpsArray.add(powerUp);
                }
                verificarFinOleada();
            }
        });
        muerteTimer.setRepeats(false);
        return muerteTimer;
    }

    /**
     * @apiNote Este méto.do tiene el fin de retrasar la creación de nuevas oleadas de Aliens,
     * con el fin de poder mostrar la "explosión" después de cada muerte
     */
    private void verificarFinOleada() {
        if (!esperandoNuevaOleada && alienArray.stream().noneMatch(a -> a.vivo)) {
            esperandoNuevaOleada = true;

            Timer crearOleada = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    puntaje += aliensColumnas * aliensFilas * 100;
                    aliensColumnas = Math.min(aliensColumnas + 1, columnas / 2 - 2);
                    aliensFilas = Math.min(aliensFilas + 1, filas - 6);
                    alienArray.clear();
                    balasArray.clear();
                    alienVelocidadx = 1;
                    ReproductorSonido.play("NEWLEVEL");
                    crearAliens();
                    esperandoNuevaOleada = false;
                }
            });
            crearOleada.setRepeats(false);
            crearOleada.start();
        }
    }



    /**
     * Méto.do para activar los efectos de los powerUps al recogerlos, en los que son por tiempo se crea un
     * timer para no modificar directamente los atributos del juego si no solo de manera temporal
     */
    //Este metodo es sencillo, usa un rand para escoger uno de los 4 poderes, y acciona su efecto
        public void activarPowerUp() {
            Random rand = new Random();
            int tipo = rand.nextInt(4);
            switch (tipo) {
                case 0 -> {
                    ReproductorSonido.play("DisparoDoble");
                    activarDisparoDoble(8000);
                }
                case 1 -> {
                    ReproductorSonido.play("Velocidad");
                    naveVelocidadx += tamcasillas / 2;
                    Timer timer = new Timer(10000, e -> naveVelocidadx -= tamcasillas / 2);
                    timer.setRepeats(false);
                    timer.start();
                }
                case 2 -> {

                    ReproductorSonido.play("PuntosExtra");
                    puntaje += 10000;
                }
                case 3 -> {
                    ReproductorSonido.play("Escudo");
                    activarEscudo(7000);
                }
            }
        }

    /**
     * @param duracion tiempo que durará el efecto del escudo
     * @apiNote Méto.do para activar el efecto del escudo por un cierto tiempo, para modificar la
     * lógica del juego en que las balas no te afecten, y se genere un escudo alrededor de la nave
     */
    public void activarEscudo(int duracion) {
        escudoActivo = true;
        Timer timer = new Timer(duracion, e -> escudoActivo = false);
        timer.setRepeats(false);
        timer.start();
    }


    /**
     * @param duracion tiempo que durará el efecto del disparo Doble
     * @apiNote Este méto.do sirve para activar el efecto del doble disparo por cierto tiempo, el timer es
     * para desactivar el efecto, y sirve para que se modifique y se dibujen los disparos
     */
    public void activarDisparoDoble(int duracion) {
        disparoDobleActivo = true;
        Timer timer = new Timer(duracion, e -> disparoDobleActivo = false);
        timer.setRepeats(false);
        timer.start();
    }


    /**
     * @apiNote Méto.do sencillo para generar los aliens de manera aleatoria cada vez, para que siempre
     * tengan un color diferente
     */
    //Esto está interesante ya que con un Random se pueden, escoger los colores que existen de los aliens
    public void crearAliens() {
        Random rand = new Random();
        for (int i = 0; i < aliensFilas; i++) {
            for (int j = 0; j < aliensColumnas; j++) {
                int randomImg = rand.nextInt(alienImagenes.size());
                Bloque alien = new Bloque(
                        alienX + j * alienAncho,
                        alienY + i * alienAlto,
                        alienAncho,
                        alienAlto,
                        alienImagenes.get(randomImg));
                alienArray.add(alien);
            }
        }
        aliensCuenta = alienArray.size();
    }

    /**
     * @param a primer bloque a detectar la colision
     * @param b segundo bloque a detectar la colisión
     * @return regresa true si hay colisión entre los dos objetos y false en caso de que no
     * @apiNote Méto.do para detectar colisiones entre las balas con aliens y el jugador y los aliens con el jugador
     */
//Metodo sencillo para la colisión de los aliens con las balas y con el jugador
    public boolean colision(Bloque a, Bloque b){
        return  a.x < b.x + b.ancho &&
                a.x + a.ancho > b.x &&
                a.y < b.y + b.alto &&
                a.y + a.alto > b.y;
    }


    /**
     * @param e the event to be processed
     * @apiNote méto.do del actionListener para que, con el timer declarado se repitan estos méto. dos
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        movimiento();
        repaint();
        if(gameOver){
            loop.stop();
        }
    }

    /**
     * @param e the event to be processed
     * @apiNote Méto.do del KeyListener, para controlar el movimiento de la nave con las flechas y el disparo
     * de la nave y el reinicio del juego en caso de gameOver
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(gameOver){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                nave.x = navex;
                alienArray.clear();
                balasArray.clear();
                balasAliensArray.clear();
                powerUpsArray.clear();
                disparoDobleActivo = false;
                puntaje = 0;
                alienVelocidadx = 1;
                aliensColumnas = 3;
                aliensFilas = 2;
                gameOver = false;
                crearAliens();
                ReproductorSonido.play("GetReady");
                loop.start();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && nave.x - naveVelocidadx >= 0){
            nave.x -= naveVelocidadx;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && nave.x + naveVelocidadx + nave.ancho <= ancho){
            nave.x += naveVelocidadx;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (disparoDobleActivo) {
                balasArray.add(new Bloque(nave.x + nave.ancho/4, nave.y, balaAncho, balaAlto, null));
                balasArray.add(new Bloque(nave.x + nave.ancho*3/4, nave.y, balaAncho, balaAlto, null));
            } else {
                balasArray.add(new Bloque(nave.x + nave.ancho*15/32, nave.y, balaAncho, balaAlto, null));
            }
            ReproductorSonido.play("shoot");
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

}


