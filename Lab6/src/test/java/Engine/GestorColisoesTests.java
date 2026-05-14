package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: suportar uma responsabilidade específica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class GestorColisoesTests {
    /**
 * Responsabilidade: realizar a operação navio no contexto da classe GestorColisoesTests.
 * @param codigo codigo usado pelo método para cumprir a responsabilidade descrita.
 * @param origemY origem y usado pelo método para cumprir a responsabilidade descrita.
 * @param destinoY destino y usado pelo método para cumprir a responsabilidade descrita.
 * @return objeto resultante da operação.
 */
    private Navio navio(String codigo, double origemY, double destinoY) {
        Porto origem = TestSupport.porto(codigo.substring(0, 1), 0.0, origemY);
        Porto destino = TestSupport.porto("D" + codigo, 10.0, destinoY);
        Viagem viagem = new Viagem(0, destino, 1.0);
        Route rota = new Route(new Ponto[] { origem.getPosicao(), destino.getPosicao() });
        return new Navio(codigo, origem, viagem, rota);
    }

    /**
 * Responsabilidade: validar ha colisao and nao ha colisao através de um teste unitário.
 */
    @Test
    public void testHaColisaoAndNaoHaColisao() {
        assertTrue(new GestorColisoes().haColisao(navio("A12", 0.0, 0.0), navio("F9", 1.0, 1.0)));
        assertFalse(new GestorColisoes().haColisao(navio("A12", 0.0, 0.0), navio("F9", 5.0, 5.0)));
    }

    /**
 * Responsabilidade: validar navio que espera is lexicographically smaller através de um teste unitário.
 */
    @Test
    public void testNavioQueEsperaIsLexicographicallySmaller() {
        Navio a = navio("A12", 0.0, 0.0);
        Navio f = navio("F9", 1.0, 1.0);

        assertSame(a, new GestorColisoes().navioQueEspera(a, f));
    }

    /**
 * Responsabilidade: validar resolver makes smaller code wait através de um teste unitário.
 */
    @Test
    public void testResolverMakesSmallerCodeWait() {
        Navio a = navio("A12", 0.0, 0.0);
        Navio f = navio("F9", 1.0, 1.0);

        new GestorColisoes().resolver(List.of(a, f));

        assertEquals(EstadoNavio.EM_ESPERA, a.getEstado());
        assertTrue(a.deveMostrarCirculoColisao());
        assertEquals(EstadoNavio.EM_MOVIMENTO, f.getEstado());
    }

    /**
 * Responsabilidade: validar invalid arguments através de um teste unitário.
 */
    @Test
    public void testInvalidArguments() {
        GestorColisoes g = new GestorColisoes();
        Navio a = navio("A12", 0.0, 0.0);

        assertThrows(IllegalArgumentException.class, () -> g.resolver(null));
        assertThrows(IllegalArgumentException.class, () -> g.haColisao(null, a));
        assertThrows(IllegalArgumentException.class, () -> g.navioQueEspera(a, null));
    }
}
