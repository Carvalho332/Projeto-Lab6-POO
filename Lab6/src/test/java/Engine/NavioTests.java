package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Navio, validando posição, estado, rota, avanço, espera, retoma e chegada ao destino.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class NavioTests {
    private Navio navioBase() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 3.0, 0.0);
        Viagem viagem = new Viagem(0, b, 1.0);
        Route rota = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });
        return new Navio("A0", a, viagem, rota);
    }

    @Test
    public void testConstructorAndGetters() {
        Navio n = navioBase();

        assertEquals("A0", n.getCodigoViagem());
        TestSupport.assertPonto(n.getPosicaoAtual(), 0.0, 0.0);
        assertEquals(EstadoNavio.EM_MOVIMENTO, n.getEstado());
        assertEquals(1.0, n.getVelocidadeLinear(), TestSupport.EPS);
        assertFalse(n.deveMostrarCirculoColisao());
    }

    @Test
    public void testProximaPosicaoAndAvancar() {
        Navio n = navioBase();

        TestSupport.assertPonto(n.proximaPosicao(), 1.0, 0.0);
        n.avancar();

        TestSupport.assertPonto(n.getPosicaoAtual(), 1.0, 0.0);
        assertEquals(EstadoNavio.EM_MOVIMENTO, n.getEstado());
    }

    @Test
    public void testEsperarAndRetomar() {
        Navio n = navioBase();

        n.esperar();
        assertEquals(EstadoNavio.EM_ESPERA, n.getEstado());
        assertTrue(n.deveMostrarCirculoColisao());
        TestSupport.assertPonto(n.proximaPosicao(), 0.0, 0.0);

        n.retomar();
        assertEquals(EstadoNavio.EM_MOVIMENTO, n.getEstado());
        assertFalse(n.deveMostrarCirculoColisao());
    }

    @Test
    public void testChegouDestino() {
        Navio n = navioBase();

        n.avancar();
        n.avancar();
        n.avancar();

        assertTrue(n.chegouDestino());
        assertEquals(EstadoNavio.CHEGOU, n.getEstado());
        TestSupport.assertPonto(n.getPosicaoAtual(), 3.0, 0.0);
    }

    @Test
    public void testDefinirRotaAtual() {
        Navio n = navioBase();
        Route nova = new Route(new Ponto[] { new Ponto(1.0, 1.0), new Ponto(5.0, 1.0) });

        n.definirRotaAtual(nova);

        assertSame(nova, n.getRotaAtual());
        TestSupport.assertPonto(n.getPosicaoAtual(), 1.0, 1.0);
        TestSupport.assertPonto(n.proximaPosicao(), 2.0, 1.0);
    }

    @Test
    public void testInvalidNavio() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 3.0, 0.0);
        Viagem viagem = new Viagem(0, b, 1.0);
        Route rota = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });

        assertThrows(IllegalArgumentException.class, () -> new Navio(null, a, viagem, rota));
        assertThrows(IllegalArgumentException.class, () -> new Navio("A0", null, viagem, rota));
        assertThrows(IllegalArgumentException.class, () -> new Navio("A0", a, null, rota));
        assertThrows(IllegalArgumentException.class, () -> new Navio("A0", a, viagem, null));
        assertThrows(IllegalArgumentException.class, () -> navioBase().definirRotaAtual(null));
    }
}
