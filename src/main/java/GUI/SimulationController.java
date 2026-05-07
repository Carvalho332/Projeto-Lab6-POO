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
        this.simulador = simuladorFactory.apply(seedAtual);
        this.timer = new Timer(600, e -> passo());
        iniciar();
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        atualizarEstadoBotoes();
    }

    public void iniciar() {
        pararTimer();
        estadoAtual = simulador.iniciar();
        atualizarViews();
    }

    public void passo() {
        estadoAtual = simulador.passo();
        atualizarViews();
    }

    public void alternarExecucaoAutomatica() {
        if (timer.isRunning()) {
            pararTimer();
        } else {
            timer.start();
        }
        atualizarEstadoBotoes();
    }

    /**
     * Volta ao tempo zero mantendo exatamente os mesmos dados da simulação atual:
     * mesma corrente, mesmos obstáculos móveis e mesmas viagens.
     *
     * A forma segura de fazer isto é reconstruir um Simulador novo com a mesma seed,
     * porque o Simulador remove viagens das listas dos portos quando os navios saem.
     */
    public void reset() {
        pararTimer();
        simulador = simuladorFactory.apply(seedAtual);
        estadoAtual = simulador.iniciar();
        atualizarViews();
    }

    /**
     * Gera uma simulação realmente nova: nova corrente, novos obstáculos móveis
     * e novas viagens.
     */
    public void novaSimulacao() {
        pararTimer();
        seedAtual = gerarNovaSeed();
        simulador = simuladorFactory.apply(seedAtual);
        estadoAtual = simulador.iniciar();
        atualizarViews();
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
            controlPanel.atualizarTextoPlay(isRunning());
        }
    }
}
