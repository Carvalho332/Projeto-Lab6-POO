package Engine;

/**
 * Responsabilidade: calcular a velocidade vetorial necessária para deslocar um navio entre dois pontos, compensando a corrente.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class AutoPilot {
    private final Ponto start;
    private final Ponto finish;

    /**
 * Responsabilidade: construir uma instância de AutoPilot, validando os dados recebidos para preservar os invariantes.
 * @param start ponto inicial do percurso controlado pelo piloto automático.
 * @param finish ponto final que o piloto automático deve atingir.
 */
    public AutoPilot(Ponto start, Ponto finish) {
        if (start == null || finish == null) {
            throw new IllegalArgumentException("AutoPilot: pontos null");
        }
        this.start = start;
        this.finish = finish;
    }

    /**
 * Responsabilidade: devolver start associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getStart() {
        return start;
    }

    /**
 * Responsabilidade: devolver finish associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getFinish() {
        return finish;
    }

    /**
 * Responsabilidade: calcular a velocidade vetorial que compensa a corrente no percurso.
 * @param w vetor da corrente a compensar no cálculo da velocidade própria do navio.
 * @param t tempo disponível para percorrer o segmento.
 * @return vetor resultante da operação.
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
 * Responsabilidade: calcular o tempo de percurso usando comprimento e velocidade linear.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return tempo de percurso calculado.
 */
    public double time(double vl) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("AutoPilot.time: velocidade invalida");
        }
        Vetor r = new Vetor(finish).sub(new Vetor(start));
        return r.modulo() / vl;
    }
}
