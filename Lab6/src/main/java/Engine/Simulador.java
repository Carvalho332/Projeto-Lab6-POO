package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade: coordenar a evolução temporal da simulação, criando navios, calculando rotas, resolvendo colisões e gerando estados para o GUI.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class Simulador {
    private final MapaNavegacao mapa;
    private final Vetor corrente;
    private int tempoAtual;
    private final List<Navio> naviosAtivos;
    private final EstrategiaCalculoRota estrategiaCalculoRota;
    private final GestorColisoes gestorColisoes;

    /**
 * Responsabilidade: construir uma instância de Simulador, validando os dados recebidos para preservar os invariantes.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 */
    public Simulador(MapaNavegacao mapa, Vetor corrente) {
        this(mapa, corrente, new CalcularRota());
    }

    /**
 * Responsabilidade: construir uma instância de Simulador, validando os dados recebidos para preservar os invariantes.
 * @param mapa mapa de navegação com portos, rotas e obstáculos.
 * @param corrente vetor da corrente usado para compensar o movimento do navio.
 * @param estrategiaCalculoRota estrategia calculo rota usado pelo método para cumprir a responsabilidade descrita.
 */
    public Simulador(MapaNavegacao mapa, Vetor corrente, EstrategiaCalculoRota estrategiaCalculoRota) {
        if (mapa == null || corrente == null || estrategiaCalculoRota == null) {
            throw new IllegalArgumentException("Simulador:iv");
        }
        this.mapa = mapa;
        this.corrente = corrente;
        this.tempoAtual = 0;
        this.naviosAtivos = new ArrayList<>();
        this.estrategiaCalculoRota = estrategiaCalculoRota;
        this.gestorColisoes = new GestorColisoes();
    }

    /**
 * Responsabilidade: preparar o primeiro estado da simulação para apresentação no GUI.
 * @return objeto resultante da operação.
 */
    public EstadoSimulacao iniciar() {
        tempoAtual = 0;
        naviosAtivos.clear();
        return criarEstado();
    }

    /**
 * Responsabilidade: avançar a simulação uma unidade temporal e devolver o novo estado.
 * @return objeto resultante da operação.
 */
    public EstadoSimulacao passo() {
        removerNaviosFinalizadosDoPassoAnterior();
        criarNaviosQueDevemSair();
        gestorColisoes.resolver(naviosAtivos);
        avancarNavios();
        tempoAtual++;
        return criarEstado();
    }

    /**
 * Responsabilidade: devolver tempo atual associado à instância atual.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getTempoAtual() {
        return tempoAtual;
    }

    /**
 * Responsabilidade: indicar se a simulação já não possui navios ativos nem viagens pendentes.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean terminou() {
        return naviosAtivos.isEmpty() && !haViagensPendentes();
    }

    /**
 * Responsabilidade: criar os navios cujas viagens começam no tempo atual.
 */
    private void criarNaviosQueDevemSair() {
        for (SaidaProgramada saida : obterSaidasDoTempoAtual()) {
            criarNavio(saida);
        }
    }

    /**
 * Responsabilidade: realizar a operação obter saidas do tempo atual no contexto da classe Simulador.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<SaidaProgramada> obterSaidasDoTempoAtual() {
        List<SaidaProgramada> saidas = new ArrayList<>();
        for (Porto origem : mapa.getPortos()) {
            for (Viagem viagem : origem.getViagensParaSairNoTempo(tempoAtual)) {
                saidas.add(new SaidaProgramada(origem, viagem));
            }
        }
        return saidas;
    }

    /**
 * Responsabilidade: criar navio com a configuração necessária.
 * @param saida saida usado pelo método para cumprir a responsabilidade descrita.
 */
    private void criarNavio(SaidaProgramada saida) {
        estrategiaCalculoRota.calcular(
                saida.origem,
                saida.viagem.getDestino(),
                mapa,
                corrente,
                saida.viagem.getVelocidadeLinear()
        ).ifPresent(rota -> {
            naviosAtivos.add(new Navio(gerarCodigoViagem(saida.origem, saida.viagem), saida.origem, saida.viagem, rota));
            saida.origem.removerViagem(saida.viagem);
        });
    }

    /**
 * Responsabilidade: realizar a operação gerar codigo viagem no contexto da classe Simulador.
 * @param origem porto de partida da viagem ou do cálculo de rota.
 * @param viagem viagem programada ou apresentada.
 * @return texto formatado ou identificador pedido.
 */
    private String gerarCodigoViagem(Porto origem, Viagem viagem) {
        return origem.getNome() + viagem.getTempoSaida();
    }

    /**
 * Responsabilidade: atualizar a posição dos navios que podem mover-se neste passo.
 */
    private void avancarNavios() {
        for (Navio n : naviosAtivos) {
            n.avancar();
        }
    }

    /**
 * Responsabilidade: remover navios que já foram apresentados como chegados no passo anterior.
 */
    private void removerNaviosFinalizadosDoPassoAnterior() {
        naviosAtivos.removeIf(Navio::chegou);
    }

    /**
 * Responsabilidade: realizar a operação ha viagens pendentes no contexto da classe Simulador.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean haViagensPendentes() {
        for (Porto porto : mapa.getPortos()) {
            if (porto.temViagensEmEspera()) {
                return true;
            }
        }
        return false;
    }

    /**
 * Responsabilidade: criar um DTO com os dados atuais necessários ao GUI.
 * @return objeto resultante da operação.
 */
    private EstadoSimulacao criarEstado() {
        return new EstadoSimulacao(
                tempoAtual,
                corrente,
                criarInfoNavios(),
                criarInfoPortos(),
                mapa.getRotas(),
                mapa.getTodosObstaculos()
        );
    }

    /**
 * Responsabilidade: criar info navios com a configuração necessária.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<InfoNavio> criarInfoNavios() {
        List<InfoNavio> infosNavios = new ArrayList<>();
        for (Navio n : naviosAtivos) {
            infosNavios.add(new InfoNavio(n, corrente));
        }
        return infosNavios;
    }

    /**
 * Responsabilidade: criar info portos com a configuração necessária.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<InfoPorto> criarInfoPortos() {
        List<InfoPorto> infosPortos = new ArrayList<>();
        for (Porto p : mapa.getPortos()) {
            infosPortos.add(new InfoPorto(p));
        }
        return infosPortos;
    }

    private static class SaidaProgramada {
        private final Porto origem;
        private final Viagem viagem;

        private SaidaProgramada(Porto origem, Viagem viagem) {
            this.origem = origem;
            this.viagem = viagem;
        }
    }
}
