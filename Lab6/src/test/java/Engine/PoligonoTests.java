package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Poligono, validando vértices, invariantes e interseções com segmentos de reta.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class PoligonoTests {
    private Poligono quadradoComoPoligono() {
        return new Poligono(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 0.0),
                new Ponto(2.0, 2.0),
                new Ponto(0.0, 2.0)
        });
    }

    @Test
    public void testConstructorAndGetters() {
        Poligono p = quadradoComoPoligono();

        assertEquals(4, p.getNumeroVertices());
        TestSupport.assertPonto(p.getVertice(0), 0.0, 0.0);
        TestSupport.assertPonto(p.getVertice(2), 2.0, 2.0);
    }

    @Test
    public void testGetVerticesReturnsCopy() {
        Poligono p = quadradoComoPoligono();
        Ponto[] copia = p.getVertices();
        copia[0] = new Ponto(99.0, 99.0);

        TestSupport.assertPonto(p.getVertice(0), 0.0, 0.0);
    }

    @Test
    public void testIntersectSegmento() {
        Poligono p = quadradoComoPoligono();
        SegmentoReta s = new SegmentoReta(new Ponto(-1.0, 1.0), new Ponto(3.0, 1.0));

        Ponto[] inter = p.intersect(s);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, 0.0, 1.0);
        TestSupport.assertContainsPonto(inter, 2.0, 1.0);
    }

    @Test
    public void testInvalidPolygon() {
        assertThrows(IllegalArgumentException.class, () -> new Poligono(null));
        assertThrows(IllegalArgumentException.class, () -> new Poligono(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(1.0, 0.0)
        }));
        assertThrows(IllegalArgumentException.class, () -> new Poligono(new Ponto[] {
                new Ponto(0.0, 0.0), null, new Ponto(1.0, 1.0)
        }));
    }
}
