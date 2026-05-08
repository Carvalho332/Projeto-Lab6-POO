package GUI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.FlowLayout;

/**
 * Responsabilidade: mostrar os controlos de execução da simulação.
 */
public class ControlPanel extends JPanel {
    private final JButton passoAtrasButton;
    private final JButton passoButton;
    private final JButton playPauseButton;
    private final JButton resetButton;
    private final JButton novaSimulacaoButton;
    private final JCheckBox grelhaCheckBox;
    private final JSlider delaySlider;

    public ControlPanel(SimulationController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("ControlPanel: controller null");
        }

        setLayout(new FlowLayout(FlowLayout.LEFT));

        passoAtrasButton = new JButton("◀ Passo");
        passoButton = new JButton("Passo ▶");
        playPauseButton = new JButton("Play");
        resetButton = new JButton("Reset");
        novaSimulacaoButton = new JButton("Nova simulação");
        grelhaCheckBox = new JCheckBox("Grelha", true);
        delaySlider = new JSlider(100, 1500, 600);
        delaySlider.setMajorTickSpacing(700);
        delaySlider.setMinorTickSpacing(100);
        delaySlider.setPaintTicks(true);
        delaySlider.setToolTipText("Intervalo entre passos automáticos, em milissegundos");

        passoAtrasButton.addActionListener(e -> controller.passoAtras());
        passoButton.addActionListener(e -> controller.passo());
        playPauseButton.addActionListener(e -> controller.alternarExecucaoAutomatica());
        resetButton.addActionListener(e -> controller.reset());
        novaSimulacaoButton.addActionListener(e -> controller.novaSimulacao());
        grelhaCheckBox.addActionListener(e -> controller.setMostrarGrelha(grelhaCheckBox.isSelected()));
        delaySlider.addChangeListener(e -> controller.setDelay(delaySlider.getValue()));

        add(playPauseButton);
        add(passoAtrasButton);
        add(passoButton);
        add(resetButton);
        add(novaSimulacaoButton);
        add(grelhaCheckBox);
        add(new JLabel("Delay"));
        add(delaySlider);
    }

    /**
     * Atualiza o texto e a disponibilidade dos botões conforme o estado da simulação.
     *
     * @param emExecucao true se a execução automática estiver ativa
     * @param podeAndarParaTras true se existir estado anterior disponível
     */
    public void atualizarEstadoBotoes(boolean emExecucao, boolean podeAndarParaTras) {
        playPauseButton.setText(emExecucao ? "Pausa" : "Play");
        passoAtrasButton.setEnabled(!emExecucao && podeAndarParaTras);
        passoButton.setEnabled(!emExecucao);
        resetButton.setEnabled(!emExecucao);
        novaSimulacaoButton.setEnabled(!emExecucao);
    }

    /**
     * Mantido por compatibilidade com versões anteriores do controller.
     */
    public void atualizarTextoPlay(boolean emExecucao) {
        atualizarEstadoBotoes(emExecucao, true);
    }
}
