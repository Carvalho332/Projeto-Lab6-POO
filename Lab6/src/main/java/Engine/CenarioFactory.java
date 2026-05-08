package Engine;

import java.util.Random;

/**
 * Factory responsável por construir o cenário de demonstração do Lab 6.
 *
 * <p>Esta classe pertence ao Engine porque cria dados de domínio: mapa, portos,
 * rotas, obstáculos, corrente e viagens. O GUI apenas recebe o Simulador já
 * criado e apresenta o estado recebido.</p>
 *
 * @version 2026-05-08
 */
public final class CenarioFactory {
    private static final double ESCALA_MAPA = 2.2;
    private static final int NUMERO_OBSTACULOS_MOVEIS = 2;

    private CenarioFactory() {
    }

    public static long novaSeed() {
        return System.nanoTime();
    }

    public static Simulador criarSimuladorDemo() {
        return criarSimuladorDemo(novaSeed());
    }

    public static Simulador criarSimuladorDemo(long seed) {
        Random random = new Random(seed);
        MapaNavegacao mapa = criarMapaDemo(random);
        Vetor corrente = criarCorrenteAleatoria(random);
        return new Simulador(mapa, corrente);
    }

    public static MapaNavegacao criarMapaDemo() {
        return criarMapaDemo(new Random());
    }

    public static MapaNavegacao criarMapaDemo(long seed) {
        return criarMapaDemo(new Random(seed));
    }

    private static MapaNavegacao criarMapaDemo(Random random) {
        MapaNavegacao mapa = new MapaNavegacao();
        Porto[] portos = criarPortos(mapa);
        adicionarRotas(mapa, portos);
        adicionarObstaculosFixos(mapa);
        adicionarObstaculosMoveisAleatorios(mapa, random);
        adicionarViagensAleatorias(portos, random);
        return mapa;
    }

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

    private static Vetor criarCorrenteAleatoria(Random random) {
        double x = randomDouble(random, -0.40, 0.40);
        double y = randomDouble(random, -0.40, 0.40);
        return new Vetor(x, y);
    }

    private static void adicionarObstaculosFixos(MapaNavegacao mapa) {
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(13, 10), p(13.4, 10), p(13.5, 12.2), p(12.9, 12.2)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(12.7, 12.2), p(13.7, 12.2), p(13.7, 12.7), p(12.7, 12.7)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(12.6, 12.7), p(13.2, 13.3), p(13.8, 12.7)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(13, 7), p(14, 6), p(15.2, 6.4), p(15.6, 7.4), p(14.8, 8.1), p(13.5, 8.0), p(12.8, 7.5)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(0, 12), p(-1.5, 10), p(-0.8, 8.7), p(1, 8), p(1.5, 10.5)}));
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{p(2, 15), p(4, 15), p(5, 16), p(4.5, 17.5), p(3, 18), p(1.7, 17), p(1.2, 16)}));
    }

    private static void adicionarObstaculosMoveisAleatorios(MapaNavegacao mapa, Random random) {
        PosicionadorObstaculosMoveis posicionador = new PosicionadorObstaculosMoveis(random);
        mapa.definirObstaculosMoveis(posicionador.posicionar(mapa.getRotas(), NUMERO_OBSTACULOS_MOVEIS));
    }

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

    private static Viagem criarViagemAleatoria(Porto[] portos, Porto origem, Random random) {
        Porto destino = escolherDestinoDiferente(portos, origem, random);
        int tempoSaida = 1 + random.nextInt(15);
        double velocidadeLinear = randomDouble(random, 0.80, 1.60);
        return new Viagem(tempoSaida, destino, velocidadeLinear);
    }

    private static Porto escolherDestinoDiferente(Porto[] portos, Porto origem, Random random) {
        Porto destino;
        do {
            destino = portos[random.nextInt(portos.length)];
        } while (destino == origem);
        return destino;
    }

    private static double randomDouble(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private static Route rota(Ponto... pontos) {
        return new Route(pontos);
    }

    private static Ponto p(double x, double y) {
        return new Ponto(x * ESCALA_MAPA, y * ESCALA_MAPA);
    }
}
