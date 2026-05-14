package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: suportar uma responsabilidade específica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class RouteTests {
    private static final double EPS = TestSupport.EPS;

    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar get pontos returns copy através de um teste unitário.
 */
    @Test
    public void testGetPontosReturnsCopy() {
        Route r = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) });
        Ponto[] copia = r.getPontos();
        copia[0] = new Ponto(99.0, 99.0);

        TestSupport.assertPonto(r.getInicio(), 0.0, 0.0);
    }

    /**
 * Responsabilidade: validar comprimento exemplo lab4 através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar time position and speed example lab5 através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar position before start and after end através de um teste unitário.
 */
    @Test
    public void testPositionBeforeStartAndAfterEnd() {
        Route r = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });

        TestSupport.assertPonto(r.position(2.0, -5.0), 0.0, 0.0);
        TestSupport.assertPonto(r.position(2.0, 100.0), 10.0, 0.0);
    }

    /**
 * Responsabilidade: validar intersect segmento exemplo lab4 através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar intersect segmento without duplicates através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar intersect obstaculo exemplo lab5 através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar intersect without intersection através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar invertida através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar invalid route através de um teste unitário.
 */
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
