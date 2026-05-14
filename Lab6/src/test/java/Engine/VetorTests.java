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
public class VetorTests {
    private static final double EPS = TestSupport.EPS;

    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Vetor v = new Vetor(3.0, -2.0);
        assertEquals(3.0, v.getX(), EPS);
        assertEquals(-2.0, v.getY(), EPS);
    }

    /**
 * Responsabilidade: validar constructor from ponto através de um teste unitário.
 */
    @Test
    public void testConstructorFromPonto() {
        TestSupport.assertVetor(new Vetor(new Ponto(4.0, 5.0)), 4.0, 5.0);
    }

    /**
 * Responsabilidade: validar vector operations através de um teste unitário.
 */
    @Test
    public void testVectorOperations() {
        Vetor a = new Vetor(3.0, 4.0);
        Vetor b = new Vetor(1.0, -2.0);

        assertEquals(5.0, a.modulo(), EPS);
        assertEquals(-5.0, a.produtoInterno(b), EPS);
        TestSupport.assertVetor(a.add(b), 4.0, 2.0);
        TestSupport.assertVetor(a.sub(b), 2.0, 6.0);
        TestSupport.assertVetor(a.mult(2.0), 6.0, 8.0);
    }

    /**
 * Responsabilidade: validar cosine similarity através de um teste unitário.
 */
    @Test
    public void testCosineSimilarity() {
        assertEquals(0.0, new Vetor(1.0, 0.0).cosineSimilarity(new Vetor(0.0, 1.0)), EPS);
    }

    /**
 * Responsabilidade: validar intersect segmento através de um teste unitário.
 */
    @Test
    public void testIntersectSegmento() {
        Vetor v = new Vetor(4.0, 0.0);
        SegmentoReta s = new SegmentoReta(new Ponto(2.0, -1.0), new Ponto(2.0, 1.0));

        TestSupport.assertPonto(v.intersect(s), 2.0, 0.0);
    }

    /**
 * Responsabilidade: validar invalid arguments através de um teste unitário.
 */
    @Test
    public void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Vetor((Ponto) null));
        assertThrows(IllegalArgumentException.class, () -> new Vetor(Double.NaN, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Vetor(0.0, 0.0).cosineSimilarity(new Vetor(1.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Vetor(1.0, 0.0).add(null));
    }
}
