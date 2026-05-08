package Engine;

import java.util.Optional;

/**
 * Responsabilidade: definir a estratégia usada para calcular uma rota disponível
 * entre dois portos.
 *
 * @version 2026-05-08
 * @see CalcularRota
 */
public interface EstrategiaCalculoRota {
    /**
     * Calcula uma rota possível entre dois portos.
     *
     * @param origem porto de origem
     * @param destino porto de destino
     * @param mapa mapa de navegação
     * @param corrente velocidade da corrente
     * @param velocidadeLinear velocidade linear pretendida para o navio
     * @return rota calculada, ou Optional.empty() se não existir caminho disponível
     */
    Optional<Route> calcular(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double velocidadeLinear);
}
