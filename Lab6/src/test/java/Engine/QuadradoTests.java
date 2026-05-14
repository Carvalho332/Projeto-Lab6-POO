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
public class QuadradoTests {
    /**
 * Responsabilidade: validar valid square através de um teste unitário.
 */
    @Test
    public void testValidSquare() {
        Quadrado q = new Quadrado(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(2.0, 2.0), new Ponto(0.0, 2.0)
        });

        assertEquals(4, q.getNumeroVertices());
    }

    /**
 * Responsabilidade: validar intersect segmento através de um teste unitário.
 */
    @Test
    public void testIntersectSegmento() {
        Quadrado q = new Quadrado(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(2.0, 2.0), new Ponto(0.0, 2.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(-1.0, 1.0), new Ponto(3.0, 1.0));

        Ponto[] inter = q.intersect(s);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, 0.0, 1.0);
        TestSupport.assertContainsPonto(inter, 2.0, 1.0);
    }

    /**
 * Responsabilidade: validar invalid square através de um teste unitário.
 */
    @Test
    public void testInvalidSquare() {
        assertThrows(IllegalArgumentException.class, () -> new Quadrado(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(2.0, 1.0), new Ponto(0.0, 1.0)
        }));
    }
}
