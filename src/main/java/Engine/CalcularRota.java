package Engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Responsabilidade: calcular a rota mais rápida disponível.
 *
 * Esta versão cria automaticamente um grafo de navegação a partir das rotas do mapa:
 * - pontos dos portos;
 * - pontos intermédios das Route;
 * - interseções entre segmentos de rotas.
 *
 * Depois aplica Dijkstra para escolher o caminho mais rápido até ao destino.
 */
public class CalcularRota {
    private static final double EPS = 1e-9;

    public Route rotaMaisRapida(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double vl) {
        if (origem == null || destino == null || mapa == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota.rotaMaisRapida: argumentos invalidos");
        }

        if (origem.getPosicao().igual(destino.getPosicao())) {
            return new Route(new Ponto[]{origem.getPosicao(), destino.getPosicao()});
        }

        Grafo grafo = construirGrafo(mapa, corrente, vl);

        int s = grafo.indiceNo(origem.getPosicao());
        int t = grafo.indiceNo(destino.getPosicao());

        if (s == -1 || t == -1) {
            return null;
        }

        double[] dist = new double[grafo.nos.size()];
        boolean[] visitado = new boolean[grafo.nos.size()];
        int[] anterior = new int[grafo.nos.size()];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            anterior[i] = -1;
        }

        dist[s] = 0.0;

        for (int k = 0; k < grafo.nos.size(); k++) {
            int u = menorDistanciaNaoVisitado(dist, visitado);
            if (u == -1 || u == t) {
                break;
            }

            visitado[u] = true;

            for (Ligacao ligacao : grafo.ligacoes) {
                if (ligacao.origem == u) {
                    int v = ligacao.destino;
                    double novaDistancia = dist[u] + ligacao.tempo;

                    if (novaDistancia + EPS < dist[v]) {
                        dist[v] = novaDistancia;
                        anterior[v] = u;
                    }
                }
            }
        }

        if (Double.isInfinite(dist[t])) {
            return null;
        }

        List<Ponto> caminho = new ArrayList<>();
        for (int atual = t; atual != -1; atual = anterior[atual]) {
            caminho.add(0, grafo.nos.get(atual));
        }

        if (caminho.size() < 2) {
            return null;
        }

