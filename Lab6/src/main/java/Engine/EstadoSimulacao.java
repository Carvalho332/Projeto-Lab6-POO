package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO imutável que transporta para o GUI o estado relevante de uma simulação.
 */
public class EstadoSimulacao {
    private final int tempoAtual;
    private final Vetor corrente;
    private final List<InfoNavio> navios;
    private final List<InfoPorto> portos;
    private final List<Route> rotas;
    private final List<Obstaculo> obstaculos;

    public EstadoSimulacao(int tempoAtual, Vetor corrente, List<InfoNavio> navios,
                           List<InfoPorto> portos, List<Route> rotas, List<Obstaculo> obstaculos) {
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

    public int getTempoAtual() {
        return tempoAtual;
    }

    public Vetor getCorrente() {
        return corrente;
    }

    public List<InfoNavio> getNavios() {
        return Collections.unmodifiableList(navios);
    }

    public List<InfoPorto> getPortos() {
        return Collections.unmodifiableList(portos);
    }

    public List<Route> getRotas() {
        return Collections.unmodifiableList(rotas);
    }

    public List<Obstaculo> getObstaculos() {
        return Collections.unmodifiableList(obstaculos);
    }

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
