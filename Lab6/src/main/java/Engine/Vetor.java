package Engine;

/**
 * Responsabilidade: representar um vetor bidimensional e fornecer operações vetoriais usadas no cálculo de movimento.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv as coordenadas são valores reais finitos.
 */
public class Vetor {
    private final double x;
    private final double y;

    /**
 * Responsabilidade: construir uma instância de Vetor, validando os dados recebidos para preservar os invariantes.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    public Vetor(double x, double y) {
        this.x = x;
        this.y = y;
        verificaInvariante();
    }

    /**
 * Responsabilidade: construir uma instância de Vetor, validando os dados recebidos para preservar os invariantes.
 * @param p ponto analisado, acrescentado ou convertido.
 */
    public Vetor(Ponto p) {
        if (p == null) {
            throw new IllegalArgumentException("Vetor: ponto nao pode ser null");
        }
        this.x = p.getX();
        this.y = p.getY();
        verificaInvariante();
    }

    /**
 * Responsabilidade: devolver x associado à instância atual.
 * @return valor real resultante do cálculo.
 */
    public double getX() {
        return x;
    }

    /**
 * Responsabilidade: devolver y associado à instância atual.
 * @return valor real resultante do cálculo.
 */
    public double getY() {
        return y;
    }

    /**
 * Responsabilidade: calcular o comprimento do vetor.
 * @return valor real resultante do cálculo.
 */
    public double modulo() {
        return Math.sqrt(modulo2());
    }

    /**
 * Responsabilidade: calcular o quadrado do comprimento do vetor sem raiz quadrada.
 * @return valor real resultante do cálculo.
 */
    public double modulo2() {
        return x * x + y * y;
    }

    /**
 * Responsabilidade: realizar a operação produto interno no contexto da classe Vetor.
 * @param other other usado pelo método para cumprir a responsabilidade descrita.
 * @return valor real resultante do cálculo.
 */
    public double produtoInterno(Vetor other) {
        if (other == null) {
            throw new IllegalArgumentException("Vetor.produtoInterno: other nao pode ser null");
        }
        return x * other.x + y * other.y;
    }

    /**
 * Responsabilidade: realizar a operação cosine similarity no contexto da classe Vetor.
 * @param other other usado pelo método para cumprir a responsabilidade descrita.
 * @return valor real resultante do cálculo.
 */
    public double cosineSimilarity(Vetor other) {
        if (other == null) {
            throw new IllegalArgumentException("Vetor.cosineSimilarity: other nao pode ser null");
        }
        double den = modulo() * other.modulo();
        if (den <= Geometria.EPS) {
            throw new IllegalArgumentException("Vetor.cosineSimilarity: vetor nulo");
        }
        return produtoInterno(other) / den;
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Vetor.intersect: segmento nao pode ser null");
        }
        return new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(x, y)).intersect(s);
    }

    /**
 * Responsabilidade: subtrair outro vetor a este vetor.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 * @return vetor resultante da operação.
 */
    public Vetor sub(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("Vetor.sub: v nao pode ser null");
        }
        return new Vetor(x - v.x, y - v.y);
    }

    /**
 * Responsabilidade: somar este vetor com outro vetor.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 * @return vetor resultante da operação.
 */
    public Vetor add(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("Vetor.add: v nao pode ser null");
        }
        return new Vetor(x + v.x, y + v.y);
    }

    /**
 * Responsabilidade: multiplicar este vetor por um escalar.
 * @param d d usado pelo método para cumprir a responsabilidade descrita.
 * @return vetor resultante da operação.
 */
    public Vetor mult(double d) {
        if (!Geometria.finito(d)) {
            throw new IllegalArgumentException("Vetor.mult: escalar invalido");
        }
        return new Vetor(x * d, y * d);
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Vetor.
 */
    private void verificaInvariante() {
        if (!Geometria.finito(x) || !Geometria.finito(y)) {
            throw new IllegalArgumentException("Vetor:iv");
        }
    }

    /**
 * Responsabilidade: produzir uma representação textual estável para debug e testes.
 * @return texto formatado ou identificador pedido.
 */
    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}
