package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar o enum EstadoNavio, validando os estados possíveis de um navio durante a simulação.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class EstadoNavioTests {
    @Test
    public void testValuesAndValueOf() {
        assertArrayEquals(
                new EstadoNavio[] { EstadoNavio.EM_MOVIMENTO, EstadoNavio.EM_ESPERA, EstadoNavio.CHEGOU },
                EstadoNavio.values()
        );
        assertEquals(EstadoNavio.EM_MOVIMENTO, EstadoNavio.valueOf("EM_MOVIMENTO"));
    }
}
