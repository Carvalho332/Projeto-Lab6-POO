package GUI;

import Engine.EstadoSimulacao;
import Engine.InfoNavio;
import Engine.InfoPorto;
import Engine.InfoViagem;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

/**
 * Responsabilidade: mostrar o estado atual da simulação em tabelas estruturadas.
 *
 * O painel recebe um EstadoSimulacao produzido pelo Engine e apenas apresenta os
 * dados ao utilizador. Não calcula posições, rotas, colisões ou listas de espera.
 */
public class StatusPanel extends JPanel {
    private static final Font FONTE_NORMAL = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONTE_CABECALHO_TABELA = new Font("Arial", Font.BOLD, 12);
    private static final Font FONTE_TITULO_SECCAO = new Font("Arial", Font.BOLD, 14);

    private static final Color COR_GRELHA = new Color(155, 155, 155);
    private static final Color COR_MENSAGEM = new Color(80, 80, 80);

    private static final int LARGURA_PAINEL = 465;
    private static final int ALTURA_TABELA_NAVIOS = 255;
    private static final int ALTURA_TABELA_PORTOS = 310;
    private static final int ALTURA_LINHA = 23;

    private static final String TITULO_ESTADO = "Estado";
    private static final String TITULO_RESUMO = "Resumo";
    private static final String TITULO_NAVIOS = "Navios ativos";
    private static final String TITULO_PORTOS = "Portos / lista de espera";

    private static final String MSG_SEM_NAVIOS = "Sem navios ativos";
    private static final String MSG_SEM_VIAGENS = "Sem viagens em espera";
    private static final String MSG_SEM_SIMULACAO = "Sem simulação";

    private static final String TEXTO_SIM = "sim";
    private static final String TEXTO_NAO = "não";
    private static final String TEXTO_VAZIO = "-";

    private static final String[] COLUNAS_RESUMO = {"Campo", "Valor"};
    private static final String[] COLUNAS_NAVIOS = {"Navio", "Estado", "X", "Y", "Vx", "Vy", "Colisão"};
    private static final String[] COLUNAS_PORTOS = {"Porto", "Saída", "Destino", "Velocidade"};

    private static final int[] LARGURAS_RESUMO = {110, 280};
    private static final int[] LARGURAS_NAVIOS = {50, 110, 50, 50, 50, 50, 65};
    private static final int[] LARGURAS_PORTOS = {80, 80, 150, 115};

    private final JLabel mensagemNavios;
    private final JLabel mensagemPortos;

    private final DefaultTableModel modeloResumo;
    private final DefaultTableModel modeloNavios;
    private final DefaultTableModel modeloPortos;

    private final JTable tabelaResumo;
    private final JTable tabelaNavios;
    private final JTable tabelaPortos;

    private final JScrollPane scrollNavios;
    private final JScrollPane scrollPortos;

    public StatusPanel() {
        setLayout(new BorderLayout());
        setBorder(criarBordaTitulo(TITULO_ESTADO));
        setPreferredSize(new Dimension(LARGURA_PAINEL, 0));

        mensagemNavios = criarLabelMensagem(MSG_SEM_NAVIOS);
        mensagemPortos = criarLabelMensagem(MSG_SEM_VIAGENS);

        modeloResumo = criarModelo(COLUNAS_RESUMO);
        modeloNavios = criarModelo(COLUNAS_NAVIOS);
        modeloPortos = criarModelo(COLUNAS_PORTOS);

        tabelaResumo = criarTabelaResumo(modeloResumo);
        tabelaNavios = criarTabela(modeloNavios);
        tabelaPortos = criarTabela(modeloPortos);

        configurarLarguras(tabelaResumo, LARGURAS_RESUMO);
        configurarLarguras(tabelaNavios, LARGURAS_NAVIOS);
        configurarLarguras(tabelaPortos, LARGURAS_PORTOS);

        inicializarTabelaResumo();

        scrollNavios = criarScrollTabela(tabelaNavios, ALTURA_TABELA_NAVIOS);
        scrollPortos = criarScrollTabela(tabelaPortos, ALTURA_TABELA_PORTOS);

        add(criarConteudo(), BorderLayout.CENTER);
    }

