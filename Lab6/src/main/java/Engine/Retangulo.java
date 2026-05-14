package Engine;

/**
 * Responsabilidade: representar um obstáculo retangular, validando lados opostos, diagonais e ângulos.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os vértices são válidos e respeitam a forma geométrica representada.
 */
public class Retangulo extends Poligono {
    /**
 * Responsabilidade: construir uma instância de Retangulo, validando os dados recebidos para preservar os invariantes.
 * @param vertices vertices usado pelo método para cumprir a responsabilidade descrita.
 */
    public Retangulo(Ponto[] vertices) {
        super(vertices);
        verificaInvariante();
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Retangulo.
 */
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

    /**
 * Responsabilidade: realizar a operação lados opostos iguais no contexto da classe Retangulo.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param c terceiro ponto usado na validação geométrica.
 * @param d d usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean ladosOpostosIguais(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(a.dist(b) - c.dist(d)) < Geometria.EPS &&
                Math.abs(b.dist(c) - d.dist(a)) < Geometria.EPS;
    }

    /**
 * Responsabilidade: realizar a operação diagonais iguais no contexto da classe Retangulo.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param c terceiro ponto usado na validação geométrica.
 * @param d d usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean diagonaisIguais(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(a.dist(c) - b.dist(d)) < Geometria.EPS;
    }

    /**
 * Responsabilidade: realizar a operação angulos consecutivos retos no contexto da classe Retangulo.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param c terceiro ponto usado na validação geométrica.
 * @param d d usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean angulosConsecutivosRetos(Ponto a, Ponto b, Ponto c, Ponto d) {
        return Math.abs(Geometria.produtoInterno(a, b, c)) < Geometria.EPS &&
                Math.abs(Geometria.produtoInterno(b, c, d)) < Geometria.EPS;
    }
}
