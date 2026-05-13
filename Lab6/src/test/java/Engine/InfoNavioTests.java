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
    public void testConstructorFromNavioHasVelocity() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Viagem v = new Viagem(0, b, 2.0);
        Route r = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });
        Navio n = new Navio("A0", a, v, r);

        InfoNavio info = new InfoNavio(n, new Vetor(0.5, 0.0));

        assertNotNull(info.getVelocidadeVetorial());
    }



    @Test
    public void testHelpersDeEstado() {
        InfoNavio movimento = new InfoNavio("A0", new Ponto(0.0, 0.0), EstadoNavio.EM_MOVIMENTO, false);
        InfoNavio espera = new InfoNavio("A1", new Ponto(0.0, 0.0), EstadoNavio.EM_ESPERA, true);
        InfoNavio chegou = new InfoNavio("A2", new Ponto(0.0, 0.0), EstadoNavio.CHEGOU, false);

        assertTrue(movimento.estaEmMovimento());
        assertFalse(movimento.estaEmEspera());
        assertFalse(movimento.chegou());

        assertTrue(espera.estaEmEspera());
        assertFalse(espera.estaEmMovimento());

        assertTrue(chegou.chegou());
    }

    @Test
    public void testConstructorFromNavioEmEsperaTemVelocidadeZero() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Navio n = new Navio("A0", a, new Viagem(0, b, 2.0), new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));
        n.esperar();

        InfoNavio info = new InfoNavio(n, new Vetor(0.5, 0.0));

        assertTrue(info.temVelocidadeVetorial());
        TestSupport.assertVetor(info.getVelocidadeVetorial(), 0.0, 0.0);
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
