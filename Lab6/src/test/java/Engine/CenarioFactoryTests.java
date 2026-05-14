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
public class CenarioFactoryTests {
    /**
 * Responsabilidade: validar cenario cumpre minimos do enunciado através de um teste unitário.
 */
    @Test
    public void testCenarioCumpreMinimosDoEnunciado() {
        MapaNavegacao mapa = CenarioFactory.criarMapaDemo(123L);

        assertTrue(mapa.getPortos().size() >= 4);
        assertTrue(mapa.getRotas().size() >= 6);
        assertTrue(mapa.getObstaculosFixos().size() >= 4);
        assertTrue(mapa.getObstaculosMoveis().size() >= 2);

        for (Route rota : mapa.getRotas()) {
            assertTrue(rota.getNumeroPontos() >= 4);
        }
    }

    /**
 * Responsabilidade: validar obstaculos moveis intersetam rotas através de um teste unitário.
 */
    @Test
    public void testObstaculosMoveisIntersetamRotas() {
        MapaNavegacao mapa = CenarioFactory.criarMapaDemo(456L);

        for (ObstaculoMovel movel : mapa.getObstaculosMoveis()) {
            assertTrue(intersetaAlgumaRota(movel, mapa));
        }
    }

    /**
 * Responsabilidade: validar obstaculos moveis mudam entre simulacoes através de um teste unitário.
 */
    @Test
    public void testObstaculosMoveisMudamEntreSimulacoes() {
        MapaNavegacao mapa1 = CenarioFactory.criarMapaDemo(1L);
        MapaNavegacao mapa2 = CenarioFactory.criarMapaDemo(2L);

        Ponto p1 = mapa1.getObstaculosMoveis().get(0).getCentro();
        Ponto p2 = mapa2.getObstaculosMoveis().get(0).getCentro();

        assertFalse(p1.igual(p2));
    }

    /**
 * Responsabilidade: validar que o simulador pode ser criado com uma corrente introduzida pelo utilizador.
 */
    @Test
    public void testCriarSimuladorDemoComCorrenteIndicada() {
        Vetor corrente = new Vetor(0.25, -0.50);
        Simulador simulador = CenarioFactory.criarSimuladorDemo(123L, corrente);
        EstadoSimulacao estado = simulador.iniciar();

        assertEquals(0.25, estado.getCorrente().getX(), 1e-9);
        assertEquals(-0.50, estado.getCorrente().getY(), 1e-9);
    }

    /**
 * Responsabilidade: validar obstaculos moveis ficam fixos durante simulacao através de um teste unitário.
 */
    @Test
    public void testObstaculosMoveisFicamFixosDuranteSimulacao() {
        Simulador simulador = CenarioFactory.criarSimuladorDemo(999L);
        EstadoSimulacao estadoInicial = simulador.iniciar();

        Ponto centroInicial = ((ObstaculoMovel) estadoInicial.getObstaculos().stream()
                .filter(o -> o instanceof ObstaculoMovel)
                .findFirst()
                .orElseThrow()).getCentro();

        EstadoSimulacao estado = estadoInicial;
        for (int i = 0; i < 5; i++) {
            estado = simulador.passo();
        }

        Ponto centroFinal = ((ObstaculoMovel) estado.getObstaculos().stream()
                .filter(o -> o instanceof ObstaculoMovel)
                .findFirst()
                .orElseThrow()).getCentro();

        assertTrue(centroInicial.igual(centroFinal));
    }

    /**
 * Responsabilidade: realizar a operação interseta alguma rota no contexto da classe CenarioFactoryTests.
 * @param obstaculo obstáculo analisado.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean intersetaAlgumaRota(ObstaculoMovel obstaculo, MapaNavegacao mapa) {
        for (Route rota : mapa.getRotas()) {
            if (rota.intersect(obstaculo) != null) {
                return true;
            }
            for (int i = 0; i < rota.getNumeroPontos(); i++) {
                if (obstaculo.contem(rota.getPonto(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
