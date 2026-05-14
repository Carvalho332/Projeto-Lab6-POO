package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsabilidade: colocar obstáculos móveis sobre rotas em posições diferentes para cada simulação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class PosicionadorObstaculosMoveis {
    private static final double RAIO_DEFAULT = 1.0;
    private static final double DISTANCIA_MINIMA_ENTRE_CENTROS = 0.5;
    private static final int MAX_TENTATIVAS = 100;

    private final Random random;

    /**
 * Responsabilidade: construir uma instância de PosicionadorObstaculosMoveis, validando os dados recebidos para preservar os invariantes.
 */
    public PosicionadorObstaculosMoveis() {
        this(new Random());
    }

    /**
 * Responsabilidade: construir uma instância de PosicionadorObstaculosMoveis, validando os dados recebidos para preservar os invariantes.
 * @param random gerador pseudoaleatório usado para variar o cenário.
 */
    public PosicionadorObstaculosMoveis(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("PosicionadorObstaculosMoveis: random null");
        }
        this.random = random;
    }

    /**
 * Responsabilidade: realizar a operação posicionar no contexto da classe PosicionadorObstaculosMoveis.
 * @param rotas rotas disponíveis no cenário ou no cálculo.
 * @param quantidade número de obstáculos móveis a criar.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
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

    /**
 * Responsabilidade: criar obstaculo diferente com a configuração necessária.
 * @param rotas rotas disponíveis no cenário ou no cálculo.
 * @param existentes existentes usado pelo método para cumprir a responsabilidade descrita.
 * @return objeto resultante da operação.
 */
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

    /**
 * Responsabilidade: realizar a operação ponto aleatorio sobre rota no contexto da classe PosicionadorObstaculosMoveis.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @return ponto calculado ou guardado pela instância.
 */
    private Ponto pontoAleatorioSobreRota(Route rota) {
        double comprimento = rota.comprimento();
        double distancia = comprimento * (0.15 + 0.70 * random.nextDouble());
        return rota.position(1.0, distancia);
    }

    /**
 * Responsabilidade: realizar a operação centro diferente dos existentes no contexto da classe PosicionadorObstaculosMoveis.
 * @param centro centro usado pelo método para cumprir a responsabilidade descrita.
 * @param existentes existentes usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean centroDiferenteDosExistentes(Ponto centro, List<ObstaculoMovel> existentes) {
        for (ObstaculoMovel existente : existentes) {
            if (centro.dist(existente.getCentro()) < DISTANCIA_MINIMA_ENTRE_CENTROS) {
                return false;
            }
        }
        return true;
    }
}
