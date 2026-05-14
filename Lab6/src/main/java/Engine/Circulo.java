package Engine;

/**
 * Responsabilidade: representar obstáculos circulares e calcular interseções entre a circunferência/disco e segmentos de reta.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv o centro não é nulo e o raio é positivo.
 */
public class Circulo extends Obstaculo {
    private static final double EPS = 1e-9;

    private final Ponto centro;
    private final double raio;

    /**
 * Responsabilidade: construir uma instância de Circulo, validando os dados recebidos para preservar os invariantes.
 * @param centro centro usado pelo método para cumprir a responsabilidade descrita.
 * @param raio raio usado pelo método para cumprir a responsabilidade descrita.
 */
    public Circulo(Ponto centro, double raio) {
        if (centro == null) {
            throw new IllegalArgumentException("Circulo: centro null");
        }
        this.centro = centro;
        this.raio = raio;
        verificaInvariante();
    }

    /**
 * Responsabilidade: devolver centro associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getCentro() {
        return centro;
    }

    /**
 * Responsabilidade: devolver raio associado à instância atual.
 * @return valor real resultante do cálculo.
 */
    public double getRaio() {
        return raio;
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
    @Override
    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Circulo.intersect: segmento null");
        }

        double ax = s.getA().getX();
        double ay = s.getA().getY();
        double bx = s.getB().getX();
        double by = s.getB().getY();

        double dx = bx - ax;
        double dy = by - ay;

        double fx = ax - centro.getX();
        double fy = ay - centro.getY();

        double qa = dx * dx + dy * dy;
        double qb = 2.0 * (fx * dx + fy * dy);
        double qc = fx * fx + fy * fy - raio * raio;

        double delta = qb * qb - 4.0 * qa * qc;

        if (delta < -EPS) {
            return null;
        }

        Ponto[] pontos = new Ponto[2];
        int n = 0;

        if (Math.abs(delta) <= EPS) {
            double t = -qb / (2.0 * qa);
            if (t >= -EPS && t <= 1.0 + EPS) {
                pontos[n++] = new Ponto(ax + t * dx, ay + t * dy);
            }
        } else {
            double raiz = Math.sqrt(delta);
            double t1 = (-qb - raiz) / (2.0 * qa);
            double t2 = (-qb + raiz) / (2.0 * qa);

            if (t1 >= -EPS && t1 <= 1.0 + EPS) {
                pontos[n++] = new Ponto(ax + t1 * dx, ay + t1 * dy);
            }

            if (t2 >= -EPS && t2 <= 1.0 + EPS) {
                Ponto p = new Ponto(ax + t2 * dx, ay + t2 * dy);
                if (n == 0 || !p.igual(pontos[0])) {
                    pontos[n++] = p;
                }
            }
        }

        if (n == 0) {
            return null;
        }

        Ponto[] resultado = new Ponto[n];
        System.arraycopy(pontos, 0, resultado, 0, n);
        return resultado;
    }

    /**
 * Responsabilidade: verificar se o ponto pertence à área ou ao segmento representado.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
    @Override
    public boolean contem(Ponto p) {
        if (p == null) {
            throw new IllegalArgumentException("Circulo.contem: ponto null");
        }
        return centro.dist(p) <= raio + EPS;
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Circulo.
 */
    private void verificaInvariante() {
        if (!Double.isFinite(raio) || raio <= 0.0) {
            throw new IllegalArgumentException("Circulo:iv");
        }
    }
}
