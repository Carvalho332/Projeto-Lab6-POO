package Engine;

import java.util.Locale;

/**
 * Responsabilidade: representar um ponto no plano R2.
 *
 * @inv as coordenadas são valores finitos.
 */
public class Ponto {
    private final double x;
    private final double y;

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

    public double dist(Ponto other) {
        if (other == null) {
            throw new IllegalArgumentException("Ponto.dist: other nao pode ser null");
        }
        return Geometria.distancia(this, other);
    }

    public boolean igual(Ponto other) {
        return Geometria.iguais(this, other);
    }

    private void verificaInvariante() {
        if (!Geometria.finito(x) || !Geometria.finito(y)) {
            throw new IllegalArgumentException("Ponto:iv");
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2f, %.2f)", x, y);
    }
}
