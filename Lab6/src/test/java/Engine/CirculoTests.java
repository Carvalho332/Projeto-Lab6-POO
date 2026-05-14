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
public class CirculoTests {
    private static final double EPS = TestSupport.EPS;

    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Ponto c = new Ponto(1.0, 2.0);
        Circulo circ = new Circulo(c, 3.0);

        assertSame(c, circ.getCentro());
        assertEquals(3.0, circ.getRaio(), EPS);
    }

    /**
 * Responsabilidade: validar intersect diameter através de um teste unitário.
 */
    @Test
    public void testIntersectDiameter() {
        Circulo circ = new Circulo(new Ponto(0.0, 0.0), 1.0);
        SegmentoReta s = new SegmentoReta(new Ponto(-2.0, 0.0), new Ponto(2.0, 0.0));

        Ponto[] inter = circ.intersect(s);

        assertNotNull(inter);
        assertEquals(2, inter.length);
        TestSupport.assertContainsPonto(inter, -1.0, 0.0);
        TestSupport.assertContainsPonto(inter, 1.0, 0.0);
    }

    /**
 * Responsabilidade: validar intersect tangent and no intersection através de um teste unitário.
 */
    @Test
    public void testIntersectTangentAndNoIntersection() {
        Circulo circ = new Circulo(new Ponto(0.0, 0.0), 1.0);

        Ponto[] tangent = circ.intersect(new SegmentoReta(new Ponto(-1.0, 1.0), new Ponto(1.0, 1.0)));
        assertNotNull(tangent);
        assertEquals(1, tangent.length);
        TestSupport.assertPonto(tangent[0], 0.0, 1.0);

        assertNull(circ.intersect(new SegmentoReta(new Ponto(2.0, 2.0), new Ponto(3.0, 2.0))));
    }

    /**
 * Responsabilidade: validar invalid circle através de um teste unitário.
 */
    @Test
    public void testInvalidCircle() {
        assertThrows(IllegalArgumentException.class, () -> new Circulo(null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new Circulo(new Ponto(0.0, 0.0), 0.0));
    }
}
