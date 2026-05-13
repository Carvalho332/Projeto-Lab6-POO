package Engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Responsabilidade: calcular a rota mais rápida disponível usando uma estratégia
 * baseada em grafo e Dijkstra.
 *
 * <p>O grafo é construído automaticamente a partir dos portos, dos pontos das
 * rotas e das interseções entre segmentos. Rotas ou sub-rotas bloqueadas por
 * obstáculos são excluídas antes da escolha do caminho.</p>
 *
 * @version 2026-05-08
 * @see EstrategiaCalculoRota
 * @see Route
 * @see MapaNavegacao
 */
public class CalcularRota implements EstrategiaCalculoRota {

    @Override
    public Optional<Route> calcular(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double velocidadeLinear) {
        validarDados(origem, destino, mapa, corrente, velocidadeLinear);
        if (origem.getPosicao().igual(destino.getPosicao())) {
            return Optional.empty();
        }

        Grafo grafo = construirGrafo(mapa, corrente, velocidadeLinear);
        int indiceOrigem = grafo.indiceNo(origem.getPosicao());
        int indiceDestino = grafo.indiceNo(destino.getPosicao());

        if (indiceOrigem == -1 || indiceDestino == -1) {
            return Optional.empty();
        }

        return executarDijkstra(grafo, indiceOrigem, indiceDestino);
    }

    /**
     * Método mantido por compatibilidade com os testes/código anteriores.
     */
    public Route rotaMaisRapida(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double vl) {
        return calcular(origem, destino, mapa, corrente, vl).orElse(null);
    }

    public boolean rotaBloqueada(Route rota, List<Obstaculo> obstaculos) {
        if (rota == null || obstaculos == null) {
            throw new IllegalArgumentException("CalcularRota.rotaBloqueada: argumentos invalidos");
        }

        for (Obstaculo obstaculo : obstaculos) {
            if (obstaculo == null) {
                throw new IllegalArgumentException("CalcularRota.rotaBloqueada: obstaculo null");
            }
            if (rota.intersect(obstaculo) != null || algumPontoDaRotaDentroDoObstaculo(rota, obstaculo)) {
                return true;
            }
        }
        return false;
    }

    public double tempoRota(Route rota, Vetor corrente, double vl) {
        if (rota == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota.tempoRota: argumentos invalidos");
        }

        /*
         * A corrente pertence ao modelo físico do problema. O tempo, contudo,
         * depende do comprimento e da velocidade linear resultante pretendida.
         * A corrente é compensada pelo AutoPilot ao calcular a velocidade vetorial
         * própria do navio.
         */
        return rota.time(vl);
    }

    private void validarDados(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double vl) {
        if (origem == null || destino == null || mapa == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota: argumentos invalidos");
        }
    }

    private boolean algumPontoDaRotaDentroDoObstaculo(Route rota, Obstaculo obstaculo) {
        for (int i = 0; i < rota.getNumeroPontos(); i++) {
            if (obstaculo.contem(rota.getPonto(i))) {
                return true;
            }
        }
        return false;
    }

    private Grafo construirGrafo(MapaNavegacao mapa, Vetor corrente, double vl) {
        Grafo grafo = new Grafo();
        adicionarPortosComoNos(grafo, mapa);
        adicionarPontosDasRotasComoNos(grafo, mapa);
        adicionarLigacoesDasRotas(grafo, mapa, corrente, vl);
        return grafo;
    }

    private void adicionarPortosComoNos(Grafo grafo, MapaNavegacao mapa) {
        for (Porto porto : mapa.getPortos()) {
            grafo.adicionarNo(porto.getPosicao());
        }
    }

    private void adicionarPontosDasRotasComoNos(Grafo grafo, MapaNavegacao mapa) {
        for (Route rota : mapa.getRotas()) {
            for (Ponto p : rota.getPontos()) {
                grafo.adicionarNo(p);
            }
        }
    }

    private void adicionarLigacoesDasRotas(Grafo grafo, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (Route rota : mapa.getRotas()) {
            adicionarLigacoesDaRota(grafo, rota, mapa, corrente, vl);
        }
    }

    private void adicionarLigacoesDaRota(Grafo grafo, Route rota, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (int i = 0; i < rota.getNumeroSegmentos(); i++) {
            SegmentoReta segmento = rota.getSegmento(i);
            List<Ponto> pontosDoSegmento = pontosRelevantesDoSegmento(segmento, rota, mapa);
            pontosDoSegmento.sort(Comparator.comparingDouble(p -> Geometria.parametroNoSegmento(p, segmento.getA(), segmento.getB())));
            adicionarSubLigacoes(grafo, pontosDoSegmento, mapa, corrente, vl);
        }
    }

    private List<Ponto> pontosRelevantesDoSegmento(SegmentoReta segmento, Route rotaAtual, MapaNavegacao mapa) {
        List<Ponto> pontos = new ArrayList<>();
        adicionarPonto(pontos, segmento.getA());
        adicionarPonto(pontos, segmento.getB());
        adicionarPortosNoSegmento(pontos, segmento, mapa);
        adicionarIntersecoesComOutrasRotas(pontos, segmento, rotaAtual, mapa);
        return pontos;
    }

