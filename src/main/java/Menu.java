/*Emiliano Chacon Alvarez 2052404
* Basicamente segui los tutoriales para los juegos de la pagina: kennyyipcoding.com
* Tiene juegos muy variados pero escogí estos 4 por gusto, y los modifiqué para hacerlos
* más completos, agregando sonidos, imagenes extra, o como en el pacman tuve que crear
* el sistema de las pastillas especiales para hacer azules a los fantasmas y ese tipo de cosas */

import Space.SpaceInvaders;
import flappybird.FlappyBird;
import maxpuntajes.GestorRecords;
import pac_man.PacMan;
import reproductor.ReproductorSonido;
import snake.Snake;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @apiNote Clase Menu para la creación de la ventana del menú principal
 */
public class Menu extends JFrame{
    private static JLabel textoFlappy, textoSnake, textoPacman, textoSpace;
    private static JButton btnFlappy;
    private static JButton btnSnake;
    private static JButton btnPacman;
    private static JButton btnSpace;
    private static Clip fondo;

    static int maxFlappy = 0;
    static int maxSnake = 0;
    static int maxPacman = 0;
    static int maxSpace = 0;
    static GestorRecords gestor;
/*Creo que el Menú fue el que me costó más que los juegos xd ya que no seguí tutoriales
y quería que quedara perfecto tuve uno que otro problema con el texto y las imagenes de los botones
a puro prueba y error lo logré (le puse hasta mi nombre a la imagen)
*/

