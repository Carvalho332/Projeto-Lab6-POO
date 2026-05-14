package GUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Responsabilidade: iniciar a aplicação gráfica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class Main {
    /**
 * Responsabilidade: iniciar a aplicação gráfica no Event Dispatch Thread do Swing.
 * @param args argumentos da linha de comandos.
 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            NavigationFrame frame = new NavigationFrame();
            frame.setVisible(true);
        });
    }
}
