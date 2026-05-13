package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar os métodos auxiliares de geometria usados pelo Engine.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 12/05/2026
 */
public class GeometriaTests {
    private static final double EPS = TestSupport.EPS;

    @Test
    public void testFinitoEIgualdadeComTolerancia() {
        assertTrue(Geometria.finito(1.5));
        assertFalse(Geometria.finito(Double.NaN));
        assertFalse(Geometria.finito(Double.POSITIVE_INFINITY));

        assertTrue(Geometria.iguais(1.0, 1.0 + 1e-10));
        assertFalse(Geometria.iguais(1.0, 1.1));
    }

    @Test
    public void testIgualdadeDistanciaEntrePontos() {
        Ponto a = new Ponto(0.0, 0.0);
        Ponto b = new Ponto(3.0, 4.0);
        Ponto quaseA = new Ponto(1e-10, -1e-10);

        assertTrue(Geometria.iguais(a, quaseA));
        assertFalse(Geometria.iguais(a, b));
        assertEquals(25.0, Geometria.distancia2(a, b), EPS);
        assertEquals(5.0, Geometria.distancia(a, b), EPS);
    }

    @Test
    public void testPontoPertenceAoSegmento() {
        Ponto a = new Ponto(0.0, 0.0);
        Ponto b = new Ponto(10.0, 0.0);

        assertTrue(Geometria.pontoPertenceAoSegmento(new Ponto(5.0, 0.0), a, b));
        assertTrue(Geometria.pontoPertenceAoSegmento(a, a, b));
        assertTrue(Geometria.pontoPertenceAoSegmento(b, a, b));
        assertFalse(Geometria.pontoPertenceAoSegmento(new Ponto(11.0, 0.0), a, b));
        assertFalse(Geometria.pontoPertenceAoSegmento(new Ponto(5.0, 1.0), a, b));
    }

    @Test
    public void testParametroNoSegmento() {
        Ponto a = new Ponto(0.0, 0.0);
        Ponto b = new Ponto(10.0, 0.0);

        assertEquals(0.0, Geometria.parametroNoSegmento(a, a, b), EPS);
        assertEquals(0.5, Geometria.parametroNoSegmento(new Ponto(5.0, 0.0), a, b), EPS);
        assertEquals(1.0, Geometria.parametroNoSegmento(b, a, b), EPS);
    }

    @Test
    public void testProdutoInterno() {
        Ponto a = new Ponto(0.0, 0.0);
        Ponto b = new Ponto(1.0, 0.0);
        Ponto c = new Ponto(1.0, 1.0);

        assertEquals(0.0, Geometria.produtoInterno(a, b, c), EPS);
    }

    @Test
    public void testInvalidArguments() {
        Ponto a = new Ponto(0.0, 0.0);
        assertThrows(IllegalArgumentException.class, () -> Geometria.distancia(null, a));
        assertThrows(IllegalArgumentException.class, () -> Geometria.distancia2(a, null));
        assertThrows(IllegalArgumentException.class, () -> Geometria.pontoPertenceAoSegmento(null, a, a));
        assertThrows(IllegalArgumentException.class, () -> Geometria.parametroNoSegmento(a, null, a));
    }
}
