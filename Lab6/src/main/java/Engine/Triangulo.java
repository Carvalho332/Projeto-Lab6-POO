package Engine;

/**
 * Responsabilidade: representar um obstáculo triangular, garantindo que os três vértices não são colineares.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os vértices são válidos e respeitam a forma geométrica representada.
 */
public class Triangulo extends Poligono {
    private static final double EPS = 1e-9;

    /**
 * Responsabilidade: construir uma instância de Triangulo, validando os dados recebidos para preservar os invariantes.
 * @param vertices vertices usado pelo método para cumprir a responsabilidade descrita.
 */
    public Triangulo(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Triangulo.
 */
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
