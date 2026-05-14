package Engine;

/**
 * Responsabilidade: representar uma viagem programada a partir de um porto para um destino com velocidade linear.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class Viagem {
    private final int tempoSaida;
    private final Porto destino;
    private final double velocidadeLinear;

    /**
 * Responsabilidade: construir uma instância de Viagem, validando os dados recebidos para preservar os invariantes.
 * @param tempoSaida instante em que a viagem deve sair do porto.
 * @param destino porto de chegada pretendido.
 * @param velocidadeLinear velocidade linear pretendida ao longo da rota.
 */
    public Viagem(int tempoSaida, Porto destino, double velocidadeLinear) {
        if (tempoSaida < 0 || destino == null || velocidadeLinear <= 0.0) {
            throw new IllegalArgumentException("Viagem:iv");
        }
        this.tempoSaida = tempoSaida;
        this.destino = destino;
        this.velocidadeLinear = velocidadeLinear;
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
 * @return objeto resultante da operação.
 */
    public Porto getDestino() {
        return destino;
    }

    /**
 * Responsabilidade: devolver velocidade linear associado à instância atual.
 * @return valor real resultante do cálculo.
 */
    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }

    /**
 * Responsabilidade: produzir uma representação textual estável para debug e testes.
 * @return texto formatado ou identificador pedido.
 */
    @Override
    public String toString() {
        return "Viagem{t=" + tempoSaida + ", destino=" + destino.getNome() + ", vl=" + velocidadeLinear + "}";
    }
}
