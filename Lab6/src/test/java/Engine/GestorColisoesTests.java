package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe GestorColisoes, validando a deteção de colisões e a regra de prioridade lexicográfica entre navios.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class GestorColisoesTests {
    private Navio navio(String codigo, double origemY, double destinoY) {
        Porto origem = TestSupport.porto(codigo.substring(0, 1), 0.0, origemY);
        Porto destino = TestSupport.porto("D" + codigo, 10.0, destinoY);
        Viagem viagem = new Viagem(0, destino, 1.0);
        Route rota = new Route(new Ponto[] { origem.getPosicao(), destino.getPosicao() });
        return new Navio(codigo, origem, viagem, rota);
    }

    @Test
    public void testHaColisaoAndNaoHaColisao() {
        assertTrue(new GestorColisoes().haColisao(navio("A12", 0.0, 0.0), navio("F9", 1.0, 1.0)));
        assertFalse(new GestorColisoes().haColisao(navio("A12", 0.0, 0.0), navio("F9", 5.0, 5.0)));
    }

    @Test
    public void testNavioQueEsperaIsLexicographicallySmaller() {
        Navio a = navio("A12", 0.0, 0.0);
        Navio f = navio("F9", 1.0, 1.0);

        assertSame(a, new GestorColisoes().navioQueEspera(a, f));
    }

    @Test
    public void testResolverMakesSmallerCodeWait() {
        Navio a = navio("A12", 0.0, 0.0);
        Navio f = navio("F9", 1.0, 1.0);

        new GestorColisoes().resolver(List.of(a, f));

        assertEquals(EstadoNavio.EM_ESPERA, a.getEstado());
        assertTrue(a.deveMostrarCirculoColisao());
        assertEquals(EstadoNavio.EM_MOVIMENTO, f.getEstado());
    }

    @Test
    public void testInvalidArguments() {
        GestorColisoes g = new GestorColisoes();
        Navio a = navio("A12", 0.0, 0.0);

        assertThrows(IllegalArgumentException.class, () -> g.resolver(null));
        assertThrows(IllegalArgumentException.class, () -> g.haColisao(null, a));
        assertThrows(IllegalArgumentException.class, () -> g.navioQueEspera(a, null));
    }
}
