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
public class PortoTests {
    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Ponto p = new Ponto(1.0, 2.0);
        Porto porto = new Porto("A", p);

        assertEquals("A", porto.getNome());
        assertSame(p, porto.getPosicao());
        assertTrue(porto.getListaEspera().isEmpty());
    }

    /**
 * Responsabilidade: validar adicionar and remover viagem através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar lista espera is unmodifiable através de um teste unitário.
 */
    @Test
    public void testListaEsperaIsUnmodifiable() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);

        assertThrows(UnsupportedOperationException.class,
                () -> a.getListaEspera().add(new Viagem(0, b, 1.0)));
    }

    /**
 * Responsabilidade: validar invalid porto através de um teste unitário.
 */
    @Test
    public void testInvalidPorto() {
        assertThrows(IllegalArgumentException.class, () -> new Porto(null, new Ponto(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Porto("", new Ponto(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Porto("A", null));
        assertThrows(IllegalArgumentException.class, () -> TestSupport.porto("A", 0.0, 0.0).adicionarViagem(null));
    }
}
