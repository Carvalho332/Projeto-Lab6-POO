package GUI;

import Engine.CenarioFactory;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

/**
 * Responsabilidade: janela principal da aplicação gráfica.
 */
public class NavigationFrame extends JFrame {
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private final ControlPanel controlPanel;
    private final SimulationController controller;

    public NavigationFrame() {
        super("Lab 6 - Simulador de Navegação");

        mapPanel = new MapPanel();
        statusPanel = new StatusPanel();
        controller = new SimulationController(CenarioFactory::criarSimuladorDemo, mapPanel, statusPanel);
        controlPanel = new ControlPanel(controller);
        controller.setControlPanel(controlPanel);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPanel, statusPanel);
        splitPane.setResizeWeight(0.78);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
