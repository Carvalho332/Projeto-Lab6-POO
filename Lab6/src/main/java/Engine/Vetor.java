package Engine;

/**
 * Responsabilidade: representar um vetor em R2 e operações vetoriais.
 *
 * @inv as componentes são valores finitos.
 */
public class Vetor {
    private final double x;
    private final double y;

    public Vetor(double x, double y) {
        this.x = x;
        this.y = y;
        verificaInvariante();
    }

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
        return Math.sqrt(modulo2());
    }

    public double modulo2() {
        return x * x + y * y;
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
        if (den <= Geometria.EPS) {
            throw new IllegalArgumentException("Vetor.cosineSimilarity: vetor nulo");
        }
        return produtoInterno(other) / den;
    }

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
        if (!Geometria.finito(d)) {
            throw new IllegalArgumentException("Vetor.mult: escalar invalido");
        }
        return new Vetor(x * d, y * d);
    }

    private void verificaInvariante() {
        if (!Geometria.finito(x) || !Geometria.finito(y)) {
            throw new IllegalArgumentException("Vetor:iv");
        }
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}
