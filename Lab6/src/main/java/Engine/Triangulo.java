package Engine;

/**
 * Responsabilidade: representar um triângulo definido por três vértices.
 */
public class Triangulo extends Poligono {
    private static final double EPS = 1e-9;

    public Triangulo(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    private void verificaInvariante() {
        if (getNumeroVertices() != 3) {
            throw new IllegalArgumentException("Triangulo:iv");
        }

        Ponto a = getVertice(0);
        Ponto b = getVertice(1);
        Ponto c = getVertice(2);

        double area2 = (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());

        if (Math.abs(area2) < EPS) {
            throw new IllegalArgumentException("Triangulo:iv");
        }
    }
}
