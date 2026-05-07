package Engine;

/**
 * Responsabilidade: representar um quadrado definido por quatro vértices.
 */
public class Quadrado extends Poligono {
    private static final double EPS = 1e-9;

    public Quadrado(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    private void verificaInvariante() {
        if (getNumeroVertices() != 4) {
            throw new IllegalArgumentException("Quadrado:iv");
        }

        Ponto a = getVertice(0);
        Ponto b = getVertice(1);
        Ponto c = getVertice(2);
        Ponto d = getVertice(3);

        double ab = a.dist(b);
        double bc = b.dist(c);
        double cd = c.dist(d);
        double da = d.dist(a);
        double ac = a.dist(c);
        double bd = b.dist(d);

        if (Math.abs(ab - bc) >= EPS ||
                Math.abs(bc - cd) >= EPS ||
                Math.abs(cd - da) >= EPS ||
                Math.abs(ac - bd) >= EPS) {
            throw new IllegalArgumentException("Quadrado:iv");
        }
    }
}
