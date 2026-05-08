package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade: coordenar os passos da simulação.
 *
 * <p>Funciona como Facade do Engine para o GUI: a interface externa fica reduzida
 * a iniciar, avançar um passo e obter estados de simulação.</p>
 */
public class Simulador {
    private final MapaNavegacao mapa;
    private final Vetor corrente;
    private int tempoAtual;
    private final List<Navio> naviosAtivos;
    private final EstrategiaCalculoRota estrategiaCalculoRota;
    private final GestorColisoes gestorColisoes;

    public Simulador(MapaNavegacao mapa, Vetor corrente) {
        this(mapa, corrente, new CalcularRota());
    }

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

    public EstadoSimulacao iniciar() {
        tempoAtual = 0;
        naviosAtivos.clear();
        return criarEstado();
    }

    public EstadoSimulacao passo() {
        removerNaviosFinalizadosDoPassoAnterior();
        criarNaviosQueDevemSair();
        gestorColisoes.resolver(naviosAtivos);
        avancarNavios();
        tempoAtual++;
        return criarEstado();
    }

    public int getTempoAtual() {
        return tempoAtual;
    }

    public boolean terminou() {
        return naviosAtivos.isEmpty() && !haViagensPendentes();
    }

    private void criarNaviosQueDevemSair() {
        for (SaidaProgramada saida : obterSaidasDoTempoAtual()) {
            criarNavio(saida);
        }
    }

    private List<SaidaProgramada> obterSaidasDoTempoAtual() {
        List<SaidaProgramada> saidas = new ArrayList<>();
        for (Porto origem : mapa.getPortos()) {
            for (Viagem viagem : origem.getViagensParaSairNoTempo(tempoAtual)) {
                saidas.add(new SaidaProgramada(origem, viagem));
            }
        }
        return saidas;
    }

    private void criarNavio(SaidaProgramada saida) {
        estrategiaCalculoRota.calcular(
                saida.origem,
                saida.viagem.getDestino(),
                mapa,
                corrente,
                saida.viagem.getVelocidadeLinear()
        ).ifPresent(rota -> {
            naviosAtivos.add(new Navio(gerarCodigoViagem(saida.origem, saida.viagem),
                    saida.origem, saida.viagem, rota));
            saida.origem.removerViagem(saida.viagem);
        });
    }

    private String gerarCodigoViagem(Porto origem, Viagem viagem) {
        return origem.getNome() + viagem.getTempoSaida();
    }

    private void avancarNavios() {
        for (Navio n : naviosAtivos) {
            n.avancar();
        }
    }

    /**
     * Os navios que chegam permanecem visíveis no estado devolvido pelo passo em
     * que chegaram. Só são removidos no passo seguinte.
     */
    private void removerNaviosFinalizadosDoPassoAnterior() {
        naviosAtivos.removeIf(Navio::chegou);
    }

    private boolean haViagensPendentes() {
        for (Porto porto : mapa.getPortos()) {
            if (porto.temViagensEmEspera()) {
                return true;
            }
        }
        return false;
    }

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

    private List<InfoNavio> criarInfoNavios() {
        List<InfoNavio> infosNavios = new ArrayList<>();
        for (Navio n : naviosAtivos) {
            infosNavios.add(new InfoNavio(n, corrente));
        }
        return infosNavios;
    }

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
