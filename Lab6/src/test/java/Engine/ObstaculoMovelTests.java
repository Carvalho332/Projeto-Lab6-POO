package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe ObstaculoMovel, validando a sua criação como obstáculo circular reposicionável no início da simulação.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class ObstaculoMovelTests {
    @Test
    public void testObstacleMovelIsCircle() {
        ObstaculoMovel o = new ObstaculoMovel(new Ponto(2.0, 3.0), 1.5);

        assertTrue(o instanceof Circulo);
        TestSupport.assertPonto(o.getCentro(), 2.0, 3.0);
        assertEquals(1.5, o.getRaio(), TestSupport.EPS);
    }

    @Test
    public void testInvalidObstacleMovel() {
        assertThrows(IllegalArgumentException.class, () -> new ObstaculoMovel(null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new ObstaculoMovel(new Ponto(0.0, 0.0), 0.0));
    }
}