    /**
     * Atualiza o painel com o estado mais recente da simulação.
     *
     * @param estado estado produzido pelo Engine; se for null, limpa as tabelas
     */
    public void setEstado(EstadoSimulacao estado) {
        limparTabelas();

        if (estado == null) {
            atualizarResumoVazio();
            mostrarTabelaOuMensagem(scrollNavios, mensagemNavios, false, MSG_SEM_SIMULACAO);
            mostrarTabelaOuMensagem(scrollPortos, mensagemPortos, false, MSG_SEM_SIMULACAO);
            return;
        }

        atualizarResumo(estado);
        atualizarTabelaNavios(estado);
        atualizarTabelaPortos(estado);
    }

    private JPanel criarConteudo() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        conteudo.add(criarPainelResumo());
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(criarPainelComTabela(TITULO_NAVIOS, scrollNavios, mensagemNavios));
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(criarPainelComTabela(TITULO_PORTOS, scrollPortos, mensagemPortos));

        return conteudo;
    }

    private TitledBorder criarBordaTitulo(String titulo) {
        TitledBorder border = BorderFactory.createTitledBorder(titulo);
        border.setTitleFont(FONTE_TITULO_SECCAO);
        return border;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(criarBordaTitulo(TITULO_RESUMO));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        painel.add(tabelaResumo, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelComTabela(String titulo, JScrollPane scroll, JLabel mensagem) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(criarBordaTitulo(titulo));
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(scroll, BorderLayout.CENTER);
        painel.add(mensagem, BorderLayout.SOUTH);

        return painel;
    }

    private JScrollPane criarScrollTabela(JTable tabela, int alturaPreferida) {
        JScrollPane scroll = new JScrollPane(tabela);

        scroll.setPreferredSize(new Dimension(LARGURA_PAINEL - 35, alturaPreferida));
        scroll.setMinimumSize(new Dimension(0, alturaPreferida));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaPreferida));

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setBorder(BorderFactory.createLineBorder(COR_GRELHA));
        scroll.getViewport().setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    private JLabel criarLabelMensagem(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(FONTE_NORMAL);
        label.setForeground(COR_MENSAGEM);
        label.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        return label;
    }

    private DefaultTableModel criarModelo(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable criarTabelaResumo(DefaultTableModel modelo) {
        JTable tabela = new JTable(modelo);

        configurarEstiloBaseTabela(tabela);
        tabela.setTableHeader(null);
        tabela.setEnabled(false);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.setDefaultRenderer(Object.class, new ResumoRenderer());

        return tabela;
    }

    private JTable criarTabela(DefaultTableModel modelo) {
        JTable tabela = new JTable(modelo);

        configurarEstiloBaseTabela(tabela);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(false);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabela.setCellSelectionEnabled(false);
        tabela.setRowSelectionAllowed(true);

        configurarCabecalho(tabela);
        tabela.setDefaultRenderer(Object.class, new TableCellRenderer());

        return tabela;
    }

    private void configurarEstiloBaseTabela(JTable tabela) {
        tabela.setFont(FONTE_NORMAL);
        tabela.setRowHeight(ALTURA_LINHA);

        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setGridColor(COR_GRELHA);
        tabela.setBorder(BorderFactory.createLineBorder(COR_GRELHA));
    }

    private void configurarCabecalho(JTable tabela) {
        JTableHeader header = tabela.getTableHeader();

        header.setReorderingAllowed(false);
        header.setFont(FONTE_CABECALHO_TABELA);
        header.setBorder(BorderFactory.createLineBorder(COR_GRELHA));
        header.setDefaultRenderer(new HeaderRenderer(tabela));
    }

    private void configurarLarguras(JTable tabela, int[] larguras) {
        for (int i = 0; i < larguras.length; i++) {
            definirLarguraColuna(tabela, i, larguras[i]);
        }
    }

    private void definirLarguraColuna(JTable tabela, int indice, int largura) {
        tabela.getColumnModel().getColumn(indice).setMinWidth(largura);
        tabela.getColumnModel().getColumn(indice).setPreferredWidth(largura);
    }

    private void inicializarTabelaResumo() {
        modeloResumo.setRowCount(0);
        modeloResumo.addRow(new Object[]{"Tempo", TEXTO_VAZIO});
        modeloResumo.addRow(new Object[]{"Corrente", TEXTO_VAZIO});
    }

    private void limparTabelas() {
        modeloNavios.setRowCount(0);
        modeloPortos.setRowCount(0);
    }

    private void atualizarResumoVazio() {
        modeloResumo.setValueAt(TEXTO_VAZIO, 0, 1);
        modeloResumo.setValueAt(TEXTO_VAZIO, 1, 1);
    }

    private void atualizarResumo(EstadoSimulacao estado) {
        modeloResumo.setValueAt(estado.getTempoAtual(), 0, 1);

        modeloResumo.setValueAt(String.format(Locale.US, "<%.2f, %.2f>", estado.getCorrente().getX(), estado.getCorrente().getY()), 1, 1);
    }

    private void atualizarTabelaNavios(EstadoSimulacao estado) {
        if (estado.getNavios().isEmpty()) {
            mostrarTabelaOuMensagem(scrollNavios, mensagemNavios, false, MSG_SEM_NAVIOS);
            return;
        }

        for (InfoNavio navio : estado.getNavios()) {
            adicionarLinhaNavio(navio);
        }

        mostrarTabelaOuMensagem(scrollNavios, mensagemNavios, true, null);
    }

    private void atualizarTabelaPortos(EstadoSimulacao estado) {
        if (!adicionarViagensEmEspera(estado)) {
            mostrarTabelaOuMensagem(scrollPortos, mensagemPortos, false, MSG_SEM_VIAGENS);
            return;
        }

        mostrarTabelaOuMensagem(scrollPortos, mensagemPortos, true, null);
    }

    private boolean adicionarViagensEmEspera(EstadoSimulacao estado) {
        boolean adicionouViagem = false;

        for (InfoPorto porto : estado.getPortos()) {
            for (InfoViagem viagem : porto.getViagensEmEspera()) {
                adicionarLinhaViagem(porto, viagem);
                adicionouViagem = true;
            }
        }

        return adicionouViagem;
    }

    private void adicionarLinhaNavio(InfoNavio navio) {
        modeloNavios.addRow(new Object[]{
                navio.getCodigoViagem(),
                navio.getEstado(),
                formatarDouble(navio.getPosicao().getX()),
                formatarDouble(navio.getPosicao().getY()),
                formatarVelocidadeX(navio),
                formatarVelocidadeY(navio),
                navio.deveMostrarCirculoColisao() ? TEXTO_SIM : TEXTO_NAO
        });
    }

    private void adicionarLinhaViagem(InfoPorto porto, InfoViagem viagem) {
        modeloPortos.addRow(new Object[]{
                porto.getNome(),
                viagem.getTempoSaida(),
                viagem.getDestino(),
                formatarDouble(viagem.getVelocidadeLinear())
        });
    }

    private void mostrarTabelaOuMensagem(JScrollPane scroll, JLabel mensagem, boolean mostrarTabela, String textoMensagem) {
        scroll.setVisible(mostrarTabela);
        mensagem.setVisible(!mostrarTabela);

        if (!mostrarTabela && textoMensagem != null) {
            mensagem.setText(textoMensagem);
        }
    }

    private String formatarVelocidadeX(InfoNavio navio) {
        if (!navio.temVelocidadeVetorial()) {
            return TEXTO_VAZIO;
        }

        return formatarDouble(navio.getVelocidadeVetorial().getX());
    }

    private String formatarVelocidadeY(InfoNavio navio) {
        if (!navio.temVelocidadeVetorial()) {
            return TEXTO_VAZIO;
        }

        return formatarDouble(navio.getVelocidadeVetorial().getY());
    }

    private String formatarDouble(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    /**
     * Renderer do cabeçalho das tabelas principais.
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        HeaderRenderer(JTable tabela) {
            setFont(FONTE_CABECALHO_TABELA);
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setBackground(tabela.getTableHeader().getBackground());
            setForeground(tabela.getTableHeader().getForeground());
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COR_GRELHA));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setText(value == null ? "" : value.toString());
            setFont(FONTE_CABECALHO_TABELA);
            setHorizontalAlignment(CENTER);
            setOpaque(true);
            setBackground(table.getTableHeader().getBackground());
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COR_GRELHA));

            return this;
        }
    }

    /**
     * Renderer das células das tabelas principais.
     */
    private static class TableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(CENTER);
            setFont(FONTE_NORMAL);
            setText(value == null ? "" : value.toString());
            setOpaque(true);
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, COR_GRELHA));

            return this;
        }
    }

    /**
     * Renderer da tabela de resumo.
     *
     * A primeira coluna usa Arial Bold 12.
     * A segunda coluna usa Arial Plain 12.
     */
    private static class ResumoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(CENTER);
            setFont(column == 0 ? FONTE_CABECALHO_TABELA : FONTE_NORMAL);
            setText(value == null ? "" : value.toString());
            setOpaque(true);
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, COR_GRELHA));

            return this;
        }
    }
}