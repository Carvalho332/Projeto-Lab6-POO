package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Viagem, validando tempo de saída, destino e velocidade linear.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class ViagemTests {
    @Test
    public void testConstructorAndGetters() {
        Porto destino = TestSupport.porto("B", 10.0, 0.0);
        Viagem viagem = new Viagem(12, destino, 2.5);

        assertEquals(12, viagem.getTempoSaida());
        assertSame(destino, viagem.getDestino());
        assertEquals(2.5, viagem.getVelocidadeLinear(), TestSupport.EPS);
    }

    @Test
    public void testInvalidViagem() {
        Porto destino = TestSupport.porto("B", 10.0, 0.0);

        assertThrows(IllegalArgumentException.class, () -> new Viagem(-1, destino, 2.0));
        assertThrows(IllegalArgumentException.class, () -> new Viagem(0, null, 2.0));
        assertThrows(IllegalArgumentException.class, () -> new Viagem(0, destino, 0.0));
    }
}
