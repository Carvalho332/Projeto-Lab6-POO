package GUI;

import Engine.CenarioFactory;
import Engine.EstadoSimulacao;
import Engine.Simulador;
import Engine.Vetor;

import javax.swing.Timer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * Responsabilidade: ligar os eventos do GUI aos métodos do simulador e preservar a semente/corrente usadas.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 13-05-2026
 * @inv simuladorFactory != null && mapPanel != null && statusPanel != null && correnteAtual != null
 */
public class SimulationController {
    private final BiFunction<Long, Vetor, Simulador> simuladorFactory;
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private ControlPanel controlPanel;

    private Simulador simulador;
    private EstadoSimulacao estadoAtual;
    private long seedAtual;
    private Vetor correnteAtual;
    private final Timer timer;

    /**
     * Responsabilidade: criar o controlador e inicializar a primeira simulação.
     * @param simuladorFactory fábrica que cria o simulador a partir da semente e da corrente.
     * @param mapPanel painel que desenha o mapa.
     * @param statusPanel painel que apresenta as tabelas de estado.
     */
    public SimulationController(BiFunction<Long, Vetor, Simulador> simuladorFactory, MapPanel mapPanel, StatusPanel statusPanel) {
        if (simuladorFactory == null || mapPanel == null || statusPanel == null) {
            throw new IllegalArgumentException("SimulationController: argumentos invalidos");
        }
        this.simuladorFactory = simuladorFactory;
        this.mapPanel = mapPanel;
        this.statusPanel = statusPanel;
        this.seedAtual = gerarNovaSeed();
        this.correnteAtual = CenarioFactory.criarCorrenteDemo(seedAtual);
        this.timer = new Timer(600, e -> passo());
        reiniciarComSeedECorrente(seedAtual, correnteAtual);
    }

    /**
     * Responsabilidade: associar o painel de controlo ao controlador.
     * @param controlPanel painel de botões e campos de entrada da simulação.
     */
    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        atualizarEstadoBotoes();
    }

    /** Responsabilidade: preparar o primeiro estado da simulação para apresentação no GUI. */
    public void iniciar() {
        reiniciarComSeedECorrente(seedAtual, correnteAtual);
    }

    /** Responsabilidade: avançar a simulação uma unidade temporal. */
    public void passo() {
        estadoAtual = simulador.passo();
        atualizarViews();
        if (simulador.terminou()) {
            pararTimer();
            atualizarEstadoBotoes();
        }
    }

    /** Responsabilidade: reconstruir a simulação no passo anterior, mantendo a mesma semente e corrente. */
    public void passoAtras() {
        if (!podeAndarParaTras()) {
            return;
        }
        pararTimer();
        voltarParaTempo(estadoAtual.getTempoAtual() - 1);
    }

    /**
     * Responsabilidade: indicar se existe um estado anterior para reconstruir.
     * @return true se o tempo atual for maior que zero.
     */
    public boolean podeAndarParaTras() {
        return estadoAtual != null && estadoAtual.getTempoAtual() > 0;
    }

    /** Responsabilidade: alternar entre execução automática e pausa. */
    public void alternarExecucaoAutomatica() {
        if (timer.isRunning()) {
            pararTimer();
        } else {
            timer.start();
        }
        atualizarEstadoBotoes();
    }

    /** Responsabilidade: reiniciar a simulação mantendo a semente e a corrente atuais. */
    public void reset() {
        reiniciarComSeedECorrente(seedAtual, correnteAtual);
    }

    /** Responsabilidade: criar uma nova simulação com nova semente e nova corrente inicial. */
    public void novaSimulacao() {
        long novaSeed = gerarNovaSeed();
        Vetor novaCorrente = CenarioFactory.criarCorrenteDemo(novaSeed);
        reiniciarComSeedECorrente(novaSeed, novaCorrente);
    }

    /**
     * Responsabilidade: aplicar uma nova corrente introduzida pelo utilizador e reiniciar o cenário atual.
     * @param x componente horizontal da corrente.
     * @param y componente vertical da corrente.
     */
    public void aplicarCorrente(double x, double y) {
        reiniciarComSeedECorrente(seedAtual, new Vetor(x, y));
    }

    /**
     * Responsabilidade: atualizar o intervalo do temporizador usado no modo automático.
     * @param millis atraso entre passos automáticos em milissegundos.
     */
    public void setDelay(int millis) {
        timer.setDelay(millis);
    }

    /**
     * Responsabilidade: ligar ou desligar a grelha do mapa.
     * @param mostrarGrelha true se a grelha deve ser apresentada.
     */
    public void setMostrarGrelha(boolean mostrarGrelha) {
        mapPanel.setMostrarGrelha(mostrarGrelha);
    }

    /**
     * Responsabilidade: indicar se o temporizador automático está ativo.
     * @return true se a simulação automática estiver a correr.
     */
    public boolean isRunning() {
        return timer.isRunning();
    }

    /**
     * Responsabilidade: devolver o último estado gerado pelo simulador.
     * @return estado atual da simulação.
     */
    public EstadoSimulacao getEstadoAtual() {
        return estadoAtual;
    }

    /**
     * Responsabilidade: devolver a semente usada no cenário atual.
     * @return semente atual.
     */
    public long getSeedAtual() {
        return seedAtual;
    }

    /**
     * Responsabilidade: devolver a componente horizontal da corrente atual.
     * @return componente x da corrente.
     */
    public double getCorrenteX() {
        return correnteAtual.getX();
    }

    /**
     * Responsabilidade: devolver a componente vertical da corrente atual.
     * @return componente y da corrente.
     */
    public double getCorrenteY() {
        return correnteAtual.getY();
    }

    private void voltarParaTempo(int tempoDestino) {
        if (tempoDestino < 0) {
            throw new IllegalArgumentException("SimulationController.voltarParaTempo: tempo invalido");
        }
        simulador = simuladorFactory.apply(seedAtual, correnteAtual);
        estadoAtual = simulador.iniciar();
        for (int i = 0; i < tempoDestino; i++) {
            estadoAtual = simulador.passo();
        }
        atualizarViews();
    }

    private void reiniciarComSeedECorrente(long seed, Vetor corrente) {
        if (corrente == null) {
            throw new IllegalArgumentException("SimulationController.reiniciarComSeedECorrente: corrente null");
        }
        pararTimer();
        seedAtual = seed;
        correnteAtual = corrente;
        simulador = simuladorFactory.apply(seedAtual, correnteAtual);
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
