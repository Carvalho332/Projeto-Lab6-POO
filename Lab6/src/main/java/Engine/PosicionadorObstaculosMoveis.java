package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsabilidade: posicionar obstáculos móveis no início de cada simulação.
 *
 * Os obstáculos criados ficam centrados sobre rotas, garantindo que intersectam rotas.
 */
public class PosicionadorObstaculosMoveis {
    private static final double RAIO_DEFAULT = 1.0;

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
            Route rota = rotas.get(random.nextInt(rotas.size()));
            double comprimento = rota.comprimento();

            // Evita os extremos da rota para não colocar o obstáculo exatamente dentro do porto.
            double distancia = comprimento * (0.15 + 0.70 * random.nextDouble());
            Ponto centro = rota.position(1.0, distancia);

            resultado.add(new ObstaculoMovel(centro, RAIO_DEFAULT));
        }

        return resultado;
    }
}
