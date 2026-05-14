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
public class AutoPilotTests {
    private static final double EPS = TestSupport.EPS;

    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Ponto start = new Ponto(2.0, 2.0);
        Ponto finish = new Ponto(2.0, 4.0);
        AutoPilot ap = new AutoPilot(start, finish);

        assertSame(start, ap.getStart());
        assertSame(finish, ap.getFinish());
    }

    /**
 * Responsabilidade: validar time and speed examples através de um teste unitário.
 */
    @Test
    public void testTimeAndSpeedExamples() {
        AutoPilot ap = new AutoPilot(new Ponto(3.0, 2.0), new Ponto(3.0, 4.0));

        assertEquals(5.0, ap.time(0.4), EPS);
        TestSupport.assertVetor(ap.speed(new Vetor(0.2, 0.2), 5.0), -0.2, 0.2);
    }

    /**
 * Responsabilidade: validar speed opposite direction através de um teste unitário.
 */
    @Test
    public void testSpeedOppositeDirection() {
        AutoPilot ap = new AutoPilot(new Ponto(3.0, 4.0), new Ponto(3.0, 2.0));

        TestSupport.assertVetor(ap.speed(new Vetor(0.2, 0.2), 5.0), -0.2, -0.6);
    }

    /**
 * Responsabilidade: validar invalid arguments através de um teste unitário.
 */
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
