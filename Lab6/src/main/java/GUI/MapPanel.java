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
 * Responsabilidade: painel Swing que apresenta o mapa da simulação.
 *
 * <p>Esta classe gere apenas o estado visual do painel: zoom, pan e delegação de
 * desenho. O desenho concreto de rotas, portos, obstáculos, navios, grelha e
 * legenda está separado em classes renderer no pacote GUI.</p>
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

    public MapPanel() {
        setPreferredSize(new Dimension(850, 850));
        setBackground(MapStyle.COR_MAR);
        configurarInteracaoRato();
        new Timer(120, e -> repaint()).start();
    }

    public void setEstado(EstadoSimulacao estado) {
        this.estado = estado;
        repaint();
    }

    public void setMostrarGrelha(boolean mostrarGrelha) {
        this.mostrarGrelha = mostrarGrelha;
        repaint();
    }

    public void reporZoom() {
        zoom = 1.0;
        panX = 0.0;
        panY = 0.0;
        repaint();
    }

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

    private void configurarInteracaoRato() {
        addMouseWheelListener(this::processarZoomComScroll);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                iniciarArrasto(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                terminarArrasto();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                processarArrasto(e);
            }
        });
    }

    private void iniciarArrasto(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        ultimoMouseX = e.getX();
        ultimoMouseY = e.getY();
        arrastando = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    private void terminarArrasto() {
        arrastando = false;
        setCursor(Cursor.getDefaultCursor());
    }

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

    private MapTransform criarTransform(EstadoSimulacao estado) {
        return new MapTransform(MapBounds.from(estado), getWidth(), getHeight(), zoom, panX, panY);
    }

    private void desenharMensagemInicial(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Use Play, Passo ou Nova simulação para testar o simulador.",
                MapStyle.PADDING,
                MapStyle.PADDING);
    }
}
