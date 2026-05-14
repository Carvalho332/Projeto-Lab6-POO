package Engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Responsabilidade: determinar o caminho mais rápido entre portos através das rotas disponíveis, considerando interseções, obstáculos e corrente.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class CalcularRota implements EstrategiaCalculoRota {

    /**
 * Responsabilidade: calcular uma rota possível entre dois portos através da estratégia configurada.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param destino porto de chegada pretendido.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param velocidadeLinear velocidade linear pretendida ao longo da rota.
 * @return Optional com a rota encontrada; Optional.empty() quando não existe rota válida.
 */
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
 * Responsabilidade: obter a rota mais rápida entre a origem e o destino, mantendo compatibilidade com código que aceita null.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param destino porto de chegada pretendido.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return rota calculada ou construída pela operação.
 */
    public Route rotaMaisRapida(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double vl) {
        return calcular(origem, destino, mapa, corrente, vl).orElse(null);
    }

    /**
 * Responsabilidade: verificar se uma rota fica inutilizável por intersectar ou atravessar algum obstáculo.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @param obstaculos obstáculos que podem bloquear rotas ou ser apresentados no mapa.
 * @return true se a condição se verificar; false caso contrário.
 */
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

    /**
 * Responsabilidade: calcular o tempo necessário para percorrer uma rota à velocidade linear indicada.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return tempo de percurso calculado.
 */
    public double tempoRota(Route rota, Vetor corrente, double vl) {
        if (rota == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota.tempoRota: argumentos invalidos");
        }

        return rota.time(vl);
    }

    /**
 * Responsabilidade: validar os argumentos obrigatórios antes de iniciar o cálculo de caminho mínimo.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param destino porto de chegada pretendido.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 */
    private void validarDados(Porto origem, Porto destino, MapaNavegacao mapa, Vetor corrente, double vl) {
        if (origem == null || destino == null || mapa == null || corrente == null || vl <= 0.0) {
            throw new IllegalArgumentException("CalcularRota: argumentos invalidos");
        }
    }

    /**
 * Responsabilidade: detetar se algum ponto de uma rota está dentro da área de um obstáculo.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @param obstaculo obstáculo analisado.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean algumPontoDaRotaDentroDoObstaculo(Route rota, Obstaculo obstaculo) {
        for (int i = 0; i < rota.getNumeroPontos(); i++) {
            if (obstaculo.contem(rota.getPonto(i))) {
                return true;
            }
        }
        return false;
    }

    /**
 * Responsabilidade: criar o grafo usado para calcular caminhos entre portos, pontos de rota e interseções.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return objeto resultante da operação.
 */
    private Grafo construirGrafo(MapaNavegacao mapa, Vetor corrente, double vl) {
        Grafo grafo = new Grafo();
        adicionarPortosComoNos(grafo, mapa);
        adicionarPontosDasRotasComoNos(grafo, mapa);
        adicionarLigacoesDasRotas(grafo, mapa, corrente, vl);
        return grafo;
    }

    /**
 * Responsabilidade: inserir no grafo as posições dos portos como nós navegáveis.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 */
    private void adicionarPortosComoNos(Grafo grafo, MapaNavegacao mapa) {
        for (Porto porto : mapa.getPortos()) {
            grafo.adicionarNo(porto.getPosicao());
        }
    }

    /**
 * Responsabilidade: inserir no grafo os pontos explícitos que compõem as rotas.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 */
    private void adicionarPontosDasRotasComoNos(Grafo grafo, MapaNavegacao mapa) {
        for (Route rota : mapa.getRotas()) {
            for (Ponto p : rota.getPontos()) {
                grafo.adicionarNo(p);
            }
        }
    }

    /**
 * Responsabilidade: adicionar ao grafo as ligações navegáveis de todas as rotas do mapa.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 */
    private void adicionarLigacoesDasRotas(Grafo grafo, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (Route rota : mapa.getRotas()) {
            adicionarLigacoesDaRota(grafo, rota, mapa, corrente, vl);
        }
    }

    /**
 * Responsabilidade: dividir os segmentos de uma rota em subligações entre pontos relevantes.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 */
    private void adicionarLigacoesDaRota(Grafo grafo, Route rota, MapaNavegacao mapa, Vetor corrente, double vl) {
        for (int i = 0; i < rota.getNumeroSegmentos(); i++) {
            SegmentoReta segmento = rota.getSegmento(i);
            List<Ponto> pontosDoSegmento = pontosRelevantesDoSegmento(segmento, rota, mapa);
            pontosDoSegmento.sort(Comparator.comparingDouble(p -> Geometria.parametroNoSegmento(p, segmento.getA(), segmento.getB())));
            adicionarSubLigacoes(grafo, pontosDoSegmento, mapa, corrente, vl);
        }
    }

    /**
 * Responsabilidade: obter extremos, portos e interseções que pertencem ao segmento analisado.
 * @param segmento segmento de rota analisado.
 * @param rotaAtual rota que contém o segmento em análise.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<Ponto> pontosRelevantesDoSegmento(SegmentoReta segmento, Route rotaAtual, MapaNavegacao mapa) {
        List<Ponto> pontos = new ArrayList<>();
        adicionarPonto(pontos, segmento.getA());
        adicionarPonto(pontos, segmento.getB());
        adicionarPortosNoSegmento(pontos, segmento, mapa);
        adicionarIntersecoesComOutrasRotas(pontos, segmento, rotaAtual, mapa);
        return pontos;
    }

    /**
 * Responsabilidade: adicionar à lista os portos que estão colocados sobre um segmento.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @param segmento segmento de rota analisado.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 */
    private void adicionarPortosNoSegmento(List<Ponto> pontos, SegmentoReta segmento, MapaNavegacao mapa) {
        for (Porto porto : mapa.getPortos()) {
            Ponto p = porto.getPosicao();
            if (segmento.contem(p)) {
                adicionarPonto(pontos, p);
            }
        }
    }

    /**
 * Responsabilidade: adicionar pontos onde o segmento atual cruza segmentos de outras rotas.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @param segmento segmento de rota analisado.
 * @param rotaAtual rota que contém o segmento em análise.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 */
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

    /**
 * Responsabilidade: criar ligações entre pontos consecutivos de um segmento, ignorando ligações bloqueadas.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param pontosDoSegmento pontos relevantes já ordenados sobre um mesmo segmento.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 */
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

    /**
 * Responsabilidade: adicionar ligacao bidirecional à estrutura respetiva mantendo a consistência dos dados.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param subRota sub rota usado pelo método para cumprir a responsabilidade descrita.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 */
    private void adicionarLigacaoBidirecional(Grafo grafo, Ponto a, Ponto b, Route subRota, Vetor corrente, double vl) {
        int ia = grafo.adicionarNo(a);
        int ib = grafo.adicionarNo(b);
        double tempo = tempoRota(subRota, corrente, vl);
        grafo.adicionarLigacao(ia, ib, tempo);
        grafo.adicionarLigacao(ib, ia, tempo);
    }

    /**
 * Responsabilidade: aplicar o algoritmo de Dijkstra ao grafo e devolver a rota formada pelo caminho mínimo encontrado.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param destino porto de chegada pretendido.
 * @return Optional com a rota encontrada; Optional.empty() quando não existe rota válida.
 */
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

    /**
 * Responsabilidade: reconstruir a sequência de pontos a partir do vetor de predecessores.
 * @param grafo estrutura de nós e ligações usada pelo algoritmo de caminho mínimo.
 * @param anterior anterior usado pelo método para cumprir a responsabilidade descrita.
 * @param destino porto de chegada pretendido.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<Ponto> reconstruirCaminho(Grafo grafo, int[] anterior, int destino) {
        List<Ponto> caminho = new ArrayList<>();
        for (int atual = destino; atual != -1; atual = anterior[atual]) {
            caminho.add(0, grafo.no(atual));
        }
        return caminho;
    }

    /**
 * Responsabilidade: guardar um ponto apenas se ainda não existir ponto equivalente na lista.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @param p ponto analisado, acrescentado ou convertido.
 */
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

        /**
 * Responsabilidade: adicionar no à estrutura respetiva mantendo a consistência dos dados.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
        private int adicionarNo(Ponto p) {
            int i = indiceNo(p);
            if (i != -1) {
                return i;
            }
            nos.add(p);
            adjacencias.add(new ArrayList<>());
            return nos.size() - 1;
        }

        /**
 * Responsabilidade: adicionar ligacao à estrutura respetiva mantendo a consistência dos dados.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param destino porto de chegada pretendido.
 * @param tempo tempo usado pelo método para cumprir a responsabilidade descrita.
 */
        private void adicionarLigacao(int origem, int destino, double tempo) {
            adjacencias.get(origem).add(new Ligacao(destino, tempo));
        }

        /**
 * Responsabilidade: realizar a operação indice no no contexto da classe CalcularRota.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
        private int indiceNo(Ponto p) {
            for (int i = 0; i < nos.size(); i++) {
                if (nos.get(i).igual(p)) {
                    return i;
                }
            }
            return -1;
        }

        /**
 * Responsabilidade: realizar a operação no no contexto da classe CalcularRota.
 * @param indice índice do elemento a obter ou configurar.
 * @return ponto calculado ou guardado pela instância.
 */
        private Ponto no(int indice) {
            return nos.get(indice);
        }

        /**
 * Responsabilidade: realizar a operação numero nos no contexto da classe CalcularRota.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
        private int numeroNos() {
            return nos.size();
        }

        /**
 * Responsabilidade: realizar a operação ligacoes de no contexto da classe CalcularRota.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
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
