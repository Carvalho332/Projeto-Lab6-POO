package GUI;

import Engine.EstadoSimulacao;
import Engine.InfoNavio;
import Engine.InfoPorto;
import Engine.Viagem;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.Locale;

/**
 * Responsabilidade: apresentar textualmente o estado atual da simulação.
 */
public class StatusPanel extends JPanel {
    private final JTextArea textArea;

    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Estado"));

        textArea = new JTextArea(18, 28);
        textArea.setEditable(false);
        textArea.setLineWrap(false);
        textArea.setTabSize(2);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public void setEstado(EstadoSimulacao estado) {
        if (estado == null) {
            textArea.setText("Sem estado de simulação.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Tempo: ").append(estado.getTempoAtual()).append('\n');
        sb.append(String.format(Locale.US, "Corrente: <%.2f, %.2f>%n%n",
                estado.getCorrente().getX(), estado.getCorrente().getY()));

        sb.append("Navios ativos:\n");
        if (estado.getNavios().isEmpty()) {
            sb.append("  - nenhum\n");
        } else {
            for (InfoNavio n : estado.getNavios()) {
                sb.append(String.format(Locale.US,
                        "  - %s | %s | pos=(%.2f, %.2f)%s%n",
                        n.getCodigoViagem(),
                        n.getEstado(),
                        n.getPosicao().getX(),
                        n.getPosicao().getY(),
                        n.deveMostrarCirculoColisao() ? " | colisao" : ""));
            }
        }

        sb.append("\nPortos / lista de espera:\n");
        for (InfoPorto p : estado.getPortos()) {
            sb.append("  ").append(p.getNome()).append(": ");
            if (p.getViagensEmEspera().isEmpty()) {
                sb.append("sem viagens");
            } else {
                for (Viagem v : p.getViagensEmEspera()) {
                    sb.append("[")
                            .append("t=").append(v.getTempoSaida())
                            .append(", dest=").append(v.getDestino().getNome())
                            .append(", v=").append(String.format(Locale.US, "%.2f", v.getVelocidadeLinear()))
                            .append("] ");
                }
            }
            sb.append('\n');
        }

        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
    }
}
