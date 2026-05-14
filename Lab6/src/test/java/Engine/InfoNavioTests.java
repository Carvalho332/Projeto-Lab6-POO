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
public class InfoNavioTests {
    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        InfoNavio info = new InfoNavio("A0", new Ponto(1.0, 2.0), EstadoNavio.EM_MOVIMENTO, false);

        assertEquals("A0", info.getCodigoViagem());
        TestSupport.assertPonto(info.getPosicao(), 1.0, 2.0);
        assertEquals(EstadoNavio.EM_MOVIMENTO, info.getEstado());
        assertEquals(1.0, info.getVelocidadeLinear(), 1e-9);
        assertFalse(info.deveMostrarCirculoColisao());
    }

    /**
 * Responsabilidade: validar constructor from navio através de um teste unitário.
 */
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
        assertEquals(1.0, info.getVelocidadeLinear(), 1e-9);
        assertTrue(info.deveMostrarCirculoColisao());
    }

    /**
 * Responsabilidade: validar constructor from navio has velocity através de um teste unitário.
 */
    @Test
    public void testConstructorFromNavioHasVelocity() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Viagem v = new Viagem(0, b, 2.0);
        Route r = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });
        Navio n = new Navio("A0", a, v, r);

        InfoNavio info = new InfoNavio(n, new Vetor(0.5, 0.0));

        assertNotNull(info.getVelocidadeVetorial());
        assertEquals(2.0, info.getVelocidadeLinear(), 1e-9);
    }

    /**
 * Responsabilidade: validar que o construtor completo guarda a velocidade linear recebida.
 */
    @Test
    public void testConstructorWithVelocidadeLinear() {
        InfoNavio info = new InfoNavio("A0", new Ponto(1.0, 2.0), EstadoNavio.EM_MOVIMENTO, false, 1.5);
        assertEquals(1.5, info.getVelocidadeLinear(), 1e-9);
    }

    /**
 * Responsabilidade: validar invalid info navio através de um teste unitário.
 */
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
