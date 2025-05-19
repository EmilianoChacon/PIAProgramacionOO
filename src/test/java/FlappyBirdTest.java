import flappybird.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Image;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlappyBirdTest {

    private FlappyBird juego;

    @BeforeEach
    void setUp() {
        juego = new FlappyBird() {
            {
                // Evitamos que inicien los timers reales para no interferir
                loop.stop();
                colocacionTuberias.stop();
                // Imagenes nulas para evitar carga en pruebas
                this.pajaro = new Pajaro((Image) null);
            }
        };
    }

    @Test
    void testColision() {
        Pajaro pajaro = new Pajaro(null);
        pajaro.x = 50;
        pajaro.y = 50;

        Tuberia tuberia = new Tuberia(null);
        tuberia.tuberiaX = 40;
        tuberia.tuberiaY = 40;

        assertTrue(juego.colision(pajaro, tuberia), "Debe detectar colisión");
    }

    @Test
    void testColisionFalse() {
        Pajaro pajaro = new Pajaro(null);
        pajaro.x = 0;
        pajaro.y = 0;

        Tuberia tuberia = new Tuberia(null);
        tuberia.tuberiaX = 200;
        tuberia.tuberiaY = 200;

        assertFalse(juego.colision(pajaro, tuberia), "No debe detectar colisión");
    }

    @Test
    void testColocarTuberias() {
        int cantidadInicial = juego.tuberias.size();
        juego.colocarTuberias();
        assertEquals(cantidadInicial + 2, juego.tuberias.size(), "Debe agregar dos tuberías");
    }

    @Test
    void testMovimientoIncrementaVelocidadY() {
        int velocidadInicial = juego.velocidadY;
        juego.movimiento();
        assertTrue(juego.velocidadY > velocidadInicial, "La velocidadY debe aumentar por gravedad");
    }

    @Test
    void testReinicioJuegoConEspacio() {
        juego.gameOver = true;
        juego.puntaje = 50;
        juego.velocidadY = 5;
        juego.tuberias.add(new Tuberia(null));
        KeyEventMock event = new KeyEventMock(java.awt.event.KeyEvent.VK_SPACE);
        juego.keyPressed(event);

        assertFalse(juego.gameOver, "El juego debe reiniciarse");
        assertEquals(0.0, juego.puntaje, "Puntaje debe reiniciarse a cero");
        assertEquals(0, juego.velocidadY, "VelocidadY debe reiniciarse");
        assertTrue(juego.tuberias.isEmpty(), "Las tuberías deben eliminarse al reiniciar");
    }

    @Test
    void testPajaroCaeFueraPantallaMarcaGameOver() {
        juego.pajaro.y = juego.alto + 10;
        juego.movimiento();
        assertTrue(juego.gameOver, "Debe ser game over si el pájaro cae fuera de pantalla");
    }

    // Clase mock para simular eventos KeyEvent
    static class KeyEventMock extends java.awt.event.KeyEvent {
        public KeyEventMock(int keyCode) {
            super(new java.awt.Canvas(), java.awt.event.KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, ' ');
        }
    }
}
