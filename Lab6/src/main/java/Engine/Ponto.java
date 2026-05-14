package Engine;

import java.util.Locale;

/**
 * Responsabilidade: representar um ponto bidimensional usado em rotas, portos, obstáculos e posições de navios.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv as coordenadas são valores reais finitos.
 */
public class Ponto {
    private final double x;
    private final double y;

    /**
 * Responsabilidade: construir uma instância de Ponto, validando os dados recebidos para preservar os invariantes.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    public Ponto(double x, double y) {
        this.x = x;
        this.y = y;
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
 * Responsabilidade: calcular a distância euclidiana entre este ponto e outro ponto.
 * @param other other usado pelo método para cumprir a responsabilidade descrita.
 * @return distância ou comprimento calculado.
 */
    public double dist(Ponto other) {
        if (other == null) {
            throw new IllegalArgumentException("Ponto.dist: other nao pode ser null");
        }
        return Geometria.distancia(this, other);
    }

    /**
 * Responsabilidade: comparar dois pontos usando tolerância numérica.
 * @param other other usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean igual(Ponto other) {
        return Geometria.iguais(this, other);
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Ponto.
 */
    private void verificaInvariante() {
        if (!Geometria.finito(x) || !Geometria.finito(y)) {
            throw new IllegalArgumentException("Ponto:iv");
        }
    }

    /**
 * Responsabilidade: produzir uma representação textual estável para debug e testes.
 * @return texto formatado ou identificador pedido.
 */
    @Override
    public String toString() {
        return String.format(Locale.US, "(%.2f, %.2f)", x, y);
    }
}
