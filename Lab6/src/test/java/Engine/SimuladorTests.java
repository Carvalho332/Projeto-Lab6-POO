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
public class SimuladorTests {
    /**
 * Responsabilidade: realizar a operação mapa com viagem no contexto da classe SimuladorTests.
 * @param distancia distancia usado pelo método para cumprir a responsabilidade descrita.
 * @param velocidade velocidade usado pelo método para cumprir a responsabilidade descrita.
 * @return objeto resultante da operação.
 */
    private MapaNavegacao mapaComViagem(double distancia, double velocidade) {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", distancia, 0.0);
        a.adicionarViagem(new Viagem(0, b, velocidade));

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));
        return mapa;
    }

    /**
 * Responsabilidade: validar iniciar através de um teste unitário.
 */
    @Test
    public void testIniciar() {
        MapaNavegacao mapa = mapaComViagem(3.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));

        EstadoSimulacao estado = simulador.iniciar();

        assertEquals(0, simulador.getTempoAtual());
        assertEquals(0, estado.getTempoAtual());
        assertEquals(0, estado.getNavios().size());
        assertEquals(2, estado.getPortos().size());
    }

    /**
 * Responsabilidade: validar passo creates and moves ship através de um teste unitário.
 */
    @Test
    public void testPassoCreatesAndMovesShip() {
        MapaNavegacao mapa = mapaComViagem(3.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();

        EstadoSimulacao estado = simulador.passo();

        assertEquals(1, simulador.getTempoAtual());
        assertEquals(1, estado.getTempoAtual());
        assertEquals(1, estado.getNavios().size());
        assertEquals("A0", estado.getNavios().get(0).getCodigoViagem());
        TestSupport.assertPonto(estado.getNavios().get(0).getPosicao(), 1.0, 0.0);
    }

    /**
 * Responsabilidade: validar ship is removed when arrives através de um teste unitário.
 */
    @Test
    public void testShipIsRemovedWhenArrives() {
        MapaNavegacao mapa = mapaComViagem(1.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();

        EstadoSimulacao estado = simulador.passo();

        assertEquals(1, estado.getTempoAtual());
        assertTrue(estado.getNavios().isEmpty());
    }

    /**
 * Responsabilidade: validar viagem stays in port when no route exists através de um teste unitário.
 */
    @Test
    public void testViagemStaysInPortWhenNoRouteExists() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        a.adicionarViagem(new Viagem(0, b, 1.0));
        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);

        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();
        EstadoSimulacao estado = simulador.passo();

        assertTrue(estado.getNavios().isEmpty());
        assertEquals(1, a.getListaEspera().size());
    }

    /**
 * Responsabilidade: validar invalid simulador através de um teste unitário.
 */
    @Test
    public void testInvalidSimulador() {
        assertThrows(IllegalArgumentException.class, () -> new Simulador(null, new Vetor(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Simulador(new MapaNavegacao(), null));
    }
}
