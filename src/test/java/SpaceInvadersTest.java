import Space.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceInvadersTest {
    private SpaceInvaders game;

    @BeforeEach
    public void inicio() {
        game = new SpaceInvaders();
        game.loop.stop();
    }

    @Test
    public void testColisionTrue() {
        Bloque a = new Bloque(10, 10, 20, 20, null);
        Bloque b = new Bloque(15, 15, 10, 10, null);
        assertTrue(game.colision(a, b), "Debería ser True");
    }

    @Test
    public void testColisionFalse() {
        Bloque a = new Bloque(0, 0, 10, 10, null);
        Bloque b = new Bloque(20, 20, 5, 5, null);
        assertFalse(game.colision(a, b), "Debería ser False al no haber colision");
    }

    @Test
    public void testCrearAliensSetsCorrectCount() {
        game.alienArray.clear();
        game.aliensFilas = 3;
        game.aliensColumnas = 4;

        game.crearAliens();

        int expected = game.aliensFilas * game.aliensColumnas;
        assertEquals(expected, game.alienArray.size(), "El arreglo debería tener los aliens exactos ");
        assertEquals(expected, game.aliensCuenta, "aliensCuenta debe ser igual a la cantidad de aliens creados");

        for (Bloque alien : game.alienArray) {
            assertTrue(alien.vivo, "Los aliens deben estar vivos en un inicio");
        }
    }

    @Test
    public void testTimerMuerteYPuntaje() throws Exception {
        game.alienArray.clear();
        Bloque alien = new Bloque(0, 0, 10, 10, null);
        alien.vivo = true;
        game.alienArray.add(alien);
        game.aliensCuenta = 1;
        game.puntaje = 0;

        Method method = SpaceInvaders.class.getDeclaredMethod("getMuerteTimer", Bloque.class);
        method.setAccessible(true);
        Timer muerteTimer = (Timer) method.invoke(game, alien);

        for (ActionListener listener : muerteTimer.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }

        assertFalse(alien.vivo, "Alien debe estar muerto después de eso");
        assertEquals(100, game.puntaje, "El puntaje debe elevarse al matar al alien");
        assertEquals(0, game.aliensCuenta, "aliensCuenta debe disminuir");
    }

    @Test
    public void testActivarEscudo() {
        game.escudoActivo = false;
        game.activarEscudo(1000);
        assertTrue(game.escudoActivo, "El escudo debe estar activado al instante");
    }

    @Test
    public void testActivarDisparoDoble() {
        game.disparoDobleActivo = false;
        game.activarDisparoDoble(1000);
        assertTrue(game.disparoDobleActivo, "El disparo doble debe activarse al instante");
    }
}
