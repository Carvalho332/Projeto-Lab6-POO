package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe InfoNavio, validando a informação enviada pelo Engine ao GUI sobre cada navio.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class InfoNavioTests {
    @Test
    public void testConstructorAndGetters() {
        InfoNavio info = new InfoNavio("A0", new Ponto(1.0, 2.0), EstadoNavio.EM_MOVIMENTO, false);

        assertEquals("A0", info.getCodigoViagem());
        TestSupport.assertPonto(info.getPosicao(), 1.0, 2.0);
        assertEquals(EstadoNavio.EM_MOVIMENTO, info.getEstado());
        assertFalse(info.deveMostrarCirculoColisao());
    }

    @Test
    public void testConstructorFromNavio() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 3.0, 0.0);
        Navio n = new Navio("A0", a, new Viagem(0, b, 1.0), new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));
        n.esperar();

        InfoNavio info = new InfoNavio(n);

        assertEquals("A0", info.getCodigoViagem());
        TestSupport.assertPonto(info.getPosicao(), 0.0, 0.0);
        assertEquals(EstadoNavio.EM_ESPERA, info.getEstado());
        assertTrue(info.deveMostrarCirculoColisao());
    }

    @Test
    public void testInvalidInfoNavio() {
        assertThrows(IllegalArgumentException.class,
                () -> new InfoNavio(null, new Ponto(0.0, 0.0), EstadoNavio.EM_MOVIMENTO, false));
        assertThrows(IllegalArgumentException.class,
                () -> new InfoNavio("A0", null, EstadoNavio.EM_MOVIMENTO, false));
        assertThrows(IllegalArgumentException.class,
                () -> new InfoNavio("A0", new Ponto(0.0, 0.0), null, false));
    }
}
