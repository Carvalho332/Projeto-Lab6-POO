package Engine;

/**
 * Responsabilidade: representar um segmento de reta definido por dois pontos.
 *
 * @inv os extremos são diferentes.
 */
public class SegmentoReta {
    private static final double EPS = 1e-9;

    private final Ponto a;
    private final Ponto b;

    /**
     * Cria um segmento a partir de um ponto e um vetor posição.
     *
     * @param a extremo inicial
     * @param v vetor que define o segundo extremo
     */
    public SegmentoReta(Ponto a, Vetor v) {
        if (a == null || v == null) {
            throw new IllegalArgumentException("SegmentoReta: argumentos null");
        }
        this.a = a;
        this.b = new Ponto(a.getX() + v.getX(), a.getY() + v.getY());
        verificaInvariante();
    }

    public SegmentoReta(Ponto a, Ponto b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("SegmentoReta: argumentos null");
        }
        this.a = a;
        this.b = b;
        verificaInvariante();
    }

    public Ponto getA() {
        return a;
    }

    public Ponto getB() {
        return b;
    }

    /**
     * Interseção entre este segmento e um vetor interpretado como segmento da origem
     * até à extremidade do vetor.
     *
     * @param v vetor
     * @return ponto de interseção ou null
     */
    public Ponto intersect(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("SegmentoReta.intersect: vetor null");
        }
        return intersect(new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(v.getX(), v.getY())));
    }

    /**
     * Determina a interseção entre dois segmentos de reta.
     *
     * @param other outro segmento
     * @return ponto de interseção ou null
     */
    public Ponto intersect(SegmentoReta other) {
        if (other == null) {
            throw new IllegalArgumentException("SegmentoReta.intersect: other null");
        }

        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();

        double cx = other.a.getX();
        double cy = other.a.getY();
        double dx = other.b.getX();
        double dy = other.b.getY();

        double rX = bx - ax;
        double rY = by - ay;
        double sX = dx - cx;
        double sY = dy - cy;

        double rxs = rX * sY - rY * sX;
        double qpx = cx - ax;
        double qpy = cy - ay;
        double qpxr = qpx * rY - qpy * rX;

        if (Math.abs(rxs) <= EPS) {
            if (Math.abs(qpxr) > EPS) {
                return null;
            }

            double rr = rX * rX + rY * rY;
            if (rr <= EPS * EPS) {
                return null;
            }

            double t0 = (qpx * rX + qpy * rY) / rr;
            double t1 = t0 + (sX * rX + sY * rY) / rr;

            double lo = Math.max(0.0, Math.min(t0, t1));
            double hi = Math.min(1.0, Math.max(t0, t1));

            if (hi + EPS < lo) {
                return null;
            }

            return new Ponto(ax + lo * rX, ay + lo * rY);
        }

        double t = (qpx * sY - qpy * sX) / rxs;
        double u = (qpx * rY - qpy * rX) / rxs;

        if (t < -EPS || t > 1.0 + EPS || u < -EPS || u > 1.0 + EPS) {
            return null;
        }

        return new Ponto(ax + t * rX, ay + t * rY);
    }

    private void verificaInvariante() {
        if (a.dist(b) <= EPS) {
            throw new IllegalArgumentException("SegmentoReta:iv");
        }
    }

    @Override
    public String toString() {
        return "sr(" + a + "; " + b + ")";
    }
}
