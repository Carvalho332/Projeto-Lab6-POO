package Engine;

/**
 * Responsabilidade: agrupar operações geométricas auxiliares usadas pelo Engine.
 *
 * @version 2026-05-08
 */
public final class Geometria {
    public static final double EPS = 1e-9;

    private Geometria() {
    }

    public static boolean finito(double valor) {
        return Double.isFinite(valor);
    }

    public static boolean iguais(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    public static boolean iguais(Ponto a, Ponto b) {
        return a != null && b != null &&
                Math.abs(a.getX() - b.getX()) < EPS &&
                Math.abs(a.getY() - b.getY()) < EPS;
    }

    public static double distancia2(Ponto a, Ponto b) {
        validarPonto(a, "a");
        validarPonto(b, "b");
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return dx * dx + dy * dy;
    }

    public static double distancia(Ponto a, Ponto b) {
        return Math.sqrt(distancia2(a, b));
    }

    public static boolean pontoPertenceAoSegmento(Ponto p, Ponto a, Ponto b) {
        validarPonto(p, "p");
        validarPonto(a, "a");
        validarPonto(b, "b");

        double cross = (p.getY() - a.getY()) * (b.getX() - a.getX())
                - (p.getX() - a.getX()) * (b.getY() - a.getY());
        if (Math.abs(cross) > EPS) {
            return false;
        }

        double dot = (p.getX() - a.getX()) * (b.getX() - a.getX())
                + (p.getY() - a.getY()) * (b.getY() - a.getY());
        if (dot < -EPS) {
            return false;
        }

        double len2 = distancia2(a, b);
        return dot <= len2 + EPS;
    }

    public static double parametroNoSegmento(Ponto p, Ponto a, Ponto b) {
        validarPonto(p, "p");
        validarPonto(a, "a");
        validarPonto(b, "b");

        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double len2 = dx * dx + dy * dy;

        if (len2 <= EPS) {
            return 0.0;
        }

        return ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / len2;
    }

    public static double produtoInterno(Ponto a, Ponto b, Ponto c) {
        validarPonto(a, "a");
        validarPonto(b, "b");
        validarPonto(c, "c");
        double abx = b.getX() - a.getX();
        double aby = b.getY() - a.getY();
        double bcx = c.getX() - b.getX();
        double bcy = c.getY() - b.getY();
        return abx * bcx + aby * bcy;
    }

    private static void validarPonto(Ponto p, String nome) {
        if (p == null) {
            throw new IllegalArgumentException("Geometria: ponto " + nome + " null");
        }
    }
}
