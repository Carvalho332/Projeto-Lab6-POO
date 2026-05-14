package Engine;

/**
 * Responsabilidade: representar um navio ativo, incluindo posição, rota, destino, estado e velocidade.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv o código, a posição, o destino e a rota atual não são nulos.
 * @inv a velocidade linear é positiva.
 */
public class Navio {
    private static final double DELTA_DIRECAO = 0.01;

    private final String codigoViagem;
    private Ponto posicaoAtual;
    private final Porto destino;
    private final double velocidadeLinear;
    private Route rotaAtual;
    private EstadoNavio estado;
    private boolean mostrarCirculoColisao;
    private double tempoNaRota;

    /**
 * Responsabilidade: construir uma instância de Navio, validando os dados recebidos para preservar os invariantes.
 * @param codigoViagem identificador da viagem associado ao navio.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param viagem viagem programada ou apresentada.
 * @param rota rota analisada, percorrida ou construída pelo método.
 */
    public Navio(String codigoViagem, Porto origem, Viagem viagem, Route rota) {
        if (codigoViagem == null || codigoViagem.isBlank() ||
                origem == null || viagem == null || rota == null) {
            throw new IllegalArgumentException("Navio:iv");
        }
        if (!rota.getInicio().igual(origem.getPosicao())) {
            throw new IllegalArgumentException("Navio: rota deve começar no porto de origem");
        }

        this.codigoViagem = codigoViagem;
        this.posicaoAtual = origem.getPosicao();
        this.destino = viagem.getDestino();
        this.velocidadeLinear = viagem.getVelocidadeLinear();
        this.rotaAtual = rota;
        this.estado = EstadoNavio.EM_MOVIMENTO;
        this.mostrarCirculoColisao = false;
        this.tempoNaRota = 0.0;
    }

    /**
 * Responsabilidade: devolver codigo viagem associado à instância atual.
 * @return texto formatado ou identificador pedido.
 */
    public String getCodigoViagem() {
        return codigoViagem;
    }

    /**
 * Responsabilidade: devolver posicao atual associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getPosicaoAtual() {
        return posicaoAtual;
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
 * Responsabilidade: devolver rota atual associado à instância atual.
 * @return rota calculada ou construída pela operação.
 */
    public Route getRotaAtual() {
        return rotaAtual;
    }

    /**
 * Responsabilidade: devolver estado associado à instância atual.
 * @return objeto resultante da operação.
 */
    public EstadoNavio getEstado() {
        return estado;
    }

    /**
 * Responsabilidade: indicar se a condição deve mostrar circulo colisao se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean deveMostrarCirculoColisao() {
        return mostrarCirculoColisao;
    }

    /**
 * Responsabilidade: indicar se a condição esta em movimento se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean estaEmMovimento() {
        return estado == EstadoNavio.EM_MOVIMENTO;
    }

    /**
 * Responsabilidade: indicar se a condição esta em espera se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean estaEmEspera() {
        return estado == EstadoNavio.EM_ESPERA;
    }

    /**
 * Responsabilidade: realizar a operação chegou no contexto da classe Navio.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean chegou() {
        return estado == EstadoNavio.CHEGOU;
    }

    /**
 * Responsabilidade: realizar a operação proxima posicao no contexto da classe Navio.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto proximaPosicao() {
        if (!estaEmMovimento()) {
            return posicaoAtual;
        }
        return posicaoNoTempo(tempoNaRota + 1.0);
    }

    /**
 * Responsabilidade: realizar a operação avancar no contexto da classe Navio.
 */
    public void avancar() {
        if (!estaEmMovimento()) {
            return;
        }

        tempoNaRota += 1.0;
        posicaoAtual = posicaoNoTempo(tempoNaRota);

        if (chegouDestino()) {
            marcarComoChegou();
        }
    }

    /**
 * Responsabilidade: realizar a operação esperar no contexto da classe Navio.
 */
    public void esperar() {
        if (estaEmMovimento()) {
            estado = EstadoNavio.EM_ESPERA;
            mostrarCirculoColisao = true;
        }
    }

