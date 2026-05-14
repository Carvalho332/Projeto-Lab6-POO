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
public class EstadoNavioTests {
    /**
 * Responsabilidade: validar values and value of através de um teste unitário.
 */
    @Test
    public void testValuesAndValueOf() {
        assertArrayEquals(
                new EstadoNavio[] { EstadoNavio.EM_MOVIMENTO, EstadoNavio.EM_ESPERA, EstadoNavio.CHEGOU },
                EstadoNavio.values()
        );
        assertEquals(EstadoNavio.EM_MOVIMENTO, EstadoNavio.valueOf("EM_MOVIMENTO"));
    }
}
