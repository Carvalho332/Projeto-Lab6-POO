package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe AutoPilot, validando o cálculo do tempo e da velocidade vetorial compensada pela corrente.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class AutoPilotTests {
    private static final double EPS = TestSupport.EPS;

    @Test
    public void testConstructorAndGetters() {
        Ponto start = new Ponto(2.0, 2.0);
        Ponto finish = new Ponto(2.0, 4.0);
        AutoPilot ap = new AutoPilot(start, finish);

        assertSame(start, ap.getStart());
        assertSame(finish, ap.getFinish());
    }

    @Test
    public void testTimeAndSpeedExamples() {
        AutoPilot ap = new AutoPilot(new Ponto(3.0, 2.0), new Ponto(3.0, 4.0));

        assertEquals(5.0, ap.time(0.4), EPS);
        TestSupport.assertVetor(ap.speed(new Vetor(0.2, 0.2), 5.0), -0.2, 0.2);
    }

    @Test
    public void testSpeedOppositeDirection() {
        AutoPilot ap = new AutoPilot(new Ponto(3.0, 4.0), new Ponto(3.0, 2.0));

        TestSupport.assertVetor(ap.speed(new Vetor(0.2, 0.2), 5.0), -0.2, -0.6);
    }

    @Test
    public void testInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new AutoPilot(null, new Ponto(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class,
                () -> new AutoPilot(new Ponto(0.0, 0.0), new Ponto(1.0, 0.0)).time(0.0));
        assertThrows(IllegalArgumentException.class,
                () -> new AutoPilot(new Ponto(0.0, 0.0), new Ponto(1.0, 0.0)).speed(null, 1.0));
        assertThrows(IllegalArgumentException.class,
                () -> new AutoPilot(new Ponto(0.0, 0.0), new Ponto(1.0, 0.0)).speed(new Vetor(0.0, 0.0), 0.0));
    }
}
