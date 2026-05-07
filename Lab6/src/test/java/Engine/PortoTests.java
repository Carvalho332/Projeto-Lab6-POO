package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Porto, validando nome, posição e gestão da lista de viagens em espera.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class PortoTests {
    @Test
    public void testConstructorAndGetters() {
        Ponto p = new Ponto(1.0, 2.0);
        Porto porto = new Porto("A", p);

        assertEquals("A", porto.getNome());
        assertSame(p, porto.getPosicao());
        assertTrue(porto.getListaEspera().isEmpty());
    }

    @Test
    public void testAdicionarAndRemoverViagem() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Viagem viagem = new Viagem(5, b, 2.0);

        a.adicionarViagem(viagem);
        assertEquals(1, a.getListaEspera().size());
        assertSame(viagem, a.getListaEspera().get(0));

        a.removerViagem(viagem);
        assertTrue(a.getListaEspera().isEmpty());
    }

    @Test
    public void testListaEsperaIsUnmodifiable() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);

        assertThrows(UnsupportedOperationException.class,
                () -> a.getListaEspera().add(new Viagem(0, b, 1.0)));
    }

    @Test
    public void testInvalidPorto() {
        assertThrows(IllegalArgumentException.class, () -> new Porto(null, new Ponto(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Porto("", new Ponto(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Porto("A", null));
        assertThrows(IllegalArgumentException.class, () -> TestSupport.porto("A", 0.0, 0.0).adicionarViagem(null));
    }
}
