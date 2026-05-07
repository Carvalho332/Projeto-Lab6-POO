package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Cliente, validando o comportamento previsto para o ponto de entrada/execução auxiliar.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class ClienteTests {
    @Test
    public void testMainDoesNotThrow() {
        assertDoesNotThrow(() -> Cliente.main(new String[0]));
    }
}
