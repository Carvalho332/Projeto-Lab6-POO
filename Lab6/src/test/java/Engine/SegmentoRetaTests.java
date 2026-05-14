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
public class SegmentoRetaTests {

    /**
 * Responsabilidade: validar constructor with two points através de um teste unitário.
 */
    @Test
    public void testConstructorWithTwoPoints() {
        SegmentoReta s = new SegmentoReta(new Ponto(1.0, 2.0), new Ponto(4.0, 6.0));
        TestSupport.assertPonto(s.getA(), 1.0, 2.0);
        TestSupport.assertPonto(s.getB(), 4.0, 6.0);
    }

    /**
 * Responsabilidade: validar constructor with point and vector através de um teste unitário.
 */
    @Test
    public void testConstructorWithPointAndVector() {
        SegmentoReta s = new SegmentoReta(new Ponto(1.0, 2.0), new Vetor(3.0, 4.0));
        TestSupport.assertPonto(s.getB(), 4.0, 6.0);
    }

    /**
 * Responsabilidade: validar intersect crossing segments através de um teste unitário.
 */
    @Test
    public void testIntersectCrossingSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 4.0));
        SegmentoReta b = new SegmentoReta(new Ponto(0.0, 4.0), new Ponto(4.0, 0.0));

        TestSupport.assertPonto(a.intersect(b), 2.0, 2.0);
    }

    /**
 * Responsabilidade: validar intersect parallel segments através de um teste unitário.
 */
    @Test
    public void testIntersectParallelSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 0.0));
        SegmentoReta b = new SegmentoReta(new Ponto(0.0, 1.0), new Ponto(4.0, 1.0));

        assertNull(a.intersect(b));
    }

    /**
 * Responsabilidade: validar intersect overlapping segments através de um teste unitário.
 */
    @Test
    public void testIntersectOverlappingSegments() {
        SegmentoReta a = new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(4.0, 0.0));
        SegmentoReta b = new SegmentoReta(new Ponto(2.0, 0.0), new Ponto(6.0, 0.0));

        TestSupport.assertPonto(a.intersect(b), 2.0, 0.0);
    }

    /**
 * Responsabilidade: validar intersect with vetor através de um teste unitário.
 */
    @Test
    public void testIntersectWithVetor() {
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, -1.0), new Ponto(2.0, 1.0));
        TestSupport.assertPonto(s.intersect(new Vetor(4.0, 0.0)), 2.0, 0.0);
    }

    /**
 * Responsabilidade: validar invalid segment através de um teste unitário.
 */
    @Test
    public void testInvalidSegment() {
        assertThrows(IllegalArgumentException.class,
                () -> new SegmentoReta(new Ponto(1.0, 1.0), new Ponto(1.0, 1.0)));
        assertThrows(IllegalArgumentException.class,
                () -> new SegmentoReta(null, new Ponto(1.0, 1.0)));
    }
}
