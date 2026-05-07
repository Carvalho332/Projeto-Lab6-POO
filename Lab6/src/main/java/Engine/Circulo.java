package Engine;

/**
 * Responsabilidade: representar um círculo definido por centro e raio.
 *
 * @inv centro != null && raio > 0
 */
public class Circulo extends Obstaculo {
    private static final double EPS = 1e-9;

    private final Ponto centro;
    private final double raio;

    public Circulo(Ponto centro, double raio) {
        if (centro == null) {
            throw new IllegalArgumentException("Circulo: centro null");
        }
        this.centro = centro;
        this.raio = raio;
        verificaInvariante();
    }

    public Ponto getCentro() {
        return centro;
    }

    public double getRaio() {
        return raio;
    }

    @Override
    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Circulo.intersect: segmento null");
        }

        double ax = s.getA().getX();
        double ay = s.getA().getY();
        double bx = s.getB().getX();
        double by = s.getB().getY();

        double dx = bx - ax;
        double dy = by - ay;

        double fx = ax - centro.getX();
        double fy = ay - centro.getY();

        double qa = dx * dx + dy * dy;
        double qb = 2.0 * (fx * dx + fy * dy);
        double qc = fx * fx + fy * fy - raio * raio;

        double delta = qb * qb - 4.0 * qa * qc;

        if (delta < -EPS) {
            return null;
        }

        Ponto[] pontos = new Ponto[2];
        int n = 0;

        if (Math.abs(delta) <= EPS) {
            double t = -qb / (2.0 * qa);
            if (t >= -EPS && t <= 1.0 + EPS) {
                pontos[n++] = new Ponto(ax + t * dx, ay + t * dy);
            }
        } else {
            double raiz = Math.sqrt(delta);
            double t1 = (-qb - raiz) / (2.0 * qa);
            double t2 = (-qb + raiz) / (2.0 * qa);

            if (t1 >= -EPS && t1 <= 1.0 + EPS) {
                pontos[n++] = new Ponto(ax + t1 * dx, ay + t1 * dy);
            }

            if (t2 >= -EPS && t2 <= 1.0 + EPS) {
                Ponto p = new Ponto(ax + t2 * dx, ay + t2 * dy);
                if (n == 0 || !p.igual(pontos[0])) {
                    pontos[n++] = p;
                }
            }
        }

        if (n == 0) {
            return null;
        }

        Ponto[] resultado = new Ponto[n];
        System.arraycopy(pontos, 0, resultado, 0, n);
        return resultado;
    }

    private void verificaInvariante() {
        if (!Double.isFinite(raio) || raio <= 0.0) {
            throw new IllegalArgumentException("Circulo:iv");
        }
    }
}
