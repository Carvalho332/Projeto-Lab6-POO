package Engine;

/**
 * Responsabilidade: representar um segmento de reta e calcular relações geométricas com pontos, vetores e outros segmentos.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class SegmentoReta {
    private final Ponto a;
    private final Ponto b;

    /**
 * Responsabilidade: construir uma instância de SegmentoReta, validando os dados recebidos para preservar os invariantes.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 */
    public SegmentoReta(Ponto a, Vetor v) {
        if (a == null || v == null) {
            throw new IllegalArgumentException("SegmentoReta: argumentos null");
        }
        this.a = a;
        this.b = new Ponto(a.getX() + v.getX(), a.getY() + v.getY());
        verificaInvariante();
    }

    /**
 * Responsabilidade: construir uma instância de SegmentoReta, validando os dados recebidos para preservar os invariantes.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 */
    public SegmentoReta(Ponto a, Ponto b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("SegmentoReta: argumentos null");
        }
        this.a = a;
        this.b = b;
        verificaInvariante();
    }

    /**
 * Responsabilidade: devolver a associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getA() {
        return a;
    }

    /**
 * Responsabilidade: devolver b associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getB() {
        return b;
    }

    /**
 * Responsabilidade: calcular o comprimento total do objeto geométrico.
 * @return distância ou comprimento calculado.
 */
    public double comprimento() {
        return a.dist(b);
    }

    /**
 * Responsabilidade: verificar se o ponto pertence à área ou ao segmento representado.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean contem(Ponto p) {
        return Geometria.pontoPertenceAoSegmento(p, a, b);
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto intersect(Vetor v) {
        if (v == null) {
            throw new IllegalArgumentException("SegmentoReta.intersect: vetor null");
        }
        return intersect(new SegmentoReta(new Ponto(0.0, 0.0), new Ponto(v.getX(), v.getY())));
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param other other usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto intersect(SegmentoReta other) {
        if (other == null) {
            throw new IllegalArgumentException("SegmentoReta.intersect: other null");
        }

        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();
        double cx = other.a.getX();
        double cy = other.a.getY();
        double dx = other.b.getX();
        double dy = other.b.getY();

        double rX = bx - ax;
        double rY = by - ay;
        double sX = dx - cx;
        double sY = dy - cy;
        double rxs = produtoVetorial(rX, rY, sX, sY);
        double qpx = cx - ax;
        double qpy = cy - ay;
        double qpxr = produtoVetorial(qpx, qpy, rX, rY);

        if (Math.abs(rxs) <= Geometria.EPS) {
            return intersecaoSegmentosParalelos(ax, ay, rX, rY, sX, sY, qpx, qpy, qpxr);
        }

        double t = produtoVetorial(qpx, qpy, sX, sY) / rxs;
        double u = produtoVetorial(qpx, qpy, rX, rY) / rxs;

        if (!parametroDentroDoSegmento(t) || !parametroDentroDoSegmento(u)) {
            return null;
        }

        return pontoNoParametro(ax, ay, rX, rY, t);
    }

    /**
 * Responsabilidade: realizar a operação intersecao segmentos paralelos no contexto da classe SegmentoReta.
 * @param ax ax usado pelo método para cumprir a responsabilidade descrita.
 * @param ay ay usado pelo método para cumprir a responsabilidade descrita.
 * @param rX r x usado pelo método para cumprir a responsabilidade descrita.
 * @param rY r y usado pelo método para cumprir a responsabilidade descrita.
 * @param sX s x usado pelo método para cumprir a responsabilidade descrita.
 * @param sY s y usado pelo método para cumprir a responsabilidade descrita.
 * @param qpx qpx usado pelo método para cumprir a responsabilidade descrita.
 * @param qpy qpy usado pelo método para cumprir a responsabilidade descrita.
 * @param qpxr qpxr usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    private Ponto intersecaoSegmentosParalelos(double ax, double ay, double rX, double rY, double sX, double sY, double qpx, double qpy, double qpxr) {
        if (Math.abs(qpxr) > Geometria.EPS) {
            return null;
        }

        double rr = rX * rX + rY * rY;
        if (rr <= Geometria.EPS * Geometria.EPS) {
            return null;
        }

        double t0 = (qpx * rX + qpy * rY) / rr;
        double t1 = t0 + (sX * rX + sY * rY) / rr;
        double lo = Math.max(0.0, Math.min(t0, t1));
        double hi = Math.min(1.0, Math.max(t0, t1));

        if (hi + Geometria.EPS < lo) {
            return null;
        }

        return pontoNoParametro(ax, ay, rX, rY, lo);
    }

    /**
 * Responsabilidade: realizar a operação produto vetorial no contexto da classe SegmentoReta.
 * @param ax ax usado pelo método para cumprir a responsabilidade descrita.
 * @param ay ay usado pelo método para cumprir a responsabilidade descrita.
 * @param bx bx usado pelo método para cumprir a responsabilidade descrita.
 * @param by by usado pelo método para cumprir a responsabilidade descrita.
 * @return valor real resultante do cálculo.
 */
    private double produtoVetorial(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }

    /**
 * Responsabilidade: realizar a operação parametro dentro do segmento no contexto da classe SegmentoReta.
 * @param t tempo disponível para percorrer o segmento.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean parametroDentroDoSegmento(double t) {
        return t >= -Geometria.EPS && t <= 1.0 + Geometria.EPS;
    }

    /**
 * Responsabilidade: realizar a operação ponto no parametro no contexto da classe SegmentoReta.
 * @param ax ax usado pelo método para cumprir a responsabilidade descrita.
 * @param ay ay usado pelo método para cumprir a responsabilidade descrita.
 * @param rX r x usado pelo método para cumprir a responsabilidade descrita.
 * @param rY r y usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 * @return ponto calculado ou guardado pela instância.
 */
    private Ponto pontoNoParametro(double ax, double ay, double rX, double rY, double t) {
        return new Ponto(ax + t * rX, ay + t * rY);
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe SegmentoReta.
 */
    private void verificaInvariante() {
        if (a.dist(b) <= Geometria.EPS) {
            throw new IllegalArgumentException("SegmentoReta:iv");
        }
    }

    /**
 * Responsabilidade: produzir uma representação textual estável para debug e testes.
 * @return texto formatado ou identificador pedido.
 */
    @Override
    public String toString() {
        return "sr(" + a + "; " + b + ")";
    }
}
