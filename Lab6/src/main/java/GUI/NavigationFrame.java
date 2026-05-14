package GUI;

import Engine.CenarioFactory;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

/**
 * Responsabilidade: construir a janela principal que contém mapa, estado e controlos da simulação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class NavigationFrame extends JFrame {
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private final ControlPanel controlPanel;
    private final SimulationController controller;

    /**
 * Responsabilidade: construir uma instância de NavigationFrame, validando os dados recebidos para preservar os invariantes.
 */
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