    /**
 * Responsabilidade: realizar a operação retomar no contexto da classe Navio.
 */
    public void retomar() {
        if (estaEmEspera()) {
            estado = EstadoNavio.EM_MOVIMENTO;
            mostrarCirculoColisao = false;
        }
    }

    /**
 * Responsabilidade: realizar a operação chegou destino no contexto da classe Navio.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean chegouDestino() {
        return posicaoAtual.dist(destino.getPosicao()) < Geometria.EPS ||
                tempoNaRota >= tempoTotalRota() - Geometria.EPS;
    }

    /**
 * Responsabilidade: realizar a operação definir rota atual no contexto da classe Navio.
 * @param novaRota nova rota usado pelo método para cumprir a responsabilidade descrita.
 */
    public void definirRotaAtual(Route novaRota) {
        if (novaRota == null) {
            throw new IllegalArgumentException("Navio.definirRotaAtual: rota null");
        }
        if (!novaRota.getInicio().igual(posicaoAtual)) {
            throw new IllegalArgumentException("Navio.definirRotaAtual: nova rota deve começar na posição atual");
        }
        this.rotaAtual = novaRota;
        this.tempoNaRota = 0.0;
    }

    /**
 * Responsabilidade: realizar a operação limpar sinalizacao colisao no contexto da classe Navio.
 */
    public void limparSinalizacaoColisao() {
        this.mostrarCirculoColisao = false;
    }

    /**
 * Responsabilidade: devolver ponto direcao associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getPontoDirecao() {
        double tempoTotal = tempoTotalRota();

        if (tempoNaRota >= tempoTotal - Geometria.EPS) {
            return posicaoNoTempo(Math.max(0.0, tempoTotal - DELTA_DIRECAO));
        }

        Ponto seguinte = posicaoNoTempo(Math.min(tempoTotal, tempoNaRota + DELTA_DIRECAO));
        if (posicaoAtual.dist(seguinte) < Geometria.EPS && tempoNaRota > 0.0) {
            return posicaoNoTempo(Math.max(0.0, tempoNaRota - DELTA_DIRECAO));
        }

        return seguinte;
    }

    /**
 * Responsabilidade: devolver velocidade vetorial associado à instância atual.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @return vetor resultante da operação.
 */
    public Vetor getVelocidadeVetorial(Vetor corrente) {
        if (corrente == null) {
            throw new IllegalArgumentException("Navio.getVelocidadeVetorial: corrente null");
        }
        if (!estaEmMovimento()) {
            return new Vetor(0.0, 0.0);
        }

        Ponto direcao = getPontoDirecao();
        if (posicaoAtual.dist(direcao) < Geometria.EPS) {
            return new Vetor(0.0, 0.0);
        }

        AutoPilot autoPilot = new AutoPilot(posicaoAtual, direcao);
        return autoPilot.speed(corrente, autoPilot.time(velocidadeLinear));
    }

    /**
 * Responsabilidade: devolver proximo ponto associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getProximoPonto() {
        return getPontoDirecao();
    }

    /**
 * Responsabilidade: indicar se a condição tempo total rota se verifica no estado atual.
 * @return tempo de percurso calculado.
 */
    private double tempoTotalRota() {
        return rotaAtual.time(velocidadeLinear);
    }

    /**
 * Responsabilidade: realizar a operação posicao no tempo no contexto da classe Navio.
 * @param tempo tempo usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    private Ponto posicaoNoTempo(double tempo) {
        return rotaAtual.position(velocidadeLinear, tempo);
    }

    /**
 * Responsabilidade: realizar a operação marcar como chegou no contexto da classe Navio.
 */
    private void marcarComoChegou() {
        posicaoAtual = destino.getPosicao();
        estado = EstadoNavio.CHEGOU;
        mostrarCirculoColisao = false;
    }
}