    private void adicionarPortosNoSegmento(List<Ponto> pontos, SegmentoReta segmento, MapaNavegacao mapa) {
        for (Porto porto : mapa.getPortos()) {
            Ponto p = porto.getPosicao();
            if (segmento.contem(p)) {
                adicionarPonto(pontos, p);
            }
        }
    }

    private void adicionarIntersecoesComOutrasRotas(List<Ponto> pontos, SegmentoReta segmento, Route rotaAtual, MapaNavegacao mapa) {
        for (Route outra : mapa.getRotas()) {
            if (outra == rotaAtual) {
                continue;
            }
            for (int i = 0; i < outra.getNumeroSegmentos(); i++) {
                Ponto intersecao = segmento.intersect(outra.getSegmento(i));
                if (intersecao != null && segmento.contem(intersecao)) {
                    adicionarPonto(pontos, intersecao);
                }
            }
        }
    }

    private void adicionarSubLigacoes(Grafo grafo, List<Ponto> pontosDoSegmento, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (int i = 0; i < pontosDoSegmento.size() - 1; i++) {
            Ponto p1 = pontosDoSegmento.get(i);
            Ponto p2 = pontosDoSegmento.get(i + 1);
            if (p1.igual(p2)) {
                continue;
            }
            Route subRota = new Route(new Ponto[]{p1, p2});
            if (!rotaBloqueada(subRota, mapa.getTodosObstaculos())) {
                adicionarLigacaoBidirecional(grafo, p1, p2, subRota, corrente, vl);
            }
        }
    }

    private void adicionarLigacaoBidirecional(Grafo grafo, Ponto a, Ponto b, Route subRota, Vetor corrente, double vl) {
        int ia = grafo.adicionarNo(a);
        int ib = grafo.adicionarNo(b);
        double tempo = tempoRota(subRota, corrente, vl);
        grafo.adicionarLigacao(ia, ib, tempo);
        grafo.adicionarLigacao(ib, ia, tempo);
    }

    private Optional<Route> executarDijkstra(Grafo grafo, int origem, int destino) {
        double[] dist = new double[grafo.numeroNos()];
        int[] anterior = new int[grafo.numeroNos()];
        boolean[] visitado = new boolean[grafo.numeroNos()];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            anterior[i] = -1;
        }

        PriorityQueue<EntradaDijkstra> fila = new PriorityQueue<>(Comparator.comparingDouble(e -> e.distancia));
        dist[origem] = 0.0;
        fila.add(new EntradaDijkstra(origem, 0.0));

        while (!fila.isEmpty()) {
            EntradaDijkstra entrada = fila.poll();
            int u = entrada.indice;
            if (visitado[u]) {
                continue;
            }
            visitado[u] = true;
            if (u == destino) {
                break;
            }

            for (Ligacao ligacao : grafo.ligacoesDe(u)) {
                int v = ligacao.destino;
                double novaDistancia = dist[u] + ligacao.tempo;
                if (novaDistancia + Geometria.EPS < dist[v]) {
                    dist[v] = novaDistancia;
                    anterior[v] = u;
                    fila.add(new EntradaDijkstra(v, novaDistancia));
                }
            }
        }

        if (Double.isInfinite(dist[destino])) {
            return Optional.empty();
        }

        List<Ponto> caminho = reconstruirCaminho(grafo, anterior, destino);
        if (caminho.size() < 2) {
            return Optional.empty();
        }
        return Optional.of(new Route(caminho.toArray(new Ponto[0])));
    }

    private List<Ponto> reconstruirCaminho(Grafo grafo, int[] anterior, int destino) {
        List<Ponto> caminho = new ArrayList<>();
        for (int atual = destino; atual != -1; atual = anterior[atual]) {
            caminho.add(0, grafo.no(atual));
        }
        return caminho;
    }

    private void adicionarPonto(List<Ponto> pontos, Ponto p) {
        for (Ponto existente : pontos) {
            if (existente.igual(p)) {
                return;
            }
        }
        pontos.add(p);
    }

    private static class Grafo {
        private final List<Ponto> nos = new ArrayList<>();
        private final List<List<Ligacao>> adjacencias = new ArrayList<>();

        private int adicionarNo(Ponto p) {
            int i = indiceNo(p);
            if (i != -1) {
                return i;
            }
            nos.add(p);
            adjacencias.add(new ArrayList<>());
            return nos.size() - 1;
        }

        private void adicionarLigacao(int origem, int destino, double tempo) {
            adjacencias.get(origem).add(new Ligacao(destino, tempo));
        }

        private int indiceNo(Ponto p) {
            for (int i = 0; i < nos.size(); i++) {
                if (nos.get(i).igual(p)) {
                    return i;
                }
            }
            return -1;
        }

        private Ponto no(int indice) {
            return nos.get(indice);
        }

        private int numeroNos() {
            return nos.size();
        }

        private List<Ligacao> ligacoesDe(int origem) {
            return adjacencias.get(origem);
        }
    }

    private static class Ligacao {
        private final int destino;
        private final double tempo;

        private Ligacao(int destino, double tempo) {
            this.destino = destino;
            this.tempo = tempo;
        }
    }

    private static class EntradaDijkstra {
        private final int indice;
        private final double distancia;

        private EntradaDijkstra(int indice, double distancia) {
            this.indice = indice;
            this.distancia = distancia;
        }
    }
}
