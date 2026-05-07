package Engine;

/**
 * Responsabilidade: representar uma viagem agendada num porto.
 *
 * @inv tempoSaida >= 0 && destino != null && velocidadeLinear > 0
 */
public class Viagem {
    private final int tempoSaida;
    private final Porto destino;
    private final double velocidadeLinear;

    public Viagem(int tempoSaida, Porto destino, double velocidadeLinear) {
        if (tempoSaida < 0 || destino == null || velocidadeLinear <= 0.0) {
            throw new IllegalArgumentException("Viagem:iv");
        }
        this.tempoSaida = tempoSaida;
        this.destino = destino;
        this.velocidadeLinear = velocidadeLinear;
    }

    public int getTempoSaida() {
        return tempoSaida;
    }

    public Porto getDestino() {
        return destino;
    }

    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }
}
