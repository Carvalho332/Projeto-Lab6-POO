package Engine;

/**
 * Responsabilidade: representar uma rota definida por uma sequência ordenada de pontos.
 *
 * @inv pontos != null && pontos.length >= 2 && pontos consecutivos diferentes
 */
public class Route {
    private static final double EPS = 1e-9;

    private final Ponto[] pontos;

    public Route(Ponto[] pontos) {
        if (pontos == null || pontos.length < 2) {
            throw new IllegalArgumentException("Route:iv");
        }

        this.pontos = new Ponto[pontos.length];
        for (int i = 0; i < pontos.length; i++) {
            if (pontos[i] == null) {
                throw new IllegalArgumentException("Route: ponto null");
            }
            this.pontos[i] = pontos[i];
            if (i > 0 && pontosIguais(this.pontos[i - 1], this.pontos[i])) {
                throw new IllegalArgumentException("Route:iv");
            }
        }
    }

    public int getNumeroPontos() {
        return pontos.length;
    }

    public Ponto getPonto(int i) {
        return pontos[i];
    }

    public Ponto getInicio() {
        return pontos[0];
    }

    public Ponto getFim() {
        return pontos[pontos.length - 1];
    }

    public Ponto[] getPontos() {
        Ponto[] copia = new Ponto[pontos.length];
        System.arraycopy(pontos, 0, copia, 0, pontos.length);
        return copia;
    }

    public double comprimento() {
        double soma = 0.0;
        for (int i = 0; i < pontos.length - 1; i++) {
            soma += pontos[i].dist(pontos[i + 1]);
        }
        return soma;
    }

    public double time(double vl) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("Route.time: velocidade invalida");
        }
        return comprimento() / vl;
    }

    public Vetor[] speed(Vetor w, double vl) {
        if (w == null) {
            throw new IllegalArgumentException("Route.speed: corrente null");
        }
        Vetor[] velocidades = new Vetor[pontos.length - 1];
        for (int i = 0; i < pontos.length - 1; i++) {
            AutoPilot ap = new AutoPilot(pontos[i], pontos[i + 1]);
            double t = ap.time(vl);
            velocidades[i] = ap.speed(w, t);
        }
        return velocidades;
    }

    public Ponto position(double vl, double t) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("Route.position: velocidade invalida");
        }
        if (t <= 0.0) {
            return pontos[0];
        }

        double tempoDecorrido = 0.0;
        for (int i = 0; i < pontos.length - 1; i++) {
            double tempoSegmento = pontos[i].dist(pontos[i + 1]) / vl;

            if (t <= tempoDecorrido + tempoSegmento + EPS) {
                double dt = t - tempoDecorrido;
                double fracao = Math.max(0.0, Math.min(1.0, dt / tempoSegmento));

                double x = pontos[i].getX() + (pontos[i + 1].getX() - pontos[i].getX()) * fracao;
                double y = pontos[i].getY() + (pontos[i + 1].getY() - pontos[i].getY()) * fracao;
                return new Ponto(x, y);
            }

            tempoDecorrido += tempoSegmento;
        }

        return pontos[pontos.length - 1];
    }

    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Route.intersect: segmento null");
        }

        Ponto[] intersecoes = new Ponto[pontos.length - 1];
        int n = 0;

        for (int i = 0; i < pontos.length - 1; i++) {
            SegmentoReta troco = new SegmentoReta(pontos[i], pontos[i + 1]);
            Ponto p = troco.intersect(s);

            if (p != null && !contem(intersecoes, n, p)) {
                intersecoes[n++] = p;
            }
        }

        if (n == 0) {
            return null;
        }

        Ponto[] resultado = new Ponto[n];
        System.arraycopy(intersecoes, 0, resultado, 0, n);
        return resultado;
    }

    public Ponto[] intersect(Obstaculo o) {
        if (o == null) {
            throw new IllegalArgumentException("Route.intersect: obstaculo null");
        }

        Ponto[] intersecoes = new Ponto[Math.max(1, 2 * (pontos.length - 1))];
        int n = 0;

        for (int i = 0; i < pontos.length - 1; i++) {
            SegmentoReta troco = new SegmentoReta(pontos[i], pontos[i + 1]);
            Ponto[] pontosIntersecao = o.intersect(troco);

            if (pontosIntersecao != null) {
                for (Ponto p : pontosIntersecao) {
                    if (p != null && !contem(intersecoes, n, p)) {
                        if (n == intersecoes.length) {
                            intersecoes = expandir(intersecoes);
                        }
                        intersecoes[n++] = p;
                    }
                }
            }
        }

        if (n == 0) {
            return null;
        }

        Ponto[] resultado = new Ponto[n];
        System.arraycopy(intersecoes, 0, resultado, 0, n);
        return resultado;
    }

    public Route invertida() {
        Ponto[] invertidos = new Ponto[pontos.length];
        for (int i = 0; i < pontos.length; i++) {
            invertidos[i] = pontos[pontos.length - 1 - i];
        }
        return new Route(invertidos);
    }

    private boolean contem(Ponto[] array, int n, Ponto p) {
        for (int i = 0; i < n; i++) {
            if (pontosIguais(array[i], p)) {
                return true;
            }
        }
        return false;
    }

    private Ponto[] expandir(Ponto[] array) {
        Ponto[] novo = new Ponto[array.length * 2 + 1];
        System.arraycopy(array, 0, novo, 0, array.length);
        return novo;
    }

    private boolean pontosIguais(Ponto p1, Ponto p2) {
        return p1 != null && p2 != null &&
                Math.abs(p1.getX() - p2.getX()) < EPS &&
                Math.abs(p1.getY() - p2.getY()) < EPS;
    }
}
