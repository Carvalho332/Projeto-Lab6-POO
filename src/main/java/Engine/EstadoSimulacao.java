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

    public EstadoSimulacao(int tempoAtual, Vetor corrente, List<InfoNavio> navios, List<InfoPorto> portos, List<Route> rotas, List<Obstaculo> obstaculos) {
        if (tempoAtual < 0 || corrente == null || navios == null || portos == null ||
                rotas == null || obstaculos == null) {
            throw new IllegalArgumentException("EstadoSimulacao:iv");
        }

        this.tempoAtual = tempoAtual;
        this.corrente = corrente;
        this.navios = new ArrayList<>(navios);
        this.portos = new ArrayList<>(portos);
        this.rotas = new ArrayList<>(rotas);
        this.obstaculos = new ArrayList<>(obstaculos);
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
}
