package Engine;

/**
 * Responsabilidade: representar um obstáculo quadrado, validando as propriedades geométricas dos seus lados.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os vértices são válidos e respeitam a forma geométrica representada.
 */
public class Quadrado extends Poligono {
    /**
 * Responsabilidade: construir uma instância de Quadrado, validando os dados recebidos para preservar os invariantes.
 * @param vertices vertices usado pelo método para cumprir a responsabilidade descrita.
 */
    public Quadrado(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Quadrado.
 */
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