    /**
     * @apiNote este méto/odo sirve para crear el JPanel del menu y to.do lo que tiene que ver con este,
     * con el fondo de arcade, crear los botones para invocar a los otros metodos y los juegos y agregar las musicas de fondo
     */
    public Menu() {
        setTitle("Menú de Juegos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(740, 740);
        setLocationRelativeTo(null);
        setResizable(false);
        Image backgroundImage = new ImageIcon(getClass().getResource("./FondoArcade1.JPG")).getImage(); // Usa ruta absoluta o en resources

        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondoPanel.setLayout(null); // Layout manual
        setContentPane(fondoPanel);

        gestor = new GestorRecords();

        maxPacman = gestor.getRecord("PacMan");
        maxSnake = gestor.getRecord("Snake");
        maxFlappy = gestor.getRecord("FlappyBird");
        maxSpace = gestor.getRecord("SpaceInvaders");



        btnFlappy = crearBoton("Flappy Bird", "FlappyBirdIcon.png", maxFlappy);
        btnSnake = crearBoton("snake.Snake", "SnakeIcon.png", maxSnake);
        btnPacman = crearBoton("Pac-Man", "PacmanIcon.png", maxPacman);
        btnSpace = crearBoton("Space Invaders", "SpaceInvadersIcon.png", maxSpace);

        int anchoBoton = 150;
        int altoBoton = 150;
        int espacio = 50;

        btnFlappy.setBounds(384, 222, anchoBoton, altoBoton);
        btnSnake.setBounds(47, 226, anchoBoton, altoBoton);
        btnPacman.setBounds(220, 218, anchoBoton, altoBoton);
        btnSpace.setBounds(558, 216, anchoBoton, altoBoton);

        fondoPanel.add(btnFlappy);
        fondoPanel.add(btnSnake);
        fondoPanel.add(btnPacman);
        fondoPanel.add(btnSpace);

        //Unas músicas de fondo sin copyright para que no se sientan tan vacíos los juegos xd
        ReproductorSonido.loadSound("MusicaFondo","./src/main/resources/Sonidos/MusicaFondo.wav");
        ReproductorSonido.loadSound("SnakeFondo","./src/main/resources/Sonidos/SnakeMusica.wav");
        ReproductorSonido.loadSound("SpaceFondo","./src/main/resources/Sonidos/SpaceMusica.wav");
        fondo = ReproductorSonido.clips.get("MusicaFondo");
        //Hacer que la musica de fondo no suene tn alto xd
        FloatControl volumen = (FloatControl) fondo.getControl(FloatControl.Type.MASTER_GAIN);
        volumen.setValue(-5.0f);
        fondo.loop(Clip.LOOP_CONTINUOUSLY);




        // Acciones
        btnFlappy.addActionListener(e -> flappy(this));
        btnSnake.addActionListener(e -> snake(this));
        btnPacman.addActionListener(e -> pacman(this));
        btnSpace.addActionListener(e -> space(this));

        //Efectos de Sonido, para que se carguen solo 1 vez en to.do el código
        //Efectos Pac-man:
        ReproductorSonido.loadSound("comidaesp","./src/main/resources/Sonidos/pacman_intermission.wav");
        ReproductorSonido.loadSound("comerfant","./src/main/resources/Sonidos/pacman_eatghost.wav");
        ReproductorSonido.loadSound("chomp","./src/main/resources/Sonidos/pacman_chomp.wav");
        ReproductorSonido.loadSound("inicio","./src/main/resources/Sonidos/pacman_inicio.wav");
        ReproductorSonido.loadSound("death","./src/main/resources/Sonidos/pacman_death.wav");
        ReproductorSonido.loadSound("extralife","./src/main/resources/Sonidos/pacman_extralife.wav");

        //Efectos Snake:
        ReproductorSonido.loadSound("SnakeEating","./src/main/resources/Sonidos/SnakeEating.wav");
        ReproductorSonido.loadSound("SnakeDeath","./src/main/resources/Sonidos/SnakeDeath.wav");

        //Efectos Flappy Bird:
        ReproductorSonido.loadSound("point","./src/main/resources/Sonidos/sfx_point.wav");
        ReproductorSonido.loadSound("hit","./src/main/resources/Sonidos/sfx_hit.wav");
        ReproductorSonido.loadSound("die","./src/main/resources/Sonidos/sfx_die.wav");
        ReproductorSonido.loadSound("wing","./src/main/resources/Sonidos/sfx_wing.wav");
        ReproductorSonido.loadSound("song","./src/main/resources/Sonidos/SongBackground.wav");
        ReproductorSonido.loadSound("logro","./src/main/resources/Sonidos/flappyAchievement.wav");

        //Efectos Space Invaders:
        ReproductorSonido.loadSound("GetReady","./src/main/resources/Sonidos/GET READY!.wav");
        ReproductorSonido.loadSound("NEWLEVEL","./src/main/resources/Sonidos/NEWLEVEL.wav");
        ReproductorSonido.loadSound("BTLNT","./src/main/resources/Sonidos/BTLNT.wav");
        ReproductorSonido.loadSound("explosion","./src/main/resources/Sonidos/si_explosion.wav");
        ReproductorSonido.loadSound("invaderkilled","./src/main/resources/Sonidos/si_invaderkilled.wav");
        ReproductorSonido.loadSound("shoot","./src/main/resources/Sonidos/si_shoot.wav");
        ReproductorSonido.loadSound("logro","./src/main/resources/Sonidos/flappyAchievement.wav");
        ReproductorSonido.loadSound("Velocidad","./src/main/resources/Sonidos/SI_Velocidad.wav");
        ReproductorSonido.loadSound("DisparoAliens","./src/main/resources/Sonidos/SI_DisparoAliens.wav");
        ReproductorSonido.loadSound("DisparoDoble","./src/main/resources/Sonidos/SI_DisparoDoble.wav");
        ReproductorSonido.loadSound("Escudo","./src/main/resources/Sonidos/SI_Escudo.wav");
        ReproductorSonido.loadSound("PuntosExtra","./src/main/resources/Sonidos/SI_PuntosExtra.wav");
    }

    /**
     * @param nombre Nombre del juego a crear, para saber que hacer
     * @param rutaImg la ruta de la imagen que se mostrará en el boton
     * @param maxPuntaje el puntaje maximo actual, solo para mostrarlo en el botón e irlo actualizando
     *
     * @apiNote Este méto.do sirve principalmente para crear los botones de cada juego, para no repetir el código
     * se le añaden sus fotos, el formato de texto, el tamaño de cada botón y lo necesario
     * @return Returna el botón ya creado y configurado para su uso en el Menu
     */
    private JButton crearBoton(String nombre, String rutaImg, int maxPuntaje) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(rutaImg));
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton boton = new JButton(scaledIcon);
        boton.setLayout(new BorderLayout());

