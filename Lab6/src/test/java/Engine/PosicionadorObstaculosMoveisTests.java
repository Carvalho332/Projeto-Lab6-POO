package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: suportar uma responsabilidade específica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class PosicionadorObstaculosMoveisTests {

    /**
 * Responsabilidade: validar posicionar creates obstacles over routes através de um teste unitário.
 */
    @Test
    public void testPosicionarCreatesObstaclesOverRoutes() {
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });
        PosicionadorObstaculosMoveis p = new PosicionadorObstaculosMoveis(new Random(1));

        List<ObstaculoMovel> obstaculos = p.posicionar(List.of(rota), 3);

        assertEquals(3, obstaculos.size());
        for (ObstaculoMovel o : obstaculos) {
            assertEquals(1.0, o.getRaio(), TestSupport.EPS);
            assertEquals(0.0, o.getCentro().getY(), TestSupport.EPS);
            assertTrue(o.getCentro().getX() > 0.0);
            assertTrue(o.getCentro().getX() < 10.0);
            assertNotNull(rota.intersect(o));
        }
    }

    /**
 * Responsabilidade: validar zero obstacles através de um teste unitário.
 */
    @Test
    public void testZeroObstacles() {
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });
        PosicionadorObstaculosMoveis p = new PosicionadorObstaculosMoveis(new Random(1));

        assertTrue(p.posicionar(List.of(rota), 0).isEmpty());
    }

    /**
 * Responsabilidade: validar invalid arguments através de um teste unitário.
 */
    @Test
    public void testInvalidArguments() {
        PosicionadorObstaculosMoveis p = new PosicionadorObstaculosMoveis(new Random(1));

        assertThrows(IllegalArgumentException.class, () -> new PosicionadorObstaculosMoveis(null));
        assertThrows(IllegalArgumentException.class, () -> p.posicionar(null, 1));
        assertThrows(IllegalArgumentException.class, () -> p.posicionar(List.of(), 1));
        assertThrows(IllegalArgumentException.class,
                () -> p.posicionar(List.of(new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) })), -1));
    }
}
