package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
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
public class EstadoSimulacaoTests {
    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
    @Test
    public void testConstructorAndGetters() {
        Vetor corrente = new Vetor(1.0, 0.0);
        InfoNavio infoNavio = new InfoNavio("A0", new Ponto(0.0, 0.0), EstadoNavio.EM_MOVIMENTO, false);
        InfoPorto infoPorto = new InfoPorto("A", new Ponto(0.0, 0.0), List.of());
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) });
        Obstaculo obstaculo = new Circulo(new Ponto(5.0, 5.0), 1.0);

        EstadoSimulacao estado = new EstadoSimulacao(
                4, corrente, List.of(infoNavio), List.of(infoPorto), List.of(rota), List.of(obstaculo)
        );

        assertEquals(4, estado.getTempoAtual());
        assertSame(corrente, estado.getCorrente());
        assertEquals(1, estado.getNavios().size());
        assertEquals(1, estado.getPortos().size());
        assertEquals(1, estado.getRotas().size());
        assertEquals(1, estado.getObstaculos().size());
    }

    /**
 * Responsabilidade: validar lists are copied and unmodifiable através de um teste unitário.
 */
    @Test
    public void testListsAreCopiedAndUnmodifiable() {
        List<InfoNavio> navios = new ArrayList<>();
        navios.add(new InfoNavio("A0", new Ponto(0.0, 0.0), EstadoNavio.EM_MOVIMENTO, false));

        EstadoSimulacao estado = new EstadoSimulacao(0, new Vetor(0.0, 0.0), navios, List.of(), List.of(), List.of());
        navios.clear();

        assertEquals(1, estado.getNavios().size());
        assertThrows(UnsupportedOperationException.class,
                () -> estado.getNavios().add(new InfoNavio("B0", new Ponto(1.0, 0.0), EstadoNavio.EM_MOVIMENTO, false)));
    }

    /**
 * Responsabilidade: validar invalid estado simulacao através de um teste unitário.
 */
    @Test
    public void testInvalidEstadoSimulacao() {
        assertThrows(IllegalArgumentException.class,
                () -> new EstadoSimulacao(-1, new Vetor(0.0, 0.0), List.of(), List.of(), List.of(), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new EstadoSimulacao(0, null, List.of(), List.of(), List.of(), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new EstadoSimulacao(0, new Vetor(0.0, 0.0), null, List.of(), List.of(), List.of()));
    }
}
