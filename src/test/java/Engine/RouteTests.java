package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Route, validando comprimento, posição ao longo da rota, tempo, velocidade e interseções.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class RouteTests {
    private static final double EPS = TestSupport.EPS;

    @Test
    public void testConstructorAndGetters() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 0.0),
                new Ponto(2.0, 3.0)
        });

        assertEquals(3, r.getNumeroPontos());
        TestSupport.assertPonto(r.getInicio(), 0.0, 0.0);
        TestSupport.assertPonto(r.getFim(), 2.0, 3.0);
        TestSupport.assertPonto(r.getPonto(1), 2.0, 0.0);
    }

    @Test
    public void testGetPontosReturnsCopy() {
        Route r = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) });
        Ponto[] copia = r.getPontos();
        copia[0] = new Ponto(99.0, 99.0);

        TestSupport.assertPonto(r.getInicio(), 0.0, 0.0);
    }

    @Test
    public void testComprimentoExemploLab4() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 1.0),
                new Ponto(1.0, 1.0),
                new Ponto(4.0, 4.0),
                new Ponto(4.0, 3.0)
        });

        assertEquals(6.242640687, r.comprimento(), 1e-6);
    }

    @Test
    public void testTimePositionAndSpeedExampleLab5() {
        Route r = new Route(new Ponto[] {
                new Ponto(5.0, 1.0),
                new Ponto(5.0, 5.0),
                new Ponto(7.0, 5.0)
        });

        assertEquals(3.0, r.time(2.0), EPS);
        TestSupport.assertPonto(r.position(2.0, 2.25), 5.5, 5.0);

        Vetor[] velocidades = r.speed(new Vetor(1.0, 1.0), 2.0);

        assertEquals(2, velocidades.length);
        TestSupport.assertVetor(velocidades[0], -1.0, 1.0);
        TestSupport.assertVetor(velocidades[1], 1.0, -1.0);
    }

    @Test
    public void testPositionBeforeStartAndAfterEnd() {
        Route r = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });

        TestSupport.assertPonto(r.position(2.0, -5.0), 0.0, 0.0);
        TestSupport.assertPonto(r.position(2.0, 100.0), 10.0, 0.0);
    }

    @Test
    public void testIntersectSegmentoExemploLab4() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 1.0),
                new Ponto(1.0, 1.0),
                new Ponto(4.0, 4.0),
                new Ponto(4.0, 3.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, 1.0), new Ponto(2.0, 4.0));

        Ponto[] inter = r.intersect(s);

        assertNotNull(inter);
        assertEquals(1, inter.length);
        TestSupport.assertPonto(inter[0], 2.0, 2.0);
    }

    @Test
    public void testIntersectSegmentoWithoutDuplicates() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 2.0),
                new Ponto(4.0, 0.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, -1.0), new Ponto(2.0, 3.0));

        Ponto[] inter = r.intersect(s);

        assertNotNull(inter);
        assertEquals(1, inter.length);
        TestSupport.assertPonto(inter[0], 2.0, 2.0);
    }

    @Test
    public void testIntersectObstaculoExemploLab5() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 2.0),
                new Ponto(2.0, 6.0)
        });
        Obstaculo o = new Quadrado(new Ponto[] {
                new Ponto(1.0, 0.0),
                new Ponto(1.0, 3.0),
                new Ponto(4.0, 3.0),
                new Ponto(4.0, 0.0)
        });

        Ponto[] inter = r.intersect(o);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, 1.0, 1.0);
        TestSupport.assertContainsPonto(inter, 2.0, 3.0);
    }

    @Test
    public void testIntersectWithoutIntersection() {
        Route r = new Route(new Ponto[] {
                new Ponto(4.0, 4.0),
                new Ponto(6.0, 6.0),
                new Ponto(7.0, 7.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, 2.0), new Ponto(4.0, 2.0));

        assertNull(r.intersect(s));
    }

    @Test
    public void testInvertida() {
        Route r = new Route(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 0.0),
                new Ponto(2.0, 3.0)
        });

        Route inv = r.invertida();

        TestSupport.assertPonto(inv.getInicio(), 2.0, 3.0);
        TestSupport.assertPonto(inv.getFim(), 0.0, 0.0);
        assertEquals(r.comprimento(), inv.comprimento(), EPS);
    }

    @Test
    public void testInvalidRoute() {
        assertThrows(IllegalArgumentException.class, () -> new Route(null));
        assertThrows(IllegalArgumentException.class, () -> new Route(new Ponto[] { new Ponto(0.0, 0.0) }));
        assertThrows(IllegalArgumentException.class,
                () -> new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(0.0, 0.0) }));
        assertThrows(IllegalArgumentException.class,
                () -> new Route(new Ponto[] { new Ponto(0.0, 0.0), null }));
    }
}
