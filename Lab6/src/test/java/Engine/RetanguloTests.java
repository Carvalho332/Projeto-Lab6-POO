package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Retangulo, validando invariantes geométricos e comportamento herdado de Poligono.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class RetanguloTests {
    @Test
    public void testValidRectangle() {
        Retangulo r = new Retangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(4.0, 0.0), new Ponto(4.0, 2.0), new Ponto(0.0, 2.0)
        });

        assertEquals(4, r.getNumeroVertices());
    }

    @Test
    public void testIntersectSegmento() {
        Retangulo r = new Retangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(4.0, 0.0), new Ponto(4.0, 2.0), new Ponto(0.0, 2.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(-1.0, 1.0), new Ponto(5.0, 1.0));

        Ponto[] inter = r.intersect(s);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, 0.0, 1.0);
        TestSupport.assertContainsPonto(inter, 4.0, 1.0);
    }

    @Test
    public void testInvalidRectangle() {
        assertThrows(IllegalArgumentException.class, () -> new Retangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(1.0, 0.0), new Ponto(1.0, 1.0)
        }));
        assertThrows(IllegalArgumentException.class, () -> new Retangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(2.0, 0.0), new Ponto(3.0, 1.0), new Ponto(1.0, 1.0)
        }));
    }
}