        JLabel texto = new JLabel(formatearTexto(maxPuntaje), SwingConstants.CENTER);
        texto.setForeground(Color.WHITE);
        texto.setOpaque(true);
        texto.setBackground(new Color(0, 0, 0, 180));
        texto.setFont(new Font("Arial", Font.BOLD, 12));


        switch (nombre) {
            case "Flappy Bird" -> textoFlappy = texto;
            case "snake.Snake" -> textoSnake = texto;
            case "Pac-Man" -> textoPacman = texto;
            case "Space Invaders" -> textoSpace = texto;
        }

        boton.add(texto, BorderLayout.SOUTH);
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);

        return boton;
    }

    /**
     * @apiNote Méto.do sencillo para actualizar los textos de los botones, principalmente para actualizar el maxPuntaje y mostrarlo en pantalla
     */
//Estos métodos sirven para cuando se juega un juego y se obtiene un nuevo puntaje máximo
// //se actualice para mostrar en el menú
    public static void actualizarTextoBotones() {
        textoFlappy.setText(formatearTexto(maxFlappy));
        textoSnake.setText(formatearTexto(maxSnake));
        textoPacman.setText(formatearTexto(maxPacman));
        textoSpace.setText(formatearTexto(maxSpace));
    }
    /**
     * @param maxPuntaje Se introduce para actualizar el texto y que se sobreescriba el texto del botón
     */
    public static String formatearTexto(int maxPuntaje) {
        return "<html><div style='text-align:center;'>Récord: " + maxPuntaje + "</div></html>";
    }


    /**
     * @param menu Simplemente para mantener el menu, que no se borre y el menu principal se pause mientras se ejecuta el juego
     * @apiNote Este méto.do crea la ventana del juego, con sus configuraciones necesarias para que se cancele to.do proceso al cerrar la ventana
     * al igual que se ejecute el juego, su música al momento de pulsar el botón respectivo además de actualizar el maxpuntaje en el menu
     */
    public static void flappy(JFrame menu){
        fondo.stop();
        menu.setEnabled(false);
        int ancho = 360;
        int altura = 640;

        JFrame ventana = new JFrame("Flappy Bird");
        //ventana.setVisible(true);
        ventana.setSize(ancho, altura);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        ventana.add(flappyBird);
        ventana.pack();
        flappyBird.requestFocus();
        Clip clip = ReproductorSonido.clips.get("song");
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        ventana.setVisible(true);
        /*Esto tuve que hacerlo en todos los juegos, para detener procesos en segundo plano,
        * como la música, los loops y actualizar el récord del puntaje */
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                clip.stop();
                flappyBird.loop.stop();
                flappyBird.maxPuntaje = Math.max(flappyBird.maxPuntaje,(int)flappyBird.puntaje);
                flappyBird.colocacionTuberias.stop();
                menu.setEnabled(true); // Reactivar el menú cuando se cierra el juego
                menu.toFront();
                maxFlappy = Math.max(flappyBird.maxPuntaje,maxFlappy);
                gestor.actualizarRecord("FlappyBird", maxFlappy);
                actualizarTextoBotones();
                fondo.loop(Clip.LOOP_CONTINUOUSLY);

            }
        });

    }
    /**
     * @param menu Simplemente, para mantener el menu, que no se borre y el menu principal se pause mientras se ejecuta el juego
     * @apiNote Este méto.do crea la ventana del juego, con sus configuraciones necesarias para que se cancele to.do proceso al cerrar la ventana
     * al igual que se ejecute el juego, su música al momento de pulsar el botón respectivo además de actualizar el maxpuntaje en el menu
     * Igual en este tuve que bajar el volumen de la música de fondo
     */
    public static void snake(JFrame menu){
        fondo.stop();
        menu.setEnabled(false);
        int ancho = 600;
        int altura = ancho;
        JFrame ventana = new JFrame("snake.Snake");

        ventana.setSize(ancho, altura);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Clip clip = ReproductorSonido.clips.get("SnakeFondo");
        FloatControl volumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        volumen.setValue(-7.0f);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        Snake snakejuego = new Snake(ancho, altura);
        ventana.add(snakejuego);
        ventana.pack();
        snakejuego.requestFocus();
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                clip.stop();
                snakejuego.loop.stop();
                menu.setEnabled(true);
                menu.toFront();
                maxSnake = Math.max(snakejuego.maxpuntaje,maxSnake);
                gestor.actualizarRecord("Snake", maxSnake);
                actualizarTextoBotones();
                fondo.loop(Clip.LOOP_CONTINUOUSLY);

            }
        });
    }
    /**
     * @param menu Simplemente, para mantener el menu, que no se borre y el menu principal se pause mientras se ejecuta el juego
     * @apiNote Este méto.do crea la ventana del juego, con sus configuraciones necesarias para que se cancele to.do proceso al cerrar la ventana
     * al igual que se ejecute el juego, su música al momento de pulsar el botón respectivo además de actualizar el maxpuntaje en el menu
     */
    public static void pacman(JFrame menu){

        fondo.stop();
        menu.setEnabled(false);
        int filas = 21;
        int columnas = 19;
        int tamcasillas = 32;
        int ancho = columnas * tamcasillas;
        int alto = filas * tamcasillas;

        JFrame ventana = new JFrame("Pac-man");
        ventana.setSize(ancho, alto);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PacMan pacman = new PacMan();
        ventana.add(pacman);
        ventana.pack();
        pacman.requestFocus();
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                pacman.loop.stop();
                menu.setEnabled(true); // Reactiva el menú cuando se cierra el juego
                menu.toFront();
                maxPacman = Math.max(pacman.maxpuntaje,maxPacman);
                gestor.actualizarRecord("PacMan", maxPacman);
                actualizarTextoBotones();
                fondo.loop(Clip.LOOP_CONTINUOUSLY);
            }
        });

    }
    /**
     * @param menu Simplemente, para mantener el menu, que no se borre y el menu principal se pause mientras se ejecuta el juego
     * @apiNote Este méto.do crea la ventana del juego, con sus configuraciones necesarias para que se cancele to.do proceso al cerrar la ventana
     * al igual que se ejecute el juego, su música al momento de pulsar el botón respectivo además de actualizar el maxpuntaje en el menu
     * En este si le baje el volumen a la cancion de fondo porque si estaba muy alta
     */
    public static void space(JFrame menu){
        fondo.stop();
        menu.setEnabled(false);
        int tamcasillas = 32;
        int filas = 16;
        int columnas = 16;
        int ancho = columnas * tamcasillas;
        int alto = filas * tamcasillas;
        JFrame ventana = new JFrame("Space Invaders");
        Clip clip = ReproductorSonido.clips.get("SpaceFondo");
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-2.0f);  // Baja el volumen 10 decibelios (ajusta este valor a tu gusto)
        } else {
            System.out.println("El control de volumen no es soportado");
        }
        ventana.setSize(ancho, alto);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SpaceInvaders space = new SpaceInvaders();
        ventana.add(space);
        ventana.pack();
        space.requestFocus();
        ventana.setVisible(true);
        ventana.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                clip.stop();
                space.loop.stop();
                menu.setEnabled(true);
                menu.toFront();
                maxSpace = Math.max(maxSpace,space.maxpuntaje);
                gestor.actualizarRecord("SpaceInvaders", maxSpace);
                actualizarTextoBotones();
                fondo.loop(Clip.LOOP_CONTINUOUSLY);
            }
        });
    }
}
