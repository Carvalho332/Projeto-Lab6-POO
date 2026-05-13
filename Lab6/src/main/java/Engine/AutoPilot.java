package Engine;

/**
 * Responsabilidade: calcular tempo e velocidade vetorial compensando a corrente.
 *
 * @inv start != null && finish != null
 */
public class AutoPilot {
    private final Ponto start;
    private final Ponto finish;

    /**
     * Responsabilidade: criar um piloto automático com ponto de partida e de chegada.
     * @param start ponto de partida
     * @param finish ponto de chegada
     */
    public AutoPilot(Ponto start, Ponto finish) {
        if (start == null || finish == null) {
            throw new IllegalArgumentException("AutoPilot: pontos null");
        }
        this.start = start;
        this.finish = finish;
    }

    /**
     * Responsabilidade: obter o ponto de partida.
     * @return start
     */
    public Ponto getStart() {
        return start;
    }


    /**
     * Responsabilidade: obter o ponto de chegada.
     * @return finish
     */
    public Ponto getFinish() {
        return finish;
    }

    /**
     * Calcula a velocidade vetorial própria do navio necessária para que o deslocamento
     * resultante, depois de somada a corrente, leve de start a finish no tempo t.
     *
     * @param w velocidade da corrente
     * @param t tempo de viagem
     * @return velocidade vetorial própria do navio
     */
    public Vetor speed(Vetor w, double t) {
        if (w == null) {
            throw new IllegalArgumentException("AutoPilot.speed: corrente null");
        }
        if (t <= 0.0) {
            throw new IllegalArgumentException("AutoPilot.speed: tempo invalido");
        }
        Vetor r = new Vetor(finish).sub(new Vetor(start));
        return r.mult(1.0 / t).sub(w);
    }

    /**
     * Calcula o tempo necessário para percorrer o segmento com a velocidade linear pretendida.
     *
     * @param vl velocidade linear constante
     * @return tempo de viagem
     */
    public double time(double vl) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("AutoPilot.time: velocidade invalida");
        }
        Vetor r = new Vetor(finish).sub(new Vetor(start));
        return r.modulo() / vl;
    }
}
