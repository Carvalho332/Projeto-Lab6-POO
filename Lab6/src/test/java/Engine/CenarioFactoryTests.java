package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar que o cenário de demonstração cumpre os mínimos do enunciado.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 08/05/2026
 */
public class CenarioFactoryTests {
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

    @Test
    public void testObstaculosMoveisIntersetamRotas() {
        MapaNavegacao mapa = CenarioFactory.criarMapaDemo(456L);

        for (ObstaculoMovel movel : mapa.getObstaculosMoveis()) {
            assertTrue(intersetaAlgumaRota(movel, mapa));
        }
    }

    @Test
    public void testObstaculosMoveisMudamEntreSimulacoes() {
        MapaNavegacao mapa1 = CenarioFactory.criarMapaDemo(1L);
        MapaNavegacao mapa2 = CenarioFactory.criarMapaDemo(2L);

        Ponto p1 = mapa1.getObstaculosMoveis().get(0).getCentro();
        Ponto p2 = mapa2.getObstaculosMoveis().get(0).getCentro();

        assertFalse(p1.igual(p2));
    }

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
