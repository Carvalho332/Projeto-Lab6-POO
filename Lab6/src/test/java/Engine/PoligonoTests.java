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
public class PoligonoTests {
    /**
 * Responsabilidade: realizar a operação quadrado como poligono no contexto da classe PoligonoTests.
 * @return objeto resultante da operação.
 */
    private Poligono quadradoComoPoligono() {
        return new Poligono(new Ponto[] {
                new Ponto(0.0, 0.0),
                new Ponto(2.0, 0.0),
                new Ponto(2.0, 2.0),
                new Ponto(0.0, 2.0)
        });
    }

    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Poligono p = quadradoComoPoligono();

        assertEquals(4, p.getNumeroVertices());
        TestSupport.assertPonto(p.getVertice(0), 0.0, 0.0);
        TestSupport.assertPonto(p.getVertice(2), 2.0, 2.0);
    }

    /**
 * Responsabilidade: validar get vertices returns copy através de um teste unitário.
 */
    @Test
    public void testGetVerticesReturnsCopy() {
        Poligono p = quadradoComoPoligono();
        Ponto[] copia = p.getVertices();
        copia[0] = new Ponto(99.0, 99.0);

        TestSupport.assertPonto(p.getVertice(0), 0.0, 0.0);
    }

    /**
 * Responsabilidade: validar intersect segmento através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar invalid polygon através de um teste unitário.
 */
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
