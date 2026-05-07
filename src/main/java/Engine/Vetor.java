package Engine;

/**
 * Responsabilidade: representar um vetor em R2 e operações vetoriais.
 *
 * @inv as componentes são valores finitos.
 */
public class Vetor {
    private static final double EPS = 1e-9;

    private final double x;
    private final double y;

    public Vetor(double x, double y) {
        this.x = x;
        this.y = y;
        verificaInvariante();
    }

    /**
     * Cria o vetor posição correspondente ao ponto dado.
     *
     * @param p ponto usado como extremidade do vetor com origem em (0,0)
     */
    public Vetor(Ponto p) {
        if (p == null) {
            throw new IllegalArgumentException("Vetor: ponto nao pode ser null");
        }
        this.x = p.getX();
        this.y = p.getY();
        verificaInvariante();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double modulo() {
        return Math.sqrt(x * x + y * y);
    }

    public double produtoInterno(Vetor other) {
        if (other == null) {
            throw new IllegalArgumentException("Vetor.produtoInterno: other nao pode ser null");
        }
        return x * other.x + y * other.y;
    }

    public double cosineSimilarity(Vetor other) {
        if (other == null) {
            throw new IllegalArgumentException("Vetor.cosineSimilarity: other nao pode ser null");
        }
        double den = modulo() * other.modulo();
        if (den <= EPS) {
            throw new IllegalArgumentException("Vetor.cosineSimilarity: vetor nulo");
        }
        return produtoInterno(other) / den;
    }

    /**
     * Interseção entre este vetor, interpretado como segmento da origem até à sua extremidade,
     * e um segmento de reta.
     *
     * @param s segmento de reta
     * @return ponto de interseção ou null
     */
    public Ponto intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Vetor.intersect: segmento nao pode ser null");
        }
        return new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(x, y)).intersect(s);
    }

    public Vetor sub(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("Vetor.sub: v nao pode ser null");
        }
        return new Vetor(x - v.x, y - v.y);
    }

    public Vetor add(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("Vetor.add: v nao pode ser null");
        }
        return new Vetor(x + v.x, y + v.y);
    }

    public Vetor mult(double d) {
        return new Vetor(x * d, y * d);
    }

    private void verificaInvariante() {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Vetor:iv");
        }
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}
