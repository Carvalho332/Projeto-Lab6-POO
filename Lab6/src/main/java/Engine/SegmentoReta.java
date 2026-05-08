package Engine;

/**
 * Responsabilidade: representar um segmento de reta definido por dois pontos.
 *
 * @inv os extremos são diferentes.
 */
public class SegmentoReta {
    private final Ponto a;
    private final Ponto b;

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

    public double comprimento() {
        return a.dist(b);
    }

    public boolean contem(Ponto p) {
        return Geometria.pontoPertenceAoSegmento(p, a, b);
    }

    public Ponto intersect(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("SegmentoReta.intersect: vetor null");
        }
        return intersect(new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(v.getX(), v.getY())));
    }

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
        double rxs = produtoVetorial(rX, rY, sX, sY);
        double qpx = cx - ax;
        double qpy = cy - ay;
        double qpxr = produtoVetorial(qpx, qpy, rX, rY);

        if (Math.abs(rxs) <= Geometria.EPS) {
            return intersecaoSegmentosParalelos(ax, ay, rX, rY, sX, sY, qpx, qpy, qpxr);
        }

        double t = produtoVetorial(qpx, qpy, sX, sY) / rxs;
        double u = produtoVetorial(qpx, qpy, rX, rY) / rxs;

        if (!parametroDentroDoSegmento(t) || !parametroDentroDoSegmento(u)) {
            return null;
        }

        return pontoNoParametro(ax, ay, rX, rY, t);
    }

    private Ponto intersecaoSegmentosParalelos(double ax, double ay, double rX, double rY,
                                               double sX, double sY, double qpx, double qpy,
                                               double qpxr) {
        if (Math.abs(qpxr) > Geometria.EPS) {
            return null;
        }

        double rr = rX * rX + rY * rY;
        if (rr <= Geometria.EPS * Geometria.EPS) {
            return null;
        }

        double t0 = (qpx * rX + qpy * rY) / rr;
        double t1 = t0 + (sX * rX + sY * rY) / rr;
        double lo = Math.max(0.0, Math.min(t0, t1));
        double hi = Math.min(1.0, Math.max(t0, t1));

        if (hi + Geometria.EPS < lo) {
            return null;
        }

        return pontoNoParametro(ax, ay, rX, rY, lo);
    }

    private double produtoVetorial(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }

    private boolean parametroDentroDoSegmento(double t) {
        return t >= -Geometria.EPS && t <= 1.0 + Geometria.EPS;
    }

    private Ponto pontoNoParametro(double ax, double ay, double rX, double rY, double t) {
        return new Ponto(ax + t * rX, ay + t * rY);
    }

    private void verificaInvariante() {
        if (a.dist(b) <= Geometria.EPS) {
            throw new IllegalArgumentException("SegmentoReta:iv");
        }
    }

    @Override
    public String toString() {
        return "sr(" + a + "; " + b + ")";
    }
}
