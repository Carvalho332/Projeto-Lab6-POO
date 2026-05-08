package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade: representar um polígono definido por uma sequência ordenada de vértices.
 *
 * @inv vertices != null && vertices.length >= 3
 */
public class Poligono extends Obstaculo {
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

    public SegmentoReta getAresta(int i) {
        if (i < 0 || i >= vertices.length) {
            throw new IndexOutOfBoundsException("Poligono.getAresta: indice invalido");
        }
        return new SegmentoReta(vertices[i], vertices[(i + 1) % vertices.length]);
    }

    @Override
    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Poligono.intersect: segmento null");
        }

        List<Ponto> intersecoes = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {
            Ponto p = getAresta(i).intersect(s);
            if (p != null) {
                adicionarPontoUnico(intersecoes, p);
            }
        }

        if (intersecoes.isEmpty()) {
            return null;
        }
        return intersecoes.toArray(new Ponto[0]);
    }

    @Override
    public boolean contem(Ponto p) {
        if (p == null) {
            throw new IllegalArgumentException("Poligono.contem: ponto null");
        }

        boolean dentro = false;
        for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            Ponto a = vertices[j];
            Ponto b = vertices[i];

            if (Geometria.pontoPertenceAoSegmento(p, a, b)) {
                return true;
            }

            boolean cruzaHorizontal = (a.getY() > p.getY()) != (b.getY() > p.getY());
            if (cruzaHorizontal) {
                double xIntersecao = a.getX() + (p.getY() - a.getY()) *
                        (b.getX() - a.getX()) / (b.getY() - a.getY());
                if (p.getX() < xIntersecao + Geometria.EPS) {
                    dentro = !dentro;
                }
            }
        }
        return dentro;
    }

    protected boolean pontosIguais(Ponto p1, Ponto p2) {
        return Geometria.iguais(p1, p2);
    }

    protected boolean temNumeroValidoVertices() {
        return vertices.length >= 3;
    }

    protected boolean sentidoHorario() {
        double soma = 0.0;
        for (int i = 0; i < vertices.length; i++) {
            Ponto a = vertices[i];
            Ponto b = vertices[(i + 1) % vertices.length];
            soma += (b.getX() - a.getX()) * (b.getY() + a.getY());
        }
        return soma > Geometria.EPS;
    }

    private void adicionarPontoUnico(List<Ponto> pontos, Ponto p) {
        for (Ponto existente : pontos) {
            if (Geometria.iguais(existente, p)) {
                return;
            }
        }
        pontos.add(p);
    }

    private void verificaInvariante() {
        if (!temNumeroValidoVertices()) {
            throw new IllegalArgumentException("Poligono:iv");
        }
    }
}
