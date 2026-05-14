package Engine;

/**
 * Responsabilidade: transportar para o GUI os dados necessários para apresentar um navio ativo.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 13-05-2026
 * @inv codigoViagem != null && !codigoViagem.isBlank() && posicao != null && estado != null && velocidadeLinear > 0
 */
public class InfoNavio {
    private final String codigoViagem;
    private final Ponto posicao;
    private final EstadoNavio estado;
    private final boolean mostrarCirculoColisao;
    private final Ponto proximoPonto;
    private final Vetor velocidadeVetorial;
    private final double velocidadeLinear;

    /**
     * Responsabilidade: construir a informação mínima de um navio quando a velocidade linear não é relevante para o teste.
     * @param codigoViagem código da viagem no formato definido pelo enunciado.
     * @param posicao posição atual do navio.
     * @param estado estado atual do navio.
     * @param mostrarCirculoColisao indica se o círculo de colisão deve ser mostrado.
     */
    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado, boolean mostrarCirculoColisao) {
        this(codigoViagem, posicao, estado, mostrarCirculoColisao, 1.0);
    }

    /**
     * Responsabilidade: construir a informação mínima de um navio para apresentação no GUI.
     * @param codigoViagem código da viagem no formato definido pelo enunciado.
     * @param posicao posição atual do navio.
     * @param estado estado atual do navio.
     * @param mostrarCirculoColisao indica se o círculo de colisão deve ser mostrado.
     * @param velocidadeLinear velocidade linear constante pretendida para o navio.
     */
    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado,
                     boolean mostrarCirculoColisao, double velocidadeLinear) {
        this(codigoViagem, posicao, estado, mostrarCirculoColisao, null, null, velocidadeLinear);
    }

    /**
     * Responsabilidade: construir a informação completa de um navio para apresentação no GUI.
     * @param codigoViagem código da viagem no formato definido pelo enunciado.
     * @param posicao posição atual do navio.
     * @param estado estado atual do navio.
     * @param mostrarCirculoColisao indica se o círculo de colisão deve ser mostrado.
     * @param proximoPonto ponto usado pelo GUI para orientar o desenho do navio.
     * @param velocidadeVetorial velocidade vetorial própria do navio, já compensando a corrente.
     * @param velocidadeLinear velocidade linear constante pretendida para o navio.
     */
    public InfoNavio(String codigoViagem, Ponto posicao, EstadoNavio estado,
                     boolean mostrarCirculoColisao, Ponto proximoPonto,
                     Vetor velocidadeVetorial, double velocidadeLinear) {
        if (codigoViagem == null || codigoViagem.isBlank() || posicao == null || estado == null || velocidadeLinear <= 0.0) {
            throw new IllegalArgumentException("InfoNavio:iv");
        }
        this.codigoViagem = codigoViagem;
        this.posicao = posicao;
        this.estado = estado;
        this.mostrarCirculoColisao = mostrarCirculoColisao;
        this.proximoPonto = proximoPonto;
        this.velocidadeVetorial = velocidadeVetorial;
        this.velocidadeLinear = velocidadeLinear;
    }

    /**
     * Responsabilidade: criar o DTO de um navio assumindo corrente nula.
     * @param navio navio de onde são copiados os dados de apresentação.
     */
    public InfoNavio(Navio navio) {
        this(navio, new Vetor(0.0, 0.0));
    }

    /**
     * Responsabilidade: criar o DTO de um navio incluindo a velocidade vetorial compensada pela corrente.
     * @param navio navio de onde são copiados os dados de apresentação.
     * @param corrente vetor da corrente usado para calcular a velocidade vetorial própria do navio.
     */
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
        this.velocidadeLinear = navio.getVelocidadeLinear();
    }

    /**
     * Responsabilidade: devolver o código da viagem do navio.
     * @return código da viagem.
     */
    public String getCodigoViagem() {
        return codigoViagem;
    }

    /**
     * Responsabilidade: devolver a posição atual do navio.
     * @return posição atual do navio.
     */
    public Ponto getPosicao() {
        return posicao;
    }

    /**
     * Responsabilidade: devolver o estado atual do navio.
     * @return estado atual do navio.
     */
    public EstadoNavio getEstado() {
        return estado;
    }

    /**
     * Responsabilidade: indicar se o círculo de colisão deve ser desenhado.
     * @return true se o círculo de colisão deve ser apresentado.
     */
    public boolean deveMostrarCirculoColisao() {
        return mostrarCirculoColisao;
    }

    /**
     * Responsabilidade: devolver o ponto usado para orientar o desenho do navio.
     * @return próximo ponto da rota ou null se não existir.
     */
    public Ponto getProximoPonto() {
        return proximoPonto;
    }

    /**
     * Responsabilidade: devolver a velocidade vetorial própria do navio.
     * @return velocidade vetorial ou null se não tiver sido calculada.
     */
    public Vetor getVelocidadeVetorial() {
        return velocidadeVetorial;
    }

    /**
     * Responsabilidade: devolver a velocidade linear constante pretendida para o navio.
     * @return velocidade linear do navio.
     */
    public double getVelocidadeLinear() {
        return velocidadeLinear;
    }

    /**
     * Responsabilidade: indicar se o navio representado está em movimento.
     * @return true se o navio estiver em movimento.
     */
    public boolean estaEmMovimento() {
        return estado == EstadoNavio.EM_MOVIMENTO;
    }

    /**
     * Responsabilidade: indicar se o navio representado está em espera.
     * @return true se o navio estiver em espera.
     */
    public boolean estaEmEspera() {
        return estado == EstadoNavio.EM_ESPERA;
    }

    /**
     * Responsabilidade: indicar se o navio representado chegou ao destino.
     * @return true se o navio tiver chegado ao destino.
     */
    public boolean chegou() {
        return estado == EstadoNavio.CHEGOU;
    }

    /**
     * Responsabilidade: indicar se existe velocidade vetorial para apresentar.
     * @return true se a velocidade vetorial não for null.
     */
    public boolean temVelocidadeVetorial() {
        return velocidadeVetorial != null;
    }
}
