package Engine;

/**
 * DTO imutável com a informação de uma viagem em espera necessária ao GUI.
 *
 * @version 2026-05-08
 * @inv tempoSaida >= 0 && destino != null && !destino.isBlank() && velocidadeLinear > 0
 */
public class InfoViagem {
    private final int tempoSaida;
    private final String destino;
    private final double velocidadeLinear;

    public InfoViagem(int tempoSaida, String destino, double velocidadeLinear) {
        if (tempoSaida < 0 || destino == null || destino.isBlank() || velocidadeLinear <= 0.0) {
            throw new IllegalArgumentException("InfoViagem:iv");
        }
        this.tempoSaida = tempoSaida;
        this.destino = destino;
        this.velocidadeLinear = velocidadeLinear;
    }

    public InfoViagem(Viagem viagem) {
        if (viagem == null) {
            throw new IllegalArgumentException("InfoViagem: viagem null");
        }
        this.tempoSaida = viagem.getTempoSaida();
        this.destino = viagem.getDestino().getNome();
        this.velocidadeLinear = viagem.getVelocidadeLinear();
    }

    public int getTempoSaida() {
        return tempoSaida;
    }

    public String getDestino() {
        return destino;
    }

    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }
}
