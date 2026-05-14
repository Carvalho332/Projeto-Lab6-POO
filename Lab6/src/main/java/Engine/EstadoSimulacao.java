package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: transportar uma fotografia imutável do estado da simulação para ser apresentada pelo GUI.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os dados transportados não expõem listas internas modificáveis.
 */
public class EstadoSimulacao {
    private final int tempoAtual;
    private final Vetor corrente;
    private final List<InfoNavio> navios;
    private final List<InfoPorto> portos;
    private final List<Route> rotas;
    private final List<Obstaculo> obstaculos;

    /**
 * Responsabilidade: construir uma instância de EstadoSimulacao, validando os dados recebidos para preservar os invariantes.
 * @param tempoAtual instante atual da simulação.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param navios navios ativos considerados no passo da simulação.
 * @param portos portos do mapa ou do estado de simulação.
 * @param rotas rotas disponíveis no cenário ou no cálculo.
 * @param obstaculos obstáculos que podem bloquear rotas ou ser apresentados no mapa.
 */
    public EstadoSimulacao(int tempoAtual, Vetor corrente, List<InfoNavio> navios, List<InfoPorto> portos, List<Route> rotas, List<Obstaculo> obstaculos) {
        if (tempoAtual < 0 || corrente == null) {
            throw new IllegalArgumentException("EstadoSimulacao:iv");
        }

        this.tempoAtual = tempoAtual;
        this.corrente = corrente;
        this.navios = copiarListaSemNulls(navios, "navios");
        this.portos = copiarListaSemNulls(portos, "portos");
        this.rotas = copiarListaSemNulls(rotas, "rotas");
        this.obstaculos = copiarListaSemNulls(obstaculos, "obstaculos");
    }

    /**
 * Responsabilidade: devolver tempo atual associado à instância atual.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getTempoAtual() {
        return tempoAtual;
    }

    /**
 * Responsabilidade: devolver corrente associado à instância atual.
 * @return vetor resultante da operação.
 */
    public Vetor getCorrente() {
        return corrente;
    }

    /**
 * Responsabilidade: devolver navios associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<InfoNavio> getNavios() {
        return Collections.unmodifiableList(navios);
    }

    /**
 * Responsabilidade: devolver portos associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<InfoPorto> getPortos() {
        return Collections.unmodifiableList(portos);
    }

    /**
 * Responsabilidade: devolver rotas associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Route> getRotas() {
        return Collections.unmodifiableList(rotas);
    }

    /**
 * Responsabilidade: devolver obstaculos associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Obstaculo> getObstaculos() {
        return Collections.unmodifiableList(obstaculos);
    }

    /**
 * Responsabilidade: realizar a operação copiar lista sem nulls no contexto da classe EstadoSimulacao.
 * @param lista lista usado pelo método para cumprir a responsabilidade descrita.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private static <T> List<T> copiarListaSemNulls(List<T> lista, String nome) {
        if (lista == null) {
            throw new IllegalArgumentException("EstadoSimulacao: " + nome + " null");
        }
        List<T> copia = new ArrayList<>();
        for (T elemento : lista) {
            if (elemento == null) {
                throw new IllegalArgumentException("EstadoSimulacao: " + nome + " contem null");
            }
            copia.add(elemento);
        }
        return copia;
    }
}