        return new Route(caminho.toArray(new Ponto[0]));
    }

    public boolean rotaBloqueada(Route rota, List<Obstaculo> obstaculos) {
        if (rota == null || obstaculos == null) {
            throw new IllegalArgumentException("CalcularRota.rotaBloqueada: argumentos invalidos");
        }

        for (Obstaculo o : obstaculos) {
            if (o != null && rota.intersect(o) != null) {
                return true;
            }
        }

        return false;
    }

    public double tempoRota(Route rota, Vetor corrente, double vl) {
        if (rota == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota.tempoRota: argumentos invalidos");
        }

        return rota.time(vl);
    }

    private Grafo construirGrafo(MapaNavegacao mapa, Vetor corrente, double vl) {
        Grafo grafo = new Grafo();

        for (Porto porto : mapa.getPortos()) {
            grafo.adicionarNo(porto.getPosicao());
        }

        for (Route rota : mapa.getRotas()) {
            for (Ponto p : rota.getPontos()) {
                grafo.adicionarNo(p);
            }
        }

        for (Route rota : mapa.getRotas()) {
            adicionarLigacoesDaRota(grafo, rota, mapa, corrente, vl);
        }

        return grafo;
    }

    private void adicionarLigacoesDaRota(Grafo grafo, Route rota, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (int i = 0; i < rota.getNumeroPontos() - 1; i++) {
            Ponto a = rota.getPonto(i);
            Ponto b = rota.getPonto(i + 1);
            SegmentoReta segmento = new SegmentoReta(a, b);

            List<Ponto> pontosDoSegmento = new ArrayList<>();
            adicionarPonto(pontosDoSegmento, a);
            adicionarPonto(pontosDoSegmento, b);

            for (Porto porto : mapa.getPortos()) {
                Ponto p = porto.getPosicao();
                if (pontoPertenceAoSegmento(p, a, b)) {
                    adicionarPonto(pontosDoSegmento, p);
                }
            }

            for (Route outra : mapa.getRotas()) {
                if (outra == rota) {
                    continue;
                }

                for (int j = 0; j < outra.getNumeroPontos() - 1; j++) {
                    SegmentoReta outroSegmento = new SegmentoReta(outra.getPonto(j), outra.getPonto(j + 1));
                    Ponto intersecao = segmento.intersect(outroSegmento);

                    if (intersecao != null && pontoPertenceAoSegmento(intersecao, a, b)) {
                        adicionarPonto(pontosDoSegmento, intersecao);
                    }
                }
            }

            pontosDoSegmento.sort(Comparator.comparingDouble(p -> parametroNoSegmento(p, a, b)));

            for (int k = 0; k < pontosDoSegmento.size() - 1; k++) {
                Ponto p1 = pontosDoSegmento.get(k);
                Ponto p2 = pontosDoSegmento.get(k + 1);

                if (!p1.igual(p2)) {
                    Route subRota = new Route(new Ponto[]{p1, p2});

                    if (!rotaBloqueada(subRota, mapa.getTodosObstaculos())) {
                        adicionarLigacaoBidirecional(grafo, p1, p2, subRota, corrente, vl);
                    }
                }
            }
        }
    }

    private void adicionarLigacaoBidirecional(Grafo grafo, Ponto a, Ponto b, Route subRota, Vetor corrente, double vl) {
        int ia = grafo.adicionarNo(a);
        int ib = grafo.adicionarNo(b);
        double tempo = tempoRota(subRota, corrente, vl);

        grafo.ligacoes.add(new Ligacao(ia, ib, tempo));
        grafo.ligacoes.add(new Ligacao(ib, ia, tempo));
    }

    private int menorDistanciaNaoVisitado(double[] dist, boolean[] visitado) {
        int melhor = -1;
        double valor = Double.POSITIVE_INFINITY;

        for (int i = 0; i < dist.length; i++) {
            if (!visitado[i] && dist[i] < valor) {
                valor = dist[i];
                melhor = i;
            }
        }

        return melhor;
    }

    private void adicionarPonto(List<Ponto> pontos, Ponto p) {
        for (Ponto existente : pontos) {
            if (existente.igual(p)) {
                return;
            }
        }

        pontos.add(p);
    }

    private boolean pontoPertenceAoSegmento(Ponto p, Ponto a, Ponto b) {
        double cross = (p.getY() - a.getY()) * (b.getX() - a.getX())
                - (p.getX() - a.getX()) * (b.getY() - a.getY());

        if (Math.abs(cross) > EPS) {
            return false;
        }

        double dot = (p.getX() - a.getX()) * (b.getX() - a.getX())
                + (p.getY() - a.getY()) * (b.getY() - a.getY());

        if (dot < -EPS) {
            return false;
        }

        double len2 = Math.pow(b.getX() - a.getX(), 2)
                + Math.pow(b.getY() - a.getY(), 2);

        return dot <= len2 + EPS;
    }

    private double parametroNoSegmento(Ponto p, Ponto a, Ponto b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double len2 = dx * dx + dy * dy;

        if (len2 <= EPS) {
            return 0.0;
        }

        return ((p.getX() - a.getX()) * dx + (p.getY() - a.getY()) * dy) / len2;
    }

    private static class Grafo {
        private final List<Ponto> nos = new ArrayList<>();
        private final List<Ligacao> ligacoes = new ArrayList<>();

        private int adicionarNo(Ponto p) {
            int i = indiceNo(p);

            if (i != -1) {
                return i;
            }

            nos.add(p);
            return nos.size() - 1;
        }

        private int indiceNo(Ponto p) {
            for (int i = 0; i < nos.size(); i++) {
                if (nos.get(i).igual(p)) {
                    return i;
                }
            }

            return -1;
        }
    }

    private static class Ligacao {
        private final int origem;
        private final int destino;
        private final double tempo;

        private Ligacao(int origem, int destino, double tempo) {
            this.origem = origem;
            this.destino = destino;
            this.tempo = tempo;
        }
    }
}