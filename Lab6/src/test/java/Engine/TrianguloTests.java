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
public class TrianguloTests {
    /**
 * Responsabilidade: validar valid triangle através de um teste unitário.
 */
    @Test
    public void testValidTriangle() {
        Triangulo t = new Triangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(4.0, 0.0), new Ponto(0.0, 3.0)
        });

        assertEquals(3, t.getNumeroVertices());
    }

    /**
 * Responsabilidade: validar intersect segmento através de um teste unitário.
 */
    @Test
    public void testIntersectSegmento() {
        Triangulo t = new Triangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(4.0, 0.0), new Ponto(0.0, 4.0)
        });
        SegmentoReta s = new SegmentoReta(new Ponto(-1.0, 1.0), new Ponto(5.0, 1.0));

        Ponto[] inter = t.intersect(s);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, 0.0, 1.0);
        TestSupport.assertContainsPonto(inter, 3.0, 1.0);
    }

    /**
 * Responsabilidade: validar invalid triangle através de um teste unitário.
 */
    @Test
    public void testInvalidTriangle() {
        assertThrows(IllegalArgumentException.class, () -> new Triangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(1.0, 0.0)
        }));
        assertThrows(IllegalArgumentException.class, () -> new Triangulo(new Ponto[] {
                new Ponto(0.0, 0.0), new Ponto(1.0, 1.0), new Ponto(2.0, 2.0)
        }));
    }
}
