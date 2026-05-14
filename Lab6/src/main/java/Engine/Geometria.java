package Engine;

/**
 * Responsabilidade: centralizar operações geométricas auxiliares usadas pelas classes do Engine.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public final class Geometria {
    public static final double EPS = 1e-9;

    /**
 * Responsabilidade: construir uma instância de Geometria, validando os dados recebidos para preservar os invariantes.
 */
    private Geometria() {
    }

    /**
 * Responsabilidade: realizar a operação finito no contexto da classe Geometria.
 * @param valor valor numérico a validar, formatar ou converter.
 * @return true se a condição se verificar; false caso contrário.
 */
    public static boolean finito(double valor) {
        return Double.isFinite(valor);
    }

    /**
 * Responsabilidade: realizar a operação iguais no contexto da classe Geometria.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return true se a condição se verificar; false caso contrário.
 */
    public static boolean iguais(double a, double b) {
        return Math.abs(a - b) < EPS;
    }

    /**
 * Responsabilidade: realizar a operação iguais no contexto da classe Geometria.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return true se a condição se verificar; false caso contrário.
 */
    public static boolean iguais(Ponto a, Ponto b) {
        return a != null && b != null && Math.abs(a.getX() - b.getX()) < EPS && Math.abs(a.getY() - b.getY()) < EPS;
    }

    /**
 * Responsabilidade: realizar a operação distancia2 no contexto da classe Geometria.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return distância ou comprimento calculado.
 */
    public static double distancia2(Ponto a, Ponto b) {
        validarPonto(a, "a");
        validarPonto(b, "b");
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return dx * dx + dy * dy;
    }

    /**
 * Responsabilidade: realizar a operação distancia no contexto da classe Geometria.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return distância ou comprimento calculado.
 */
    public static double distancia(Ponto a, Ponto b) {
        return Math.sqrt(distancia2(a, b));
    }

    /**
 * Responsabilidade: realizar a operação ponto pertence ao segmento no contexto da classe Geometria.
 * @param p ponto analisado, acrescentado ou convertido.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return true se a condição se verificar; false caso contrário.
 */
    public static boolean pontoPertenceAoSegmento(Ponto p, Ponto a, Ponto b) {
        validarPonto(p, "p");
        validarPonto(a, "a");
        validarPonto(b, "b");

        double cross = (p.getY() - a.getY()) * (b.getX() - a.getX()) - (p.getX() - a.getX()) * (b.getY() - a.getY());
        if (Math.abs(cross) > EPS) {
            return false;
        }

        double dot = (p.getX() - a.getX()) * (b.getX() - a.getX()) + (p.getY() - a.getY()) * (b.getY() - a.getY());
        if (dot < -EPS) {
            return false;
        }

        double len2 = distancia2(a, b);
        return dot <= len2 + EPS;
    }

    /**
 * Responsabilidade: realizar a operação parametro no segmento no contexto da classe Geometria.
 * @param p ponto analisado, acrescentado ou convertido.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @return valor real resultante do cálculo.
 */
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

    /**
 * Responsabilidade: realizar a operação produto interno no contexto da classe Geometria.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param c terceiro ponto usado na validação geométrica.
 * @return valor real resultante do cálculo.
 */
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

    /**
 * Responsabilidade: realizar a operação validar ponto no contexto da classe Geometria.
 * @param p ponto analisado, acrescentado ou convertido.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 */
    private static void validarPonto(Ponto p, String nome) {
        if (p == null) {
            throw new IllegalArgumentException("Geometria: ponto " + nome + " null");
        }
    }
}
