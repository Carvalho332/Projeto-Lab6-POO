package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Simulador, validando a inicialização, evolução por passos,
 * aplicação da estratégia de cálculo de rota e produção de EstadoSimulacao.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 12/05/2026
 */
public class SimuladorTests {
    private MapaNavegacao mapaComViagem(double distancia, double velocidade) {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", distancia, 0.0);
        a.adicionarViagem(new Viagem(0, b, velocidade));

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarRota(new Route(new Ponto[]{a.getPosicao(), b.getPosicao()}));
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
        assertSame(mapa.getRotas().get(0), estado.getRotas().get(0));
    }

    @Test
    public void testPassoCriaEMoveNavio() {
        MapaNavegacao mapa = mapaComViagem(3.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();

        EstadoSimulacao estado = simulador.passo();

        assertEquals(1, simulador.getTempoAtual());
        assertEquals(1, estado.getTempoAtual());
        assertEquals(1, estado.getNavios().size());
        assertEquals("A0", estado.getNavios().get(0).getCodigoViagem());
        assertEquals(EstadoNavio.EM_MOVIMENTO, estado.getNavios().get(0).getEstado());
        TestSupport.assertPonto(estado.getNavios().get(0).getPosicao(), 1.0, 0.0);
    }

    @Test
    public void testNavioChegadoFicaVisivelUmPassoEDepoisEremovido() {
        MapaNavegacao mapa = mapaComViagem(1.0, 1.0);
        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0));
        simulador.iniciar();

        EstadoSimulacao estadoChegada = simulador.passo();

        assertEquals(1, estadoChegada.getTempoAtual());
        assertEquals(1, estadoChegada.getNavios().size());
        assertEquals(EstadoNavio.CHEGOU, estadoChegada.getNavios().get(0).getEstado());
        TestSupport.assertPonto(estadoChegada.getNavios().get(0).getPosicao(), 1.0, 0.0);

        EstadoSimulacao estadoSeguinte = simulador.passo();

        assertEquals(2, estadoSeguinte.getTempoAtual());
        assertTrue(estadoSeguinte.getNavios().isEmpty());
        assertTrue(simulador.terminou());
    }

    @Test
    public void testViagemPermaneceNoPortoQuandoNaoExisteRota() {
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
        assertFalse(simulador.terminou());
    }

    @Test
    public void testSimuladorUsaEstrategiaInjetada() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 5.0, 0.0);
        a.adicionarViagem(new Viagem(0, b, 1.0));

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);

        EstrategiaCalculoRota estrategia = (origem, destino, m, corrente, velocidadeLinear) ->
                Optional.of(new Route(new Ponto[]{origem.getPosicao(), destino.getPosicao()}));

        Simulador simulador = new Simulador(mapa, new Vetor(0.0, 0.0), estrategia);
        simulador.iniciar();
        EstadoSimulacao estado = simulador.passo();

        assertEquals(1, estado.getNavios().size());
        assertEquals("A0", estado.getNavios().get(0).getCodigoViagem());
        assertEquals(0, a.getListaEspera().size());
    }

    @Test
    public void testEstadoContemCorrenteEObstaculosDoMapa() {
        MapaNavegacao mapa = mapaComViagem(3.0, 1.0);
        Circulo obstaculo = new Circulo(new Ponto(100.0, 100.0), 1.0);
        mapa.adicionarObstaculoFixo(obstaculo);
        Vetor corrente = new Vetor(0.2, -0.3);

        Simulador simulador = new Simulador(mapa, corrente);
        EstadoSimulacao estado = simulador.iniciar();

        assertSame(corrente, estado.getCorrente());
        assertTrue(estado.getObstaculos().contains(obstaculo));
    }

    @Test
    public void testInvalidSimulador() {
        assertThrows(IllegalArgumentException.class, () -> new Simulador(null, new Vetor(0.0, 0.0)));
        assertThrows(IllegalArgumentException.class, () -> new Simulador(new MapaNavegacao(), null));
        assertThrows(IllegalArgumentException.class,
                () -> new Simulador(new MapaNavegacao(), new Vetor(0.0, 0.0), null));
    }
}
