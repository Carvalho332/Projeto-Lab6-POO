package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Simulador, validando a inicialização, evolução por passos e produção de EstadoSimulacao.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class SimuladorTests {
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

    @Test
    public void testShipIsRemovedWhenArrives() {
        MapaNavegacao mapa = mapaComViagem(1.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();

        EstadoSimulacao estado = simulador.passo();

        assertEquals(1, estado.getTempoAtual());
        assertTrue(estado.getNavios().isEmpty());
    }

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

    @Test
    public void testInvalidSimulador() {
        assertThrows(IllegalArgumentException.class, () -> new Simulador(null, new Vetor(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Simulador(new MapaNavegacao(), null));
    }
}
