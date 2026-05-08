package Engine;

/**
 * Responsabilidade: representar um quadrado definido por quatro vértices.
 */
public class Quadrado extends Poligono {
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

        if (Math.abs(ab - bc) >= Geometria.EPS ||
                Math.abs(bc - cd) >= Geometria.EPS ||
                Math.abs(cd - da) >= Geometria.EPS ||
                Math.abs(a.dist(c) - b.dist(d)) >= Geometria.EPS ||
                Math.abs(Geometria.produtoInterno(a, b, c)) >= Geometria.EPS) {
            throw new IllegalArgumentException("Quadrado:iv");
        }
    }
}
