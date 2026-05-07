package Engine;

/**
 * Responsabilidade: representar um ponto no plano R2.
 *
 * @inv as coordenadas são valores finitos.
 */
public class Ponto {
    private static final double EPS = 1e-9;

    private final double x;
    private final double y;

    /**
     * Cria um ponto a partir das suas coordenadas.
     *
     * @param x coordenada x
     * @param y coordenada y
     */
    public Ponto(double x, double y) {
        this.x = x;
        this.y = y;
        verificaInvariante();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Calcula a distância euclidiana entre este ponto e outro.
     *
     * @param other outro ponto
     * @return distância entre os dois pontos
     */
    public double dist(Ponto other) {
        if (other == null) {
            throw new IllegalArgumentException("Ponto.dist: other nao pode ser null");
        }
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean igual(Ponto other) {
        return other != null && dist(other) < EPS;
    }

    private void verificaInvariante() {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Ponto:iv");
        }
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
