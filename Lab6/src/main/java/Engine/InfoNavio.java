package Engine;

/**
 * DTO imutável com a informação de um navio necessária para o GUI.
 *
 * @version 2026-05-08
 * @inv codigoViagem != null && posicao != null && estado != null
 */
public class InfoNavio {
    private final String codigoViagem;
    private final Ponto posicao;
    private final EstadoNavio estado;
    private final boolean mostrarCirculoColisao;
    private final Ponto proximoPonto;
    private final Vetor velocidadeVetorial;

    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado, boolean mostrarCirculoColisao) {
        this(codigoViagem, posicao, estado, mostrarCirculoColisao, null, null);
    }

    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado,
                     boolean mostrarCirculoColisao, Ponto proximoPonto, Vetor velocidadeVetorial) {
        if (codigoViagem == null || codigoViagem.isBlank() || posicao == null || estado == null) {
            throw new IllegalArgumentException("InfoNavio:iv");
        }
        this.codigoViagem = codigoViagem;
        this.posicao = posicao;
        this.estado = estado;
        this.mostrarCirculoColisao = mostrarCirculoColisao;
        this.proximoPonto = proximoPonto;
        this.velocidadeVetorial = velocidadeVetorial;
    }

    public InfoNavio(Navio navio) {
        this(navio, new Vetor(0.0, 0.0));
    }

    public InfoNavio(Navio navio, Vetor corrente) {
        if (navio == null || corrente == null) {
            throw new IllegalArgumentException("InfoNavio: argumentos invalidos");
        }
        this.codigoViagem = navio.getCodigoViagem();
        this.posicao = navio.getPosicaoAtual();
        this.estado = navio.getEstado();
        this.mostrarCirculoColisao = navio.deveMostrarCirculoColisao();
        this.proximoPonto = navio.getProximoPonto();
        this.velocidadeVetorial = navio.getVelocidadeVetorial(corrente);
    }

    public String getCodigoViagem() {
        return codigoViagem;
    }

    public Ponto getPosicao() {
        return posicao;
    }

    public EstadoNavio getEstado() {
        return estado;
    }

    public boolean deveMostrarCirculoColisao() {
        return mostrarCirculoColisao;
    }

    public Ponto getProximoPonto() {
        return proximoPonto;
    }

    public Vetor getVelocidadeVetorial() {
        return velocidadeVetorial;
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

    public boolean temVelocidadeVetorial() {
        return velocidadeVetorial != null;
    }
}
