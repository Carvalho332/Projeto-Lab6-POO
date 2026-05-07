package GUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Ponto de entrada do GUI.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Se o LookAndFeel do sistema não estiver disponível, usa-se o padrão do Swing.
            }

            NavigationFrame frame = new NavigationFrame();
            frame.setVisible(true);
        });
    }
}
