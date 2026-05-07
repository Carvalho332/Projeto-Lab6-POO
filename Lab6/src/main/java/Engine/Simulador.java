package Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade: coordenar os passos da simulação.
 */
public class Simulador {
    private final MapaNavegacao mapa;
    private final Vetor corrente;
    private int tempoAtual;
    private final List<Navio> naviosAtivos;
    private final CalcularRota calcularRota;
    private final GestorColisoes gestorColisoes;

    public Simulador(MapaNavegacao mapa, Vetor corrente) {
        if (mapa == null || corrente == null) {
            throw new IllegalArgumentException("Simulador:iv");
        }
        this.mapa = mapa;
        this.corrente = corrente;
        this.tempoAtual = 0;
        this.naviosAtivos = new ArrayList<>();
        this.calcularRota = new CalcularRota();
        this.gestorColisoes = new GestorColisoes();
    }

    public EstadoSimulacao iniciar() {
        tempoAtual = 0;
        naviosAtivos.clear();
        return criarEstado();
    }

    public EstadoSimulacao passo() {
        criarNaviosQueDevemSair();
        gestorColisoes.resolver(naviosAtivos);

        for (Navio n : naviosAtivos) {
            n.avancar();
        }

        removerNaviosQueChegaram();
        tempoAtual++;

        return criarEstado();
    }

    public int getTempoAtual() {
        return tempoAtual;
    }

    private void criarNaviosQueDevemSair() {
        List<SaidaProgramada> saidas = new ArrayList<>();

        for (Porto origem : mapa.getPortos()) {
            for (Viagem viagem : origem.getListaEspera()) {
                if (viagem.getTempoSaida() == tempoAtual) {
                    saidas.add(new SaidaProgramada(origem, viagem));
                }
            }
        }

        for (SaidaProgramada s : saidas) {
            Route rota = calcularRota.rotaMaisRapida(s.origem, s.viagem.getDestino(), mapa, corrente,
                    s.viagem.getVelocidadeLinear());

            if (rota != null) {
                String codigoViagem = s.origem.getNome() + s.viagem.getTempoSaida();
                naviosAtivos.add(new Navio(codigoViagem, s.origem, s.viagem, rota));
                s.origem.removerViagem(s.viagem);
            }
        }
    }

    private void removerNaviosQueChegaram() {
        naviosAtivos.removeIf(n -> n.getEstado() == EstadoNavio.CHEGOU || n.chegouDestino());
    }

    private EstadoSimulacao criarEstado() {
        List<InfoNavio> infosNavios = new ArrayList<>();
        for (Navio n : naviosAtivos) {
            infosNavios.add(new InfoNavio(n));
        }

        List<InfoPorto> infosPortos = new ArrayList<>();
        for (Porto p : mapa.getPortos()) {
            infosPortos.add(new InfoPorto(p));
        }

        return new EstadoSimulacao(
                tempoAtual,
                corrente,
                infosNavios,
                infosPortos,
                mapa.getRotas(),
                mapa.getTodosObstaculos()
        );
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
