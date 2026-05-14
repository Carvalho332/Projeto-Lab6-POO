package GUI;

import Engine.EstadoSimulacao;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

/**
 * Responsabilidade: apresentar graficamente o mapa, rotas, obstáculos, portos, navios e zonas de colisão.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class MapPanel extends JPanel {
    private static final double MIN_ZOOM = 0.35;
    private static final double MAX_ZOOM = 8.0;
    private static final double ZOOM_STEP = 1.12;

    private double zoom = 1.0;
    private double panX = 0.0;
    private double panY = 0.0;

    private int ultimoMouseX;
    private int ultimoMouseY;
    private boolean arrastando;

    private EstadoSimulacao estado;
    private boolean mostrarGrelha = true;

    /**
 * Responsabilidade: construir uma instância de MapPanel, validando os dados recebidos para preservar os invariantes.
 */
    public MapPanel() {
        setPreferredSize(new Dimension(850, 850));
        setBackground(MapStyle.COR_MAR);
        configurarInteracaoRato();
        new Timer(120, e -> repaint()).start();
    }

    /**
 * Responsabilidade: preencher as tabelas do painel com o estado mais recente da simulação.
 * @param estado estado da simulação recebido do Engine.
 */
    public void setEstado(EstadoSimulacao estado) {
        this.estado = estado;
        repaint();
    }

    /**
 * Responsabilidade: atualizar mostrar grelha da instância atual com o valor recebido.
 * @param mostrarGrelha mostrar grelha usado pelo método para cumprir a responsabilidade descrita.
 */
    public void setMostrarGrelha(boolean mostrarGrelha) {
        this.mostrarGrelha = mostrarGrelha;
        repaint();
    }

    /**
 * Responsabilidade: realizar a operação repor zoom no contexto da classe MapPanel.
 */
    public void reporZoom() {
        zoom = 1.0;
        panX = 0.0;
        panY = 0.0;
        repaint();
    }

    /**
 * Responsabilidade: redesenhar o painel gráfico com base no estado atual.
 * @param g contexto gráfico recebido pelo Swing.
 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (estado == null) {
            desenharMensagemInicial(g2);
            g2.dispose();
            return;
        }

        MapTransform transform = criarTransform(estado);
        if (mostrarGrelha) {
            GridRenderer.desenhar(g2, transform, getWidth(), getHeight());
        }
        RouteRenderer.desenhar(g2, transform, estado.getRotas());
        ObstacleRenderer.desenhar(g2, transform, estado.getObstaculos());
        PortRenderer.desenhar(g2, transform, estado.getPortos());
        ShipRenderer.desenhar(g2, transform, estado.getNavios());
        LegendRenderer.desenhar(g2, getWidth());

        g2.dispose();
    }

    /**
 * Responsabilidade: configurar interacao rato de acordo com o aspeto definido para o GUI.
 */
    private void configurarInteracaoRato() {
        addMouseWheelListener(this::processarZoomComScroll);
        addMouseListener(new MouseAdapter() {
            /**
 * Responsabilidade: realizar a operação mouse pressed no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
            @Override
            public void mousePressed(MouseEvent e) {
                iniciarArrasto(e);
            }

            /**
 * Responsabilidade: realizar a operação mouse released no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
            @Override
            public void mouseReleased(MouseEvent e) {
                terminarArrasto();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            /**
 * Responsabilidade: realizar a operação mouse dragged no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
            @Override
            public void mouseDragged(MouseEvent e) {
                processarArrasto(e);
            }
        });
    }

    /**
 * Responsabilidade: realizar a operação iniciar arrasto no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
    private void iniciarArrasto(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        ultimoMouseX = e.getX();
        ultimoMouseY = e.getY();
        arrastando = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    /**
 * Responsabilidade: realizar a operação terminar arrasto no contexto da classe MapPanel.
 */
    private void terminarArrasto() {
        arrastando = false;
        setCursor(Cursor.getDefaultCursor());
    }

    /**
 * Responsabilidade: realizar a operação processar arrasto no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
    private void processarArrasto(MouseEvent e) {
        if (!arrastando) {
            return;
        }
        int dx = e.getX() - ultimoMouseX;
        int dy = e.getY() - ultimoMouseY;
        panX += dx;
        panY += dy;
        ultimoMouseX = e.getX();
        ultimoMouseY = e.getY();
        repaint();
    }

    /**
 * Responsabilidade: realizar a operação processar zoom com scroll no contexto da classe MapPanel.
 * @param e e usado pelo método para cumprir a responsabilidade descrita.
 */
    private void processarZoomComScroll(MouseWheelEvent e) {
        if (estado == null) {
            return;
        }

        MapTransform antes = criarTransform(estado);
        double mundoX = antes.worldX(e.getX());
        double mundoY = antes.worldY(e.getY());

        double fator = Math.pow(ZOOM_STEP, -e.getPreciseWheelRotation());
        double novoZoom = MapStyle.limitar(zoom * fator, MIN_ZOOM, MAX_ZOOM);
        if (Math.abs(novoZoom - zoom) < 1e-9) {
            return;
        }

        zoom = novoZoom;
        MapTransform depois = criarTransform(estado);
        panX += e.getX() - depois.xDouble(mundoX);
        panY += e.getY() - depois.yDouble(mundoY);
        repaint();
    }

    /**
 * Responsabilidade: criar transform com a configuração necessária.
 * @param estado estado da simulação recebido do Engine.
 * @return objeto resultante da operação.
 */
    private MapTransform criarTransform(EstadoSimulacao estado) {
        return new MapTransform(MapBounds.from(estado), getWidth(), getHeight(), zoom, panX, panY);
    }

    /**
 * Responsabilidade: realizar a operação desenhar mensagem inicial no contexto da classe MapPanel.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 */
    private void desenharMensagemInicial(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Use Play, Passo ou Nova simulação para testar o simulador.",
                MapStyle.PADDING,
                MapStyle.PADDING);
    }
}
