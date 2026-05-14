package Engine;

import java.util.Random;

/**
 * Responsabilidade: construir cenários de demonstração com portos, rotas, obstáculos, viagens, corrente e simulador configurado.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public final class CenarioFactory {
    private static final double ESCALA_MAPA = 2.2;
    private static final int NUMERO_OBSTACULOS_MOVEIS = 2;

    /**
 * Responsabilidade: construir uma instância de CenarioFactory, validando os dados recebidos para preservar os invariantes.
 */
    private CenarioFactory() {
    }

    /**
 * Responsabilidade: realizar a operação nova seed no contexto da classe CenarioFactory.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public static long novaSeed() {
        return System.nanoTime();
    }

    /**
 * Responsabilidade: criar um simulador completo para demonstração do Lab 6.
 * @return objeto resultante da operação.
 */
    public static Simulador criarSimuladorDemo() {
        return criarSimuladorDemo(novaSeed());
    }

    /**
 * Responsabilidade: criar um simulador completo para demonstração do Lab 6.
 * @param seed semente usada para reproduzir a simulação.
 * @return objeto resultante da operação.
 */
    public static Simulador criarSimuladorDemo(long seed) {
        return criarSimuladorDemo(seed, criarCorrenteDemo(seed));
    }

    /**
 * Responsabilidade: criar um simulador completo usando uma corrente indicada pelo utilizador.
 * @param seed semente usada para reproduzir o mapa, obstáculos móveis e viagens.
 * @param corrente vetor da corrente introduzido ou escolhido para a simulação.
 * @return simulador configurado com o cenário e a corrente recebida.
 */
    public static Simulador criarSimuladorDemo(long seed, Vetor corrente) {
        if (corrente == null) {
            throw new IllegalArgumentException("CenarioFactory.criarSimuladorDemo: corrente null");
        }
        Random random = new Random(seed);
        MapaNavegacao mapa = criarMapaDemo(random);
        return new Simulador(mapa, corrente);
    }

    /**
 * Responsabilidade: criar uma corrente inicial reprodutível a partir de uma semente.
 * @param seed semente usada para reproduzir a corrente inicial.
 * @return vetor da corrente inicial.
 */
    public static Vetor criarCorrenteDemo(long seed) {
        return criarCorrenteAleatoria(new Random(seed ^ 0x5DEECE66DL));
    }

    /**
 * Responsabilidade: criar o mapa de demonstração com portos, rotas e obstáculos.
 * @return objeto resultante da operação.
 */
    public static MapaNavegacao criarMapaDemo() {
        return criarMapaDemo(new Random());
    }

    /**
 * Responsabilidade: criar o mapa de demonstração com portos, rotas e obstáculos.
 * @param seed semente usada para reproduzir a simulação.
 * @return objeto resultante da operação.
 */
    public static MapaNavegacao criarMapaDemo(long seed) {
        return criarMapaDemo(new Random(seed));
    }

    /**
 * Responsabilidade: criar o mapa de demonstração com portos, rotas e obstáculos.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 * @return objeto resultante da operação.
 */
    private static MapaNavegacao criarMapaDemo(Random random) {
        MapaNavegacao mapa = new MapaNavegacao();
        Porto[] portos = criarPortos(mapa);
        adicionarRotas(mapa, portos);
        adicionarObstaculosFixos(mapa);
        adicionarObstaculosMoveisAleatorios(mapa, random);
        adicionarViagensAleatorias(portos, random);
        return mapa;
    }

    /**
 * Responsabilidade: criar e registar os portos usados no cenário.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @return array com os elementos calculados ou copiados.
 */
    private static Porto[] criarPortos(MapaNavegacao mapa) {
        Porto a = new Porto("A", p(2, 13));
        Porto b = new Porto("B", p(0, 2));
        Porto c = new Porto("C", p(16, 14));
        Porto d = new Porto("D", p(20, 4));
        Porto e = new Porto("E", p(10, 0));

        Porto[] portos = {a, b, c, d, e};
        for (Porto porto : portos) {
            mapa.adicionarPorto(porto);
        }
        return portos;
    }

    /**
 * Responsabilidade: adicionar as rotas poligonais obrigatórias ao mapa.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param portos portos do mapa ou do estado de simulação.
 */
    private static void adicionarRotas(MapaNavegacao mapa, Porto[] portos) {
        Porto a = portos[0];
        Porto b = portos[1];
        Porto c = portos[2];
        Porto d = portos[3];
        Porto e = portos[4];

        mapa.adicionarRota(rota(d.getPosicao(), p(4, 4), p(7, 19), c.getPosicao()));
        mapa.adicionarRota(rota(b.getPosicao(), p(6, 2), p(10, 14), c.getPosicao()));
        mapa.adicionarRota(rota(a.getPosicao(), p(3, 6), p(15, 9), d.getPosicao()));
        mapa.adicionarRota(rota(a.getPosicao(), p(7, 14), p(8, 16), e.getPosicao()));
        mapa.adicionarRota(rota(b.getPosicao(), p(3, 4), p(7, 6), e.getPosicao()));
        mapa.adicionarRota(rota(a.getPosicao(), p(5, 12), p(11, 15), c.getPosicao()));
    }

    /**
 * Responsabilidade: criar corrente aleatoria com a configuração necessária.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 * @return vetor resultante da operação.
 */
    private static Vetor criarCorrenteAleatoria(Random random) {
        double x = randomDouble(random, -0.40, 0.40);
        double y = randomDouble(random, -0.40, 0.40);
        return new Vetor(x, y);
    }

    /**
 * Responsabilidade: adicionar os obstáculos fixos do cenário ao mapa.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 */
    private static void adicionarObstaculosFixos(MapaNavegacao mapa) {
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(13, 10), p(13.4, 10), p(13.5, 12.2), p(12.9, 12.2)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(12.7, 12.2), p(13.7, 12.2), p(13.7, 12.7), p(12.7, 12.7)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(12.6, 12.7), p(13.2, 13.3), p(13.8, 12.7)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(13, 7), p(14, 6), p(15.2, 6.4), p(15.6, 7.4), p(14.8, 8.1), p(13.5, 8.0), p(12.8, 7.5)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(0, 12), p(-1.5, 10), p(-0.8, 8.7), p(1, 8), p(1.5, 10.5)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(2, 15), p(4, 15), p(5, 16), p(4.5, 17.5), p(3, 18), p(1.7, 17), p(1.2, 16)}));
    }

    /**
 * Responsabilidade: adicionar obstaculos moveis aleatorios à estrutura respetiva mantendo a consistência dos dados.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 */
    private static void adicionarObstaculosMoveisAleatorios(MapaNavegacao mapa, Random random) {
        PosicionadorObstaculosMoveis posicionador = new PosicionadorObstaculosMoveis(random);
        mapa.definirObstaculosMoveis(posicionador.posicionar(mapa.getRotas(), NUMERO_OBSTACULOS_MOVEIS));
    }

    /**
 * Responsabilidade: adicionar viagens aleatorias à estrutura respetiva mantendo a consistência dos dados.
 * @param portos portos do mapa ou do estado de simulação.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 */
    private static void adicionarViagensAleatorias(Porto[] portos, Random random) {
        for (Porto origem : portos) {
            for (int i = 0; i < 2; i++) {
                origem.adicionarViagem(criarViagemAleatoria(portos, origem, random));
            }
        }

        int viagensExtra = random.nextInt(4);
        for (int i = 0; i < viagensExtra; i++) {
            Porto origem = portos[random.nextInt(portos.length)];
            origem.adicionarViagem(criarViagemAleatoria(portos, origem, random));
        }
    }

    /**
 * Responsabilidade: criar viagem aleatoria com a configuração necessária.
 * @param portos portos do mapa ou do estado de simulação.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 * @return objeto resultante da operação.
 */
    private static Viagem criarViagemAleatoria(Porto[] portos, Porto origem, Random random) {
        Porto destino = escolherDestinoDiferente(portos, origem, random);
        int tempoSaida = 1 + random.nextInt(15);
        double velocidadeLinear = randomDouble(random, 0.80, 1.60);
        return new Viagem(tempoSaida, destino, velocidadeLinear);
    }

    /**
 * Responsabilidade: realizar a operação escolher destino diferente no contexto da classe CenarioFactory.
 * @param portos portos do mapa ou do estado de simulação.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 * @return objeto resultante da operação.
 */
    private static Porto escolherDestinoDiferente(Porto[] portos, Porto origem, Random random) {
        Porto destino;
        do {
            destino = portos[random.nextInt(portos.length)];
        } while (destino == origem);
        return destino;
    }

    /**
 * Responsabilidade: realizar a operação random double no contexto da classe CenarioFactory.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 * @param min min usado pelo método para cumprir a responsabilidade descrita.
 * @param max max usado pelo método para cumprir a responsabilidade descrita.
 * @return valor real resultante do cálculo.
 */
    private static double randomDouble(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    /**
 * Responsabilidade: realizar a operação rota no contexto da classe CenarioFactory.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @return rota calculada ou construída pela operação.
 */
    private static Route rota(Ponto... pontos) {
        return new Route(pontos);
    }

    /**
 * Responsabilidade: realizar a operação p no contexto da classe CenarioFactory.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @return ponto calculado ou guardado pela instância.
 */
    private static Ponto p(double x, double y) {
        return new Ponto(x * ESCALA_MAPA, y * ESCALA_MAPA);
    }
}
