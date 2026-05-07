package GUI;

import Engine.MapaNavegacao;
import Engine.ObstaculoMovel;
import Engine.Ponto;
import Engine.Poligono;
import Engine.Porto;
import Engine.Route;
import Engine.Simulador;
import Engine.Vetor;
import Engine.Viagem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsabilidade: construir um cenário de demonstração para testar o simulador na prática.
 */
public final class Cenario {

    /*
     * AUMENTA O ESPAÇAMENTO DO MAPA.
     *
     * Quanto maior este valor:
     * - mais afastados ficam os elementos;
     * - mais visível fica o mapa;
     * - mantém exatamente o mesmo desenho/layout.
     */
    private static final double ESCALA = 1.5;

    private Cenario() {
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

    private static MapaNavegacao criarMapaDemo(Random random) {
        MapaNavegacao mapa = new MapaNavegacao();

        /*
         * PORTOS
         */
        Porto a = new Porto("A", p(2, 13));
        Porto b = new Porto("B", p(0, 2));
        Porto c = new Porto("C", p(16, 14));
        Porto d = new Porto("D", p(20, 4));
        Porto e = new Porto("E", p(10, 0));

        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarPorto(c);
        mapa.adicionarPorto(d);
        mapa.adicionarPorto(e);

        /*
         * ROTAS
         */

        // Rota preta D -> C
        Route dc = new Route(new Ponto[]{
                d.getPosicao(),
                p(4, 4),
                p(7, 19),
                c.getPosicao()
        });

        // Rota azul B -> C
        Route bc = new Route(new Ponto[]{
                b.getPosicao(),
                p(6, 2),
                p(10, 14),
                c.getPosicao()
        });

        // Rota vermelha A -> D
        Route ad = new Route(new Ponto[]{
                a.getPosicao(),
                p(3, 6),
                p(15, 9),
                d.getPosicao()
        });

        // Rota roxa A -> E
        Route ae = new Route(new Ponto[]{
                a.getPosicao(),
                p(7, 14),
                p(8, 16),
                e.getPosicao()
        });

        List<Route> rotas = new ArrayList<>();
        rotas.add(dc);
        rotas.add(bc);
        rotas.add(ad);
        rotas.add(ae);

        for (Route rota : rotas) {
            mapa.adicionarRota(rota);
        }

        adicionarObstaculosFixos(mapa);
        adicionarObstaculosMoveisAleatorios(mapa, random);
        adicionarViagensAleatorias(new Porto[]{a, b, c, d, e}, random);

        return mapa;
    }

    private static Vetor criarCorrenteAleatoria(Random random) {
        double x = randomDouble(random, -0.40, 0.40);
        double y = randomDouble(random, -0.40, 0.40);

        return new Vetor(x, y);
    }

    private static void adicionarObstaculosFixos(MapaNavegacao mapa) {

        // Polígono superior direito
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{
                p(13, 10),
                p(15, 11),
                p(14, 13),
                p(12, 12)
        }));

        // Retângulo
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{
                p(13, 7),
                p(13, 6),
                p(15, 6),
                p(15, 7)
        }));

        // Triângulo
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{
                p(0, 12),
                p(-2, 9),
                p(1, 8)
        }));

        // Polígono superior esquerdo
        mapa.adicionarObstaculoFixo(new Poligono(new Ponto[]{
                p(2, 15),
                p(4, 15),
                p(4.5, 16.5),
                p(3, 18),
                p(1.5, 16.5)
        }));
    }

    private static void adicionarObstaculosMoveisAleatorios(MapaNavegacao mapa, Random random) {

        List<ObstaculoMovel> moveis = new ArrayList<>();
        List<Ponto> pontos = new ArrayList<>();

        /*
         * INTERSETAM ROTAS
         */
        pontos.add(p(15, 4));
        pontos.add(p(5, 11));
        pontos.add(p(3, 2));
        pontos.add(p(2.5, 10));
        pontos.add(p(11, 4.5));
        pontos.add(p(11.5, 3.5));
        pontos.add(p(10, 13));
        pontos.add(p(7, 14));
        pontos.add(p(8, 8));
        pontos.add(p(4, 14));

        /*
         * NÃO INTERSETAM
         */
        pontos.add(p(0, 14));
        pontos.add(p(14, 2));
        pontos.add(p(17, 6));
        pontos.add(p(18, 11));
        pontos.add(p(17, 15));
        pontos.add(p(1, 3));
        pontos.add(p(8, 1));
        pontos.add(p(7, 11));
        pontos.add(p(14, 16));
        pontos.add(p(19, 15));

        Ponto pontoInterseta = pontos.get(random.nextInt(10));

        Ponto pontoNaoInterseta =
                pontos.get(random.nextInt(pontos.size() - 10) + 10);

        moveis.add(new ObstaculoMovel(pontoInterseta, 1.6));
        moveis.add(new ObstaculoMovel(pontoNaoInterseta, 1.2));

        mapa.definirObstaculosMoveis(moveis);
    }

    private static Ponto pontoAleatorioSobreRota(Route rota, Random random) {

        int numeroSegmentos = rota.getNumeroPontos() - 1;

        if (numeroSegmentos <= 0) {
            throw new IllegalArgumentException("A rota precisa de pelo menos dois pontos.");
        }

        int segmento = random.nextInt(numeroSegmentos);

        Ponto origem = rota.getPonto(segmento);
        Ponto destino = rota.getPonto(segmento + 1);

        double t = randomDouble(random, 0.20, 0.80);

        double x = origem.getX() + t * (destino.getX() - origem.getX());
        double y = origem.getY() + t * (destino.getY() - origem.getY());

        return new Ponto(x, y);
    }

    private static void adicionarViagensAleatorias(Porto[] portos, Random random) {

        for (Porto origem : portos) {

            for (int i = 0; i < 2; i++) {

                Porto destino =
                        escolherDestinoDiferente(portos, origem, random);

                int tempoSaida = 1 + random.nextInt(15);

                double velocidadeLinear =
                        randomDouble(random, 0.80, 1.60);

                origem.adicionarViagem(
                        new Viagem(
                                tempoSaida,
                                destino,
                                velocidadeLinear
                        )
                );
            }
        }

        int viagensExtra = random.nextInt(4);

        for (int i = 0; i < viagensExtra; i++) {

            Porto origem = portos[random.nextInt(portos.length)];

            Porto destino =
                    escolherDestinoDiferente(portos, origem, random);

            int tempoSaida = 1 + random.nextInt(15);

            double velocidadeLinear =
                    randomDouble(random, 0.80, 1.60);

            origem.adicionarViagem(
                    new Viagem(
                            tempoSaida,
                            destino,
                            velocidadeLinear
                    )
            );
        }
    }

    private static Porto escolherDestinoDiferente(
            Porto[] portos,
            Porto origem,
            Random random
    ) {

        Porto destino;

        do {
            destino = portos[random.nextInt(portos.length)];
        }
        while (destino == origem);

        return destino;
    }

    private static double randomDouble(Random random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    /*
     * CONVERTE AS COORDENADAS ORIGINAIS
     * PARA UM MAPA MAIS ESPAÇADO.
     *
     * NÃO É UMA MULTIPLICAÇÃO SIMPLES.
     * Isto aumenta realmente as distâncias visuais.
     */
    private static Ponto p(double x, double y) {

        double novoX = x * ESCALA + x * 0.7;
        double novoY = y * ESCALA + y * 0.7;

        return new Ponto(novoX, novoY);
    }
}