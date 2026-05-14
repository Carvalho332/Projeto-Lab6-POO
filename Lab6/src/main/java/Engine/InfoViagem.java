package Engine;

/**
 * Responsabilidade: transportar para o GUI os dados simplificados de uma viagem em espera.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os dados transportados não expõem listas internas modificáveis.
 */
public class InfoViagem {
    private final int tempoSaida;
    private final String destino;
    private final double velocidadeLinear;

    /**
 * Responsabilidade: construir uma instância de InfoViagem, validando os dados recebidos para preservar os invariantes.
 * @param tempoSaida instante em que a viagem deve sair do porto.
 * @param destino porto de chegada pretendido.
 * @param velocidadeLinear velocidade linear pretendida ao longo da rota.
 */
    public InfoViagem(int tempoSaida, String destino, double velocidadeLinear) {
        if (tempoSaida < 0 || destino == null || destino.isBlank() || velocidadeLinear <= 0.0) {
            throw new IllegalArgumentException("InfoViagem:iv");
        }
        this.tempoSaida = tempoSaida;
        this.destino = destino;
        this.velocidadeLinear = velocidadeLinear;
    }

    /**
 * Responsabilidade: construir uma instância de InfoViagem, validando os dados recebidos para preservar os invariantes.
 * @param viagem viagem programada ou apresentada.
 */
    public InfoViagem(Viagem viagem) {
        if (viagem == null) {
            throw new IllegalArgumentException("InfoViagem: viagem null");
        }
        this.tempoSaida = viagem.getTempoSaida();
        this.destino = viagem.getDestino().getNome();
        this.velocidadeLinear = viagem.getVelocidadeLinear();
    }

    /**
 * Responsabilidade: devolver tempo saida associado à instância atual.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getTempoSaida() {
        return tempoSaida;
    }

    /**
 * Responsabilidade: devolver destino associado à instância atual.
 * @return texto formatado ou identificador pedido.
 */
    public String getDestino() {
        return destino;
    }

    /**
 * Responsabilidade: devolver velocidade linear associado à instância atual.
 * @return valor real resultante do cálculo.
 */
    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }
}
