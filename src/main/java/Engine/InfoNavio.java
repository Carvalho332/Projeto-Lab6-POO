package Engine;

public class InfoNavio {
    private final String codigoViagem;
    private final Ponto posicao;
    private final EstadoNavio estado;
    private final boolean mostrarCirculoColisao;

    private final Ponto proximoPonto;

    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado, boolean mostrarCirculoColisao) {
        if (codigoViagem == null || posicao == null || estado == null) {
            throw new IllegalArgumentException("InfoNavio:iv");
        }
        this.codigoViagem = codigoViagem;
        this.posicao = posicao;
        this.estado = estado;
        this.mostrarCirculoColisao = mostrarCirculoColisao;
        this.proximoPonto = null;
    }

    public InfoNavio(Navio navio) {
        this.codigoViagem = navio.getCodigoViagem();
        this.posicao = navio.getPosicaoAtual();
        this.estado = navio.getEstado();
        this.mostrarCirculoColisao = navio.deveMostrarCirculoColisao();
        this.proximoPonto = navio.getProximoPonto();
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
}