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

        passoButton = new JButton("Passo");
        playPauseButton = new JButton("Play");
        resetButton = new JButton("Reset");
        novaSimulacaoButton = new JButton("Nova simulação");
        grelhaCheckBox = new JCheckBox("Grelha", true);
        delaySlider = new JSlider(100, 1500, 600);
        delaySlider.setMajorTickSpacing(700);
        delaySlider.setMinorTickSpacing(100);
        delaySlider.setPaintTicks(true);
        delaySlider.setToolTipText("Intervalo entre passos automáticos, em milissegundos");

        passoButton.addActionListener(e -> controller.passo());
        playPauseButton.addActionListener(e -> controller.alternarExecucaoAutomatica());
        resetButton.addActionListener(e -> controller.reset());
        novaSimulacaoButton.addActionListener(e -> controller.novaSimulacao());
        grelhaCheckBox.addActionListener(e -> controller.setMostrarGrelha(grelhaCheckBox.isSelected()));
        delaySlider.addChangeListener(e -> controller.setDelay(delaySlider.getValue()));

        add(playPauseButton);
        add(passoButton);
        add(resetButton);
        add(novaSimulacaoButton);
        add(grelhaCheckBox);
        add(new JLabel("Delay"));
        add(delaySlider);
    }

    public void atualizarTextoPlay(boolean emExecucao) {
        playPauseButton.setText(emExecucao ? "Pausa" : "Play");
    }
}