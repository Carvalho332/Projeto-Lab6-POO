package Engine;

/**
 * Responsabilidade: representar um polígono definido por uma sequência ordenada de vértices.
 *
 * @inv vertices != null && vertices.length >= 3
 */
public class Poligono extends Obstaculo {
    private static final double EPS = 1e-9;

    private final Ponto[] vertices;

    public Poligono(Ponto[] vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Poligono: vertices null");
        }
        this.vertices = new Ponto[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == null) {
                throw new IllegalArgumentException("Poligono: vertice null");
            }
            this.vertices[i] = vertices[i];
        }

        if (getClass() == Poligono.class) {
            verificaInvariante();
        }
    }

    public int getNumeroVertices() {
        return vertices.length;
    }

    public Ponto getVertice(int i) {
        return vertices[i];
    }

    public Ponto[] getVertices() {
        Ponto[] copia = new Ponto[vertices.length];
        System.arraycopy(vertices, 0, copia, 0, vertices.length);
        return copia;
    }

    @Override
    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Poligono.intersect: segmento null");
        }

        Ponto[] intersecoes = new Ponto[vertices.length];
        int n = 0;

        for (int i = 0; i < vertices.length; i++) {
            SegmentoReta aresta = new SegmentoReta(vertices[i], vertices[(i + 1) % vertices.length]);
            Ponto p = aresta.intersect(s);

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

    protected boolean pontosIguais(Ponto p1, Ponto p2) {
        return p1 != null && p2 != null &&
                Math.abs(p1.getX() - p2.getX()) < EPS &&
                Math.abs(p1.getY() - p2.getY()) < EPS;
    }

    protected boolean temNumeroValidoVertices() {
        return vertices.length >= 3;
    }

    /**
     * Verifica a orientação dos vértices.
     *
     * @return true se os vértices estiverem no sentido horário
     */
    protected boolean sentidoHorario() {
        double soma = 0.0;
        for (int i = 0; i < vertices.length; i++) {
            Ponto a = vertices[i];
            Ponto b = vertices[(i + 1) % vertices.length];
            soma += (b.getX() - a.getX()) * (b.getY() + a.getY());
        }
        return soma > EPS;
    }

    private boolean contem(Ponto[] pontos, int n, Ponto p) {
        for (int i = 0; i < n; i++) {
            if (pontosIguais(pontos[i], p)) {
                return true;
            }
        }
        return false;
    }

    private void verificaInvariante() {
        if (!temNumeroValidoVertices()) {
            throw new IllegalArgumentException("Poligono:iv");
        }
    }
}
