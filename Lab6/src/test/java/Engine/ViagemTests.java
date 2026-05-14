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
public class ViagemTests {
    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Porto destino = TestSupport.porto("B", 10.0, 0.0);
        Viagem viagem = new Viagem(12, destino, 2.5);

        assertEquals(12, viagem.getTempoSaida());
        assertSame(destino, viagem.getDestino());
        assertEquals(2.5, viagem.getVelocidadeLinear(), TestSupport.EPS);
    }

    /**
 * Responsabilidade: validar invalid viagem através de um teste unitário.
 */
    @Test
    public void testInvalidViagem() {
        Porto destino = TestSupport.porto("B", 10.0, 0.0);

        assertThrows(IllegalArgumentException.class, () -> new Viagem(-1, destino, 2.0));
        assertThrows(IllegalArgumentException.class, () -> new Viagem(0, null, 2.0));
        assertThrows(IllegalArgumentException.class, () -> new Viagem(0, destino, 0.0));
    }
}
