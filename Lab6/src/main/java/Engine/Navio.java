package Engine;

/**
 * Responsabilidade: representar um navio ativo numa simulação.
 *
 * @inv codigoViagem != null && destino != null && velocidadeLinear > 0 && rotaAtual != null
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

    public String getCodigoViagem() {
        return codigoViagem;
    }

    public Ponto getPosicaoAtual() {
        return posicaoAtual;
    }

    public Porto getDestino() {
        return destino;
    }

    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }

    public Route getRotaAtual() {
        return rotaAtual;
    }

    public EstadoNavio getEstado() {
        return estado;
    }

    public boolean deveMostrarCirculoColisao() {
        return mostrarCirculoColisao;
    }

    public boolean estaEmMovimento() {
        return estado == EstadoNavio.EM_MOVIMENTO;
    }

    public boolean estaEmEspera() {
        return estado == EstadoNavio.EM_ESPERA;
    }

    public boolean chegou() {
        return estado == EstadoNavio.CHEGOU;
    }

    public Ponto proximaPosicao() {
        if (!estaEmMovimento()) {
            return posicaoAtual;
        }
        return posicaoNoTempo(tempoNaRota + 1.0);
    }

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

    public void esperar() {
        if (estaEmMovimento()) {
            estado = EstadoNavio.EM_ESPERA;
            mostrarCirculoColisao = true;
        }
    }

    public void retomar() {
        if (estaEmEspera()) {
            estado = EstadoNavio.EM_MOVIMENTO;
            mostrarCirculoColisao = false;
        }
    }

    public boolean chegouDestino() {
        return posicaoAtual.dist(destino.getPosicao()) < Geometria.EPS ||
                tempoNaRota >= tempoTotalRota() - Geometria.EPS;
    }

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

    public void limparSinalizacaoColisao() {
        this.mostrarCirculoColisao = false;
    }

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

    public Ponto getProximoPonto() {
        return getPontoDirecao();
    }

    private double tempoTotalRota() {
        return rotaAtual.time(velocidadeLinear);
    }

    private Ponto posicaoNoTempo(double tempo) {
        return rotaAtual.position(velocidadeLinear, tempo);
    }

    private void marcarComoChegou() {
        posicaoAtual = destino.getPosicao();
        estado = EstadoNavio.CHEGOU;
        mostrarCirculoColisao = false;
    }
}
