import reproductor.ReproductorSonido;
import snake.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {

    Snake snake;

    @BeforeEach
    public void setup() {
        snake = new Snake(500, 500);
        snake.velocidadx = 1;
        snake.velocidady = 0;
    }

    @Test
    public void testColisionTrue() {
        casilla a = new casilla(3, 4);
        casilla b = new casilla(3, 4);
        assertTrue(snake.colision(a, b));
    }

    @Test
    public void testColisionFalse() {
        casilla a = new casilla(3, 4);
        casilla b = new casilla(1, 2);
        assertFalse(snake.colision(a, b));
    }

    @Test
    public void testColocarComidaDentroDeLimites() {
        for (int i = 0; i < 100; i++) {
            snake.colocarcomida();
            assertTrue(snake.comida.x >= 0 && snake.comida.x < snake.ancho / snake.casillaTam);
            assertTrue(snake.comida.y >= 0 && snake.comida.y < snake.alto / snake.casillaTam);
        }
    }

    @Test
    public void testMovimientoComeComida() {
        ReproductorSonido.loadSound("SnakeEating","./src/main/resources/Sonidos/SnakeEating.wav");
        snake.comida.x = snake.cabeza.x;
        snake.comida.y = snake.cabeza.y;
        snake.velocidadx = 1;
        snake.velocidady = 0;

        snake.movimiento(); // debería comer

        assertEquals(1, snake.cuerpo.size());
        assertFalse(snake.gameover);
    }

    @Test
    public void testMovimientoChocaConPared() {
        snake.cabeza.x = snake.ancho / snake.casillaTam - 1;
        snake.cabeza.y = 5;
        snake.velocidadx = 1;
        snake.velocidady = 0;

        snake.movimiento();

        assertTrue(snake.gameover);
    }

    @Test
    public void testReinicioJuegoConEnter() {
        snake.gameover = true;
        snake.cuerpo.add(new casilla(6, 5));

        KeyEvent enter = new KeyEvent(snake, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        snake.keyPressed(enter);

        assertFalse(snake.gameover);
        assertEquals(0, snake.cuerpo.size());
        assertEquals(5, snake.cabeza.x);
        assertEquals(5, snake.cabeza.y);
    }
}
