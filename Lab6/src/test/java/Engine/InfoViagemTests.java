package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar o DTO InfoViagem, validando criação direta e criação a partir de Viagem.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 12/05/2026
 */
public class InfoViagemTests {
    private static final double EPS = TestSupport.EPS;

    @Test
    public void testConstrutorDireto() {
        InfoViagem info = new InfoViagem(12, "B", 1.25);

        assertEquals(12, info.getTempoSaida());
        assertEquals("B", info.getDestino());
        assertEquals(1.25, info.getVelocidadeLinear(), EPS);
    }

    @Test
    public void testConstrutorComViagem() {
        Porto destino = TestSupport.porto("C", 4.0, 5.0);
        Viagem viagem = new Viagem(8, destino, 0.9);

        InfoViagem info = new InfoViagem(viagem);

        assertEquals(8, info.getTempoSaida());
        assertEquals("C", info.getDestino());
        assertEquals(0.9, info.getVelocidadeLinear(), EPS);
    }

    @Test
    public void testInvalidArguments() {
        Porto destino = TestSupport.porto("C", 4.0, 5.0);
        Viagem viagem = new Viagem(8, destino, 0.9);

        assertThrows(IllegalArgumentException.class, () -> new InfoViagem(-1, "B", 1.0));
        assertThrows(IllegalArgumentException.class, () -> new InfoViagem(0, null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new InfoViagem(0, " ", 1.0));
        assertThrows(IllegalArgumentException.class, () -> new InfoViagem(0, "B", 0.0));
        assertThrows(IllegalArgumentException.class, () -> new InfoViagem((Viagem) null));

        InfoViagem info = new InfoViagem(viagem);
        assertEquals("C", info.getDestino());
    }
}
