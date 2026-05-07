package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Ponto, validando coordenadas, distância, igualdade aproximada e representação textual.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class PontoTests {
    private static final double EPS = TestSupport.EPS;

    @Test
    public void testConstructorAndGetters() {
        Ponto p = new Ponto(2.5, -1.25);
        assertEquals(2.5, p.getX(), EPS);
        assertEquals(-1.25, p.getY(), EPS);
    }

    @Test
    public void testDistAndIgual() {
        Ponto a = new Ponto(0.0, 0.0);
        Ponto b = new Ponto(3.0, 4.0);
        Ponto quaseA = new Ponto(1e-10, -1e-10);

        assertEquals(5.0, a.dist(b), EPS);
        assertTrue(a.igual(quaseA));
        assertFalse(a.igual(b));
        assertFalse(a.igual(null));
    }

    @Test
    public void testToString() {
        assertEquals("(2.0,3.5)", new Ponto(2.0, 3.5).toString());
    }

    @Test
    public void testInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Ponto(Double.NaN, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Ponto(0.0, Double.POSITIVE_INFINITY));
    }
}
