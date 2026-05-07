package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Quadrado, validando invariantes geométricos e comportamento herdado de Retangulo/Poligono.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class QuadradoTests {
    @Test
    public void testValidSquare() {
        Quadrado q = new Quadrado(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(2.0, 2.0), new Ponto(0.0, 2.0)
        });

        assertEquals(4, q.getNumeroVertices());
    }

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

    @Test
    public void testInvalidSquare() {
        assertThrows(IllegalArgumentException.class, () -> new Quadrado(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(2.0, 1.0), new Ponto(0.0, 1.0)
        }));
    }
}
