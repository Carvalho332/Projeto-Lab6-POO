package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade: representar obstáculos poligonais e calcular interseções entre as suas arestas e segmentos de rota.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os vértices são válidos e respeitam a forma geométrica representada.
 */
public class Poligono extends Obstaculo {
    private final Ponto[] vertices;

    /**
 * Responsabilidade: construir uma instância de Poligono, validando os dados recebidos para preservar os invariantes.
 * @param vertices vertices usado pelo método para cumprir a responsabilidade descrita.
 */
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

    /**
 * Responsabilidade: devolver numero vertices associado à instância atual.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getNumeroVertices() {
        return vertices.length;
    }

    /**
 * Responsabilidade: devolver vertice associado à instância atual.
 * @param i índice do elemento a obter.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getVertice(int i) {
        return vertices[i];
    }

    /**
 * Responsabilidade: devolver vertices associado à instância atual.
 * @return array com os elementos calculados ou copiados.
 */
    public Ponto[] getVertices() {
        Ponto[] copia = new Ponto[vertices.length];
        System.arraycopy(vertices, 0, copia, 0, vertices.length);
        return copia;
    }

    /**
 * Responsabilidade: devolver aresta associado à instância atual.
 * @param i índice do elemento a obter.
 * @return objeto resultante da operação.
 */
    public SegmentoReta getAresta(int i) {
        if (i < 0 || i >= vertices.length) {
            throw new IndexOutOfBoundsException("Poligono.getAresta: indice invalido");
        }
        return new SegmentoReta(vertices[i], vertices[(i + 1) % vertices.length]);
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
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

    /**
 * Responsabilidade: verificar se o ponto pertence à área ou ao segmento representado.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
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

    /**
 * Responsabilidade: realizar a operação pontos iguais no contexto da classe Poligono.
 * @param p1 p1 usado pelo método para cumprir a responsabilidade descrita.
 * @param p2 p2 usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    protected boolean pontosIguais(Ponto p1, Ponto p2) {
        return Geometria.iguais(p1, p2);
    }

    /**
 * Responsabilidade: indicar se a condição tem numero valido vertices se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    protected boolean temNumeroValidoVertices() {
        return vertices.length >= 3;
    }

    /**
 * Responsabilidade: realizar a operação sentido horario no contexto da classe Poligono.
 * @return true se a condição se verificar; false caso contrário.
 */
    protected boolean sentidoHorario() {
        double soma = 0.0;
        for (int i = 0; i < vertices.length; i++) {
            Ponto a = vertices[i];
            Ponto b = vertices[(i + 1) % vertices.length];
            soma += (b.getX() - a.getX()) * (b.getY() + a.getY());
        }
        return soma > Geometria.EPS;
    }

    /**
 * Responsabilidade: adicionar ponto unico à estrutura respetiva mantendo a consistência dos dados.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @param p ponto analisado, acrescentado ou convertido.
 */
    private void adicionarPontoUnico(List<Ponto> pontos, Ponto p) {
        for (Ponto existente : pontos) {
            if (Geometria.iguais(existente, p)) {
                return;
            }
        }
        pontos.add(p);
    }

    /**
 * Responsabilidade: realizar a operação verifica invariante no contexto da classe Poligono.
 */
    private void verificaInvariante() {
        if (!temNumeroValidoVertices()) {
            throw new IllegalArgumentException("Poligono:iv");
        }
    }
}
