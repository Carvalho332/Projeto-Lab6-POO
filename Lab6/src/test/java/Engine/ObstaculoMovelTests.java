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
public class ObstaculoMovelTests {
    /**
 * Responsabilidade: validar obstacle movel is circle através de um teste unitário.
 */
    @Test
    public void testObstacleMovelIsCircle() {
        ObstaculoMovel o = new ObstaculoMovel(new Ponto(2.0, 3.0), 1.5);

        assertTrue(o instanceof Circulo);
        TestSupport.assertPonto(o.getCentro(), 2.0, 3.0);
        assertEquals(1.5, o.getRaio(), TestSupport.EPS);
    }

    /**
 * Responsabilidade: validar invalid obstacle movel através de um teste unitário.
 */
    @Test
    public void testInvalidObstacleMovel() {
        assertThrows(IllegalArgumentException.class, () -> new ObstaculoMovel(null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new ObstaculoMovel(new Ponto(0.0, 0.0), 0.0));
    }
}
