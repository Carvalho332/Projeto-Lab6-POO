package GUI;

import Engine.EstadoSimulacao;
import Engine.InfoNavio;
import Engine.InfoPorto;
import Engine.InfoViagem;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.util.Locale;

/**
 * Responsabilidade: mostrar em texto o estado atual da simulação.
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
        textArea.setText(estado == null ? "Sem estado de simulação." : formatarEstado(estado));
        textArea.setCaretPosition(0);
    }

    private String formatarEstado(EstadoSimulacao estado) {
        StringBuilder sb = new StringBuilder();
        adicionarResumo(sb, estado);
        adicionarNavios(sb, estado);
        adicionarPortos(sb, estado);
        return sb.toString();
    }

    private void adicionarResumo(StringBuilder sb, EstadoSimulacao estado) {
        sb.append("Tempo: ").append(estado.getTempoAtual()).append('\n');
        sb.append(String.format(Locale.US, "Corrente: <%.2f, %.2f>%n%n",
                estado.getCorrente().getX(), estado.getCorrente().getY()));
    }

    private void adicionarNavios(StringBuilder sb, EstadoSimulacao estado) {
        sb.append("Navios ativos:\n");
        if (estado.getNavios().isEmpty()) {
            sb.append("  - nenhum\n");
            return;
        }
        for (InfoNavio n : estado.getNavios()) {
            sb.append(formatarNavio(n)).append('\n');
        }
    }

    private String formatarNavio(InfoNavio n) {
        String colisao = n.deveMostrarCirculoColisao() ? " | colisao" : "";
        String velocidade = n.temVelocidadeVetorial()
                ? String.format(Locale.US, " | vv=<%.2f, %.2f>",
                n.getVelocidadeVetorial().getX(), n.getVelocidadeVetorial().getY())
                : "";
        return String.format(Locale.US,
                "  - %s | %s | pos=(%.2f, %.2f)%s%s",
                n.getCodigoViagem(),
                n.getEstado(),
                n.getPosicao().getX(),
                n.getPosicao().getY(),
                velocidade,
                colisao);
    }

    private void adicionarPortos(StringBuilder sb, EstadoSimulacao estado) {
        sb.append("\nPortos / lista de espera:\n");
        for (InfoPorto p : estado.getPortos()) {
            sb.append("  ").append(p.getNome()).append(": ");
            if (!p.temViagensEmEspera()) {
                sb.append("sem viagens");
            } else {
                for (InfoViagem v : p.getViagensEmEspera()) {
                    sb.append(formatarViagem(v)).append(' ');
                }
            }
            sb.append('\n');
        }
    }

    private String formatarViagem(InfoViagem v) {
        return String.format(Locale.US,
                "[t=%d, dest=%s, v=%.2f]",
                v.getTempoSaida(),
                v.getDestino(),
                v.getVelocidadeLinear());
    }
}
