package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsabilidade: posicionar obstáculos móveis no início de cada simulação.
 *
 * <p>Os obstáculos criados ficam centrados sobre rotas, garantindo que
 * intersectam rotas. Depois de criados, permanecem fixos durante toda a
 * simulação.</p>
 *
 * @version 2026-05-08
 * @inv random != null
 */
public class PosicionadorObstaculosMoveis {
    private static final double RAIO_DEFAULT = 1.0;
    private static final double DISTANCIA_MINIMA_ENTRE_CENTROS = 0.5;
    private static final int MAX_TENTATIVAS = 100;

    private final Random random;

    public PosicionadorObstaculosMoveis() {
        this(new Random());
    }

    public PosicionadorObstaculosMoveis(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("PosicionadorObstaculosMoveis: random null");
        }
        this.random = random;
    }

    public List<ObstaculoMovel> posicionar(List<Route> rotas, int quantidade) {
        if (rotas == null || rotas.isEmpty()) {
            throw new IllegalArgumentException("PosicionadorObstaculosMoveis.posicionar: rotas invalidas");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("PosicionadorObstaculosMoveis.posicionar: quantidade invalida");
        }

        List<ObstaculoMovel> resultado = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            resultado.add(criarObstaculoDiferente(rotas, resultado));
        }
        return resultado;
    }

    private ObstaculoMovel criarObstaculoDiferente(List<Route> rotas, List<ObstaculoMovel> existentes) {
        for (int tentativa = 0; tentativa < MAX_TENTATIVAS; tentativa++) {
            Route rota = rotas.get(random.nextInt(rotas.size()));
            Ponto centro = pontoAleatorioSobreRota(rota);
            if (centroDiferenteDosExistentes(centro, existentes)) {
                return new ObstaculoMovel(centro, RAIO_DEFAULT);
            }
        }
        throw new IllegalStateException("Nao foi possivel posicionar obstaculo movel diferente.");
    }

    private Ponto pontoAleatorioSobreRota(Route rota) {
        double comprimento = rota.comprimento();
        double distancia = comprimento * (0.15 + 0.70 * random.nextDouble());
        return rota.position(1.0, distancia);
    }

    private boolean centroDiferenteDosExistentes(Ponto centro, List<ObstaculoMovel> existentes) {
        for (ObstaculoMovel existente : existentes) {
            if (centro.dist(existente.getCentro()) < DISTANCIA_MINIMA_ENTRE_CENTROS) {
                return false;
            }
        }
        return true;
    }
}
