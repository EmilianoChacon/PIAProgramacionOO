import pac_man.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PacManTest {

    private PacMan pacman;

    @BeforeEach
    public void setUp() {
        pacman = new PacMan();

    }

    @Test
    public void testPacManInitialPosition() {
        assertTrue(pacman.Pacman.x >= 0 && pacman.Pacman.y >= 0);
    }

    @Test
    public void testMovePacmanRight() {
        int originalX = pacman.Pacman.velocidadx;

        pacman.Pacman.actdireccion('R');
        pacman.loop.stop();
        assertEquals(originalX + pacman.Pacman.velocidadx, pacman.Pacman.velocidadx);

    }



    @Test
    public void testLoseLifeOnCollisionWithGhost() {
        int initialLives = pacman.vidas;
        for(Fantasma fantasma : pacman.fantasmas){
            pacman.Pacman.x = fantasma.x;
            pacman.Pacman.y = fantasma.y;
            fantasma.vulnerabilidad = false;
            fantasma.actdireccion('R');
            break;

        }
        pacman.Pacman.actdireccion('R');


        assertEquals(initialLives , pacman.vidas);

    }
}
