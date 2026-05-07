package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe SegmentoReta, validando construção, invariantes e interseções entre segmentos.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class SegmentoRetaTests {

    @Test
    public void testConstructorWithTwoPoints() {
        SegmentoReta s = new SegmentoReta(new Ponto(1.0, 2.0), new Ponto(4.0, 6.0));
        TestSupport.assertPonto(s.getA(), 1.0, 2.0);
        TestSupport.assertPonto(s.getB(), 4.0, 6.0);
    }

    @Test
    public void testConstructorWithPointAndVector() {
        SegmentoReta s = new SegmentoReta(new Ponto(1.0, 2.0), new Vetor(3.0, 4.0));
        TestSupport.assertPonto(s.getB(), 4.0, 6.0);
    }

    @Test
    public void testIntersectCrossingSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 4.0));
        SegmentoReta b = new SegmentoReta(new Ponto(0.0, 4.0), new Ponto(4.0, 0.0));

        TestSupport.assertPonto(a.intersect(b), 2.0, 2.0);
    }

    @Test
    public void testIntersectParallelSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 0.0));
        SegmentoReta b = new SegmentoReta(new Ponto(0.0, 1.0), new Ponto(4.0, 1.0));

        assertNull(a.intersect(b));
    }

    @Test
    public void testIntersectOverlappingSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 0.0));
        SegmentoReta b = new SegmentoReta(new Ponto(2.0, 0.0), new Ponto(6.0, 0.0));

        TestSupport.assertPonto(a.intersect(b), 2.0, 0.0);
    }

    @Test
    public void testIntersectWithVetor() {
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, -1.0), new Ponto(2.0, 1.0));
        TestSupport.assertPonto(s.intersect(new Vetor(4.0, 0.0)), 2.0, 0.0);
    }

    @Test
    public void testInvalidSegment() {
        assertThrows(IllegalArgumentException.class,
                () -> new SegmentoReta(new Ponto(1.0, 1.0), new Ponto(1.0, 1.0)));
        assertThrows(IllegalArgumentException.class,
                () -> new SegmentoReta(null, new Ponto(1.0, 1.0)));
    }
}
