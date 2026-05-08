package Engine;

/**
 * Responsabilidade: representar um retângulo definido por quatro vértices.
 */
public class Retangulo extends Poligono {
    public Retangulo(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    private void verificaInvariante() {
        if (getNumeroVertices() != 4) {
            throw new IllegalArgumentException("Retangulo:iv");
        }

        Ponto a = getVertice(0);
        Ponto b = getVertice(1);
        Ponto c = getVertice(2);
        Ponto d = getVertice(3);

        if (!ladosOpostosIguais(a, b, c, d) || !diagonaisIguais(a, b, c, d) ||
                !angulosConsecutivosRetos(a, b, c, d)) {
            throw new IllegalArgumentException("Retangulo:iv");
        }
    }

    private boolean ladosOpostosIguais(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(a.dist(b) - c.dist(d)) < Geometria.EPS &&
                Math.abs(b.dist(c) - d.dist(a)) < Geometria.EPS;
    }

    private boolean diagonaisIguais(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(a.dist(c) - b.dist(d)) < Geometria.EPS;
    }

    private boolean angulosConsecutivosRetos(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(Geometria.produtoInterno(a, b, c)) < Geometria.EPS &&
                Math.abs(Geometria.produtoInterno(b, c, d)) < Geometria.EPS;
    }
}
