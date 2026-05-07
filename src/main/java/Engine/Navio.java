package Engine;

/**
 * Responsabilidade: representar um navio ativo numa simulação.
 *
 * @inv codigoViagem != null && destino != null && velocidadeLinear > 0 && rotaAtual != null
 */
public class Navio {
    private static final double EPS = 1e-9;

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

    public Ponto proximaPosicao() {
        if (estado != EstadoNavio.EM_MOVIMENTO) {
            return posicaoAtual;
        }
        return rotaAtual.position(velocidadeLinear, tempoNaRota + 1.0);
    }

    public void avancar() {
        if (estado == EstadoNavio.CHEGOU || estado == EstadoNavio.EM_ESPERA) {
            return;
        }

        tempoNaRota += 1.0;
        posicaoAtual = rotaAtual.position(velocidadeLinear, tempoNaRota);

        if (chegouDestino()) {
            posicaoAtual = destino.getPosicao();
            estado = EstadoNavio.CHEGOU;
        }
    }

    public void esperar() {
        if (estado == EstadoNavio.EM_MOVIMENTO) {
            estado = EstadoNavio.EM_ESPERA;
            mostrarCirculoColisao = true;
        }
    }

    public void retomar() {
        if (estado == EstadoNavio.EM_ESPERA) {
            estado = EstadoNavio.EM_MOVIMENTO;
            mostrarCirculoColisao = false;
        }
    }

    public boolean chegouDestino() {
        return posicaoAtual.dist(destino.getPosicao()) < EPS ||
                tempoNaRota >= rotaAtual.time(velocidadeLinear) - EPS;
    }

    public void definirRotaAtual(Route novaRota) {
        if (novaRota == null) {
            throw new IllegalArgumentException("Navio.definirRotaAtual: rota null");
        }
        this.rotaAtual = novaRota;
        this.tempoNaRota = 0.0;
        this.posicaoAtual = novaRota.getInicio();
    }

    public void limparSinalizacaoColisao() {
        this.mostrarCirculoColisao = false;
    }

    /**
     * Devolve um ponto muito próximo que indica a direção atual do navio na rota.
     *
     * @return ponto usado pelo GUI para orientar o desenho do navio
     */
    public Ponto getPontoDirecao() {
        double tempoTotal = rotaAtual.time(velocidadeLinear);

        if (tempoNaRota >= tempoTotal - EPS) {
            double tAnterior = Math.max(0.0, tempoTotal - 0.01);
            return rotaAtual.position(velocidadeLinear, tAnterior);
        }

        double tSeguinte = Math.min(tempoTotal, tempoNaRota + 0.01);
        Ponto seguinte = rotaAtual.position(velocidadeLinear, tSeguinte);

        if (posicaoAtual.dist(seguinte) < EPS && tempoNaRota > 0.0) {
            double tAnterior = Math.max(0.0, tempoNaRota - 0.01);
            return rotaAtual.position(velocidadeLinear, tAnterior);
        }

        return seguinte;
    }

    /**
     * Mantido para compatibilidade com o GUI já existente.
     *
     * @return ponto usado para indicar a direção atual do navio
     */
    public Ponto getProximoPonto() {
        return getPontoDirecao();
    }
}

