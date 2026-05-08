package GUI;

import Engine.EstadoSimulacao;
import Engine.Simulador;

import javax.swing.Timer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongFunction;

/**
 * Responsabilidade: fazer a ponte entre os botões do GUI e o Engine.Simulador.
 */
public class SimulationController {
    private final LongFunction<Simulador> simuladorFactory;
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private ControlPanel controlPanel;

    private Simulador simulador;
    private EstadoSimulacao estadoAtual;
    private long seedAtual;
    private final Timer timer;

    public SimulationController(LongFunction<Simulador> simuladorFactory, MapPanel mapPanel, StatusPanel statusPanel) {
        if (simuladorFactory == null || mapPanel == null || statusPanel == null) {
            throw new IllegalArgumentException("SimulationController: argumentos invalidos");
        }
        this.simuladorFactory = simuladorFactory;
        this.mapPanel = mapPanel;
        this.statusPanel = statusPanel;
        this.seedAtual = gerarNovaSeed();
        this.timer = new Timer(600, e -> passo());
        reiniciarComSeed(seedAtual);
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        atualizarEstadoBotoes();
    }

    public void iniciar() {
        reiniciarComSeed(seedAtual);
    }

    public void passo() {
        estadoAtual = simulador.passo();
        atualizarViews();
        if (simulador.terminou()) {
            pararTimer();
            atualizarEstadoBotoes();
        }
    }

    /**
     * Recuar um passo é feito por replay determinístico: recria-se a simulação
     * com a mesma seed e executa-se até ao tempo anterior. Assim evita-se ter de
     * desfazer manualmente navios, viagens, portos e colisões.
     */
    public void passoAtras() {
        if (!podeAndarParaTras()) {
            return;
        }

        pararTimer();
        int tempoDestino = estadoAtual.getTempoAtual() - 1;
        voltarParaTempo(tempoDestino);
    }

    public boolean podeAndarParaTras() {
        return estadoAtual != null && estadoAtual.getTempoAtual() > 0;
    }

    public void alternarExecucaoAutomatica() {
        if (timer.isRunning()) {
            pararTimer();
        } else {
            timer.start();
        }
        atualizarEstadoBotoes();
    }

    public void reset() {
        reiniciarComSeed(seedAtual);
    }

    public void novaSimulacao() {
        reiniciarComSeed(gerarNovaSeed());
    }

    public void setDelay(int millis) {
        timer.setDelay(millis);
    }

    public void setMostrarGrelha(boolean mostrarGrelha) {
        mapPanel.setMostrarGrelha(mostrarGrelha);
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    public EstadoSimulacao getEstadoAtual() {
        return estadoAtual;
    }

    public long getSeedAtual() {
        return seedAtual;
    }

    private void voltarParaTempo(int tempoDestino) {
        if (tempoDestino < 0) {
            throw new IllegalArgumentException("SimulationController.voltarParaTempo: tempo invalido");
        }

        simulador = simuladorFactory.apply(seedAtual);
        estadoAtual = simulador.iniciar();

        for (int i = 0; i < tempoDestino; i++) {
            estadoAtual = simulador.passo();
        }

        atualizarViews();
    }

    private void reiniciarComSeed(long seed) {
        pararTimer();
        seedAtual = seed;
        simulador = simuladorFactory.apply(seedAtual);
        estadoAtual = simulador.iniciar();
        atualizarViews();
    }

    private long gerarNovaSeed() {
        return ThreadLocalRandom.current().nextLong();
    }

    private void pararTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }

    private void atualizarViews() {
        mapPanel.setEstado(estadoAtual);
        statusPanel.setEstado(estadoAtual);
        atualizarEstadoBotoes();
    }

    private void atualizarEstadoBotoes() {
        if (controlPanel != null) {
            controlPanel.atualizarEstadoBotoes(isRunning(), podeAndarParaTras());
        }
    }
}
