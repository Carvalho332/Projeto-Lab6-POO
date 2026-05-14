package Engine;

import java.util.Optional;

/**
 * Responsabilidade: definir o contrato para algoritmos que calculam rotas entre portos.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public interface EstrategiaCalculoRota {

    Optional<Route> calcular(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double velocidadeLinear);
}
