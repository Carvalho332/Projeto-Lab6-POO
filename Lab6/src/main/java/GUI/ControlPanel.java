package GUI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.FlowLayout;

/**
 * Responsabilidade: apresentar os controlos usados para avançar a simulação, alterar a corrente e configurar o desenho.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 13-05-2026
 * @inv todos os botões, campos de corrente e controlos gráficos são inicializados no construtor.
 */
public class ControlPanel extends JPanel {
    private final JButton passoAtrasButton;
    private final JButton passoButton;
    private final JButton playPauseButton;
    private final JButton resetButton;
    private final JButton novaSimulacaoButton;
    private final JButton aplicarCorrenteButton;
    private final JCheckBox grelhaCheckBox;
    private final JSlider delaySlider;
    private final JSpinner correnteXSpinner;
    private final JSpinner correnteYSpinner;

    /**
     * Responsabilidade: criar o painel de controlo e associar cada botão ao controlador da simulação.
     * @param controller controlador responsável por executar as ações pedidas pelo utilizador.
     */
    public ControlPanel(SimulationController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("ControlPanel: controller null");
        }

        setLayout(new FlowLayout(FlowLayout.LEFT));

        playPauseButton = new JButton("Play");
        passoAtrasButton = new JButton("◀ Passo");
        passoButton = new JButton("Passo ▶");
        resetButton = new JButton("Reset");
        novaSimulacaoButton = new JButton("Nova simulação");
        aplicarCorrenteButton = new JButton("Aplicar corrente");
        grelhaCheckBox = new JCheckBox("Grelha", true);
        delaySlider = criarSliderDelay(controller);
        correnteXSpinner = criarSpinnerCorrente(controller.getCorrenteX());
        correnteYSpinner = criarSpinnerCorrente(controller.getCorrenteY());

        associarEventos(controller);
        adicionarComponentes();
    }

    /**
     * Responsabilidade: atualizar a disponibilidade dos botões conforme o estado atual da simulação.
     * @param emExecucao true se a simulação automática estiver ativa.
     * @param podeAndarParaTras true se existir um passo anterior para reconstruir.
     */
    public void atualizarEstadoBotoes(boolean emExecucao, boolean podeAndarParaTras) {
        playPauseButton.setText(emExecucao ? "Pausa" : "Play");
        passoAtrasButton.setEnabled(!emExecucao && podeAndarParaTras);
        passoButton.setEnabled(!emExecucao);
        resetButton.setEnabled(!emExecucao);
        novaSimulacaoButton.setEnabled(!emExecucao);
        aplicarCorrenteButton.setEnabled(!emExecucao);
        correnteXSpinner.setEnabled(!emExecucao);
        correnteYSpinner.setEnabled(!emExecucao);
    }

    /**
     * Responsabilidade: atualizar apenas o texto do botão Play/Pausa mantendo a possibilidade de recuar ativa.
     * @param emExecucao true se a simulação automática estiver ativa.
     */
    public void atualizarTextoPlay(boolean emExecucao) {
        atualizarEstadoBotoes(emExecucao, true);
    }

    private JSlider criarSliderDelay(SimulationController controller) {
        JSlider slider = new JSlider(100, 1500, 600);
        slider.setMajorTickSpacing(700);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setToolTipText("Intervalo entre passos automáticos, em milissegundos");
        slider.addChangeListener(e -> controller.setDelay(slider.getValue()));
        return slider;
    }

    private JSpinner criarSpinnerCorrente(double valorInicial) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(valorInicial, -10.0, 10.0, 0.05));
        spinner.setToolTipText("Componente da corrente");
        return spinner;
    }

    private void associarEventos(SimulationController controller) {
        passoAtrasButton.addActionListener(e -> controller.passoAtras());
        passoButton.addActionListener(e -> controller.passo());
        playPauseButton.addActionListener(e -> controller.alternarExecucaoAutomatica());
        resetButton.addActionListener(e -> controller.reset());
        novaSimulacaoButton.addActionListener(e -> {
            controller.novaSimulacao();
            atualizarSpinnersCorrente(controller);
        });
        aplicarCorrenteButton.addActionListener(e -> controller.aplicarCorrente(
                valorSpinner(correnteXSpinner),
                valorSpinner(correnteYSpinner)
        ));
        grelhaCheckBox.addActionListener(e -> controller.setMostrarGrelha(grelhaCheckBox.isSelected()));
    }

    private void adicionarComponentes() {
        add(playPauseButton);
        add(passoAtrasButton);
        add(passoButton);
        add(resetButton);
        add(novaSimulacaoButton);
        add(grelhaCheckBox);
        add(new JLabel("Corrente X"));
        add(correnteXSpinner);
        add(new JLabel("Corrente Y"));
        add(correnteYSpinner);
        add(aplicarCorrenteButton);
        add(new JLabel("Delay"));
        add(delaySlider);
    }

    private double valorSpinner(JSpinner spinner) {
        return ((Number) spinner.getValue()).doubleValue();
    }

    private void atualizarSpinnersCorrente(SimulationController controller) {
        correnteXSpinner.setValue(controller.getCorrenteX());
        correnteYSpinner.setValue(controller.getCorrenteY());
    }
}
