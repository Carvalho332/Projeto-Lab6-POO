package GUI;

import Engine.*;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import javax.swing.Timer;
/**
 * Responsabilidade: desenhar o mapa, portos, rotas, obstáculos e navios.
 */
public class MapPanel extends JPanel {
    private static final int PADDING = 50;
    private static final double DEFAULT_MARGIN = 2.0;

    private static final double MIN_ZOOM = 0.35;
    private static final double MAX_ZOOM = 8.0;
    private static final double ZOOM_STEP = 1.12;

    private double zoom = 1.0;
    private double panX = 0.0;
    private double panY = 0.0;

    private int ultimoMouseX;
    private int ultimoMouseY;
    private boolean arrastando = false;

    private EstadoSimulacao estado;
    private boolean mostrarGrelha = true;

    public MapPanel() {

        setPreferredSize(new Dimension(850, 850));

        // fundo oceano azul
        setBackground(new Color(120, 185, 255));

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

        // Timer para animação
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

    /**
     * Zoom com scroll, mantendo fixo no ecrã o ponto do mapa que está debaixo do rato.
     */
    private void processarZoomComScroll(MouseWheelEvent e) {
        if (estado == null) {
            return;
        }

        Transform antes = criarTransform(estado);

        double mundoX = antes.worldX(e.getX());
        double mundoY = antes.worldY(e.getY());

        double fator = Math.pow(ZOOM_STEP, -e.getPreciseWheelRotation());
        double novoZoom = limitar(zoom * fator, MIN_ZOOM, MAX_ZOOM);

        if (Math.abs(novoZoom - zoom) < 1e-9) {
            return;
        }

        zoom = novoZoom;

        Transform depois = criarTransform(estado);

        panX += e.getX() - depois.xDouble(mundoX);
        panY += e.getY() - depois.yDouble(mundoY);

        repaint();
    }

    public void reporZoom() {
        zoom = 1.0;
        panX = 0.0;
        panY = 0.0;
        repaint();
    }

    private double limitar(double valor, double minimo, double maximo) {
        return Math.max(minimo, Math.min(maximo, valor));
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

        Transform transform = criarTransform(estado);

        if (mostrarGrelha) {
            desenharGrelha(g2, transform);
        }

        desenharRotas(g2, transform);
        desenharObstaculos(g2, transform);
        desenharPortos(g2, transform);
        desenharNavios(g2, transform);
        desenharLegenda(g2);

        g2.dispose();
    }

    private void desenharMensagemInicial(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Use Play, Passo ou Nova simulação para testar o simulador.", PADDING, PADDING);
    }

    private void desenharGrelha(Graphics2D g2, Transform t) {
        g2.setColor(new Color(214, 226, 238));
        g2.setStroke(new BasicStroke(1f));

        int minX = (int) Math.floor(t.minX);
        int maxX = (int) Math.ceil(t.maxX);
        int minY = (int) Math.floor(t.minY);
        int maxY = (int) Math.ceil(t.maxY);

        for (int x = minX; x <= maxX; x++) {
            int sx = t.x(x);
            g2.draw(new Line2D.Double(sx, 0, sx, getHeight()));
        }

        for (int y = minY; y <= maxY; y++) {
            int sy = t.y(y);
            g2.draw(new Line2D.Double(0, sy, getWidth(), sy));
        }

        g2.setColor(new Color(130, 145, 160));
        g2.drawString(
                String.format(
                        Locale.US,
                        "x:[%.1f, %.1f] y:[%.1f, %.1f]   Zoom: %.0f%%",
                        t.minX,
                        t.maxX,
                        t.minY,
                        t.maxY,
                        t.zoom * 100.0
                ),
                PADDING,
                getHeight() - 15
        );
    }

    private void desenharRotas(Graphics2D g2, Transform t) {
        g2.setColor(Color.BLACK);
        g2.setStroke(
                new BasicStroke(2f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        for (Route r : estado.getRotas()) {
            for (int i = 0; i < r.getNumeroPontos() - 1; i++) {
                Ponto a = r.getPonto(i);
                Ponto b = r.getPonto(i + 1);
                g2.draw(new Line2D.Double(t.x(a), t.y(a), t.x(b), t.y(b)));
            }
        }
    }
    private void desenharObstaculos(Graphics2D g2, Transform t) {
        for (Obstaculo o : estado.getObstaculos()) {
            if (o instanceof Circulo) {
                desenharCirculo(g2, t, (Circulo) o, o instanceof ObstaculoMovel);
            } else if (o instanceof Poligono) {
                desenharPoligono(g2, t, (Poligono) o);
            }
        }
    }

    private void desenharCirculo(Graphics2D g2, Transform t, Circulo c, boolean movel) {
        double raio = c.getRaio() * t.scale;
        double cx = t.xDouble(c.getCentro()) - raio;
        double cy = t.yDouble(c.getCentro()) - raio;
        Shape s = new Ellipse2D.Double(cx, cy, 2 * raio, 2 * raio);

        if (movel) {
            g2.setColor(new Color(255, 155, 79, 130));
            g2.fill(s);
            g2.setColor(new Color(194, 88, 12));
        } else {
            g2.setColor(new Color(220, 64, 64, 125));
            g2.fill(s);
            g2.setColor(new Color(145, 32, 32));
        }
        g2.setStroke(new BasicStroke(2f));
        g2.draw(s);
    }

    private void desenharPoligono(Graphics2D g2, Transform t, Poligono p) {

        Path2D path = new Path2D.Double();

        Ponto primeiro = p.getVertice(0);

        path.moveTo(
                t.x(primeiro),
                t.y(primeiro)
        );

        for (int i = 1; i < p.getNumeroVertices(); i++) {

            Ponto v = p.getVertice(i);

            path.lineTo(
                    t.x(v),
                    t.y(v)
            );
        }

        path.closePath();

        int n = p.getNumeroVertices();

        // =====================================================
        // FAROL
        // =====================================================

        if (n == 4) {

            g2.setColor(new Color(240, 240, 240));
            g2.fill(path);

            Rectangle bounds = path.getBounds();

            // === 4 LISTRAS HORIZONTAIS VERMELHAS ===
            g2.setClip(path);

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3f));

            int step = bounds.height / 5;

            for (int i = 1; i <= 4; i++) {
                int y = bounds.y + i * step;
                g2.drawLine(bounds.x, y, bounds.x + bounds.width, y);
            }

            g2.setClip(null);

            // contorno
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(path);
        }

        // =====================================================
        // TELHADO FAROL
        // =====================================================

        else if (n == 3) {

            g2.setColor(new Color(200, 40, 40));
            g2.fill(path);

            g2.setColor(Color.BLACK);
            g2.draw(path);
        }

        // =====================================================
        // ROCHA
        // =====================================================

        else if (n == 5) {

            g2.setColor(new Color(110, 110, 110));
            g2.fill(path);

            g2.setColor(new Color(70, 70, 70));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(path);
        }

        // =====================================================
        // ILHAS
        // =====================================================

        else {

            g2.setColor(new Color(90, 170, 90));
            g2.fill(path);

            g2.setColor(new Color(40, 110, 40));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(path);

            desenharPalmeira(g2, path);
        }
    }
    private void desenharPalmeira(Graphics2D g2, Path2D path) {

        Rectangle r = path.getBounds();

        int x = r.x + r.width / 2;
        int y = r.y + r.height / 2;

        // tronco
        g2.setColor(new Color(139, 69, 19));
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(x, y, x, y - 15);

        // folhas
        g2.setColor(new Color(34, 139, 34));

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);

            int x2 = (int) (x + Math.cos(angle) * 10);
            int y2 = (int) (y - 15 + Math.sin(angle) * 10);

            g2.drawLine(x, y - 15, x2, y2);
        }
    }

    private void desenharPortos(Graphics2D g2, Transform t) {
        for (InfoPorto p : estado.getPortos()) {
            int x = t.x(p.getPosicao());
            int y = t.y(p.getPosicao());

            /*
             * O porto era desenhado com medidas fixas em pixeis, por exemplo x - 14,
             * y - 24, etc. Por isso, quando a janela era redimensionada ou quando havia
             * zoom, o mapa mudava de escala, mas o porto mantinha sempre o mesmo tamanho.
             *
             * Agora todas as medidas do desenho do porto são multiplicadas por s.
             * O valor de s é calculado a partir da escala atual do mapa.
             */
            double s = escalaIcone(t);

            // BASE DO PORTO
            Path2D basePorto = new Path2D.Double();
            basePorto.moveTo(x - 14 * s, y + 8 * s);
            basePorto.lineTo(x - 14 * s, y - 6 * s);
            basePorto.lineTo(x - 8 * s, y - 12 * s);
            basePorto.lineTo(x + 8 * s, y - 12 * s);
            basePorto.lineTo(x + 14 * s, y - 6 * s);
            basePorto.lineTo(x + 14 * s, y + 8 * s);
            basePorto.closePath();
            g2.setColor(new Color(255, 255, 255));
            g2.fill(basePorto);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke((float) Math.max(1.0, 1.5 * s)));
            g2.draw(basePorto);

            // TORRE
            Path2D torre = new Path2D.Double();
            torre.moveTo(x - 4 * s, y - 12 * s);
            torre.lineTo(x - 4 * s, y - 24 * s);
            torre.lineTo(x + 4 * s, y - 24 * s);
            torre.lineTo(x + 4 * s, y - 12 * s);
            torre.closePath();
            g2.setColor(new Color(255, 255, 255));
            g2.fill(torre);
            g2.setColor(Color.BLACK);
            g2.draw(torre);

            // TELHADO
            Path2D telhado = new Path2D.Double();
            telhado.moveTo(x - 7 * s, y - 24 * s);
            telhado.lineTo(x, y - 30 * s);
            telhado.lineTo(x + 7 * s, y - 24 * s);
            telhado.closePath();
            g2.setColor(new Color(170, 55, 55));
            g2.fill(telhado);
            g2.setColor(Color.BLACK);
            g2.draw(telhado);

            // CAIS
            Path2D cais = new Path2D.Double();
            cais.moveTo(x - 18 * s, y + 8 * s);
            cais.lineTo(x + 18 * s, y + 8 * s);
            cais.lineTo(x + 14 * s, y + 13 * s);
            cais.lineTo(x - 14 * s, y + 13 * s);
            cais.closePath();
            g2.setColor(new Color(138, 84, 27));
            g2.fill(cais);
            g2.setColor(new Color(138, 84, 27));
            g2.draw(cais);

            // POSTES
            g2.setColor(new Color(60, 50, 40));
            g2.fill(new Rectangle2D.Double(x - 12 * s, y + 13 * s, 3 * s, 6 * s));
            g2.fill(new Rectangle2D.Double(x - 2 * s, y + 13 * s, 3 * s, 6 * s));
            g2.fill(new Rectangle2D.Double(x + 8 * s, y + 13 * s, 3 * s, 6 * s));

            // JANELAS
            g2.setColor(new Color(1, 5, 7));
            g2.fill(new Rectangle2D.Double(x - 8 * s, y - 2 * s, 4 * s, 4 * s));
            g2.fill(new Rectangle2D.Double(x + 4 * s, y - 2 * s, 4 * s, 4 * s));

            // TEXTO
            g2.setColor(new Color(20, 40, 30));
            g2.drawString("Porto " + p.getNome(), (int) Math.round(x + 24 * s), (int) Math.round(y - 6 * s));
            if (!p.getViagensEmEspera().isEmpty()) {
                g2.drawString("Fila: " + p.getViagensEmEspera().size(), (int) Math.round(x + 24 * s), (int) Math.round(y + 8 * s));
            }
        }
    }

    private double escalaIcone(Transform t) {
        /*
         * t.scale é a quantidade de pixeis por unidade do mundo.
         * Dividir por 30 mantém o porto com um tamanho parecido ao anterior
         * no zoom/base típico, mas permite que ele aumente/diminua com o mapa.
         */
        return limitar(t.scale / 30.0, 0.45, 4.0);
    }

    private void desenharNavios(Graphics2D g2, Transform t) {
        // CORES DOS NAVIOS
        Color[] coresNavios = {
                new Color(0,0,0),
                new Color(255, 255,255),
                new Color(40, 120, 70),
                new Color(120, 70, 160),
                new Color(210, 140, 30),
                new Color(50, 170, 200),
                new Color(230, 90, 140),

        };

        for (InfoNavio n : estado.getNavios()) {
            int x = t.x(n.getPosicao());
            int y = t.y(n.getPosicao());
            // CÍRCULO DE COLISÃO
            double r = 1.0 * t.scale;
            Shape colisao = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
            long tempo = System.currentTimeMillis();
            boolean zona = true;
            boolean emColisao = n.getEstado() == EstadoNavio.EM_ESPERA;
            g2.setStroke(new BasicStroke(1.5f));
            if (emColisao) {
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fill(colisao);
                g2.setColor(Color.RED);
                g2.draw(colisao);
            }
            else {
                if (zona) {
                    g2.setColor(Color.GREEN);
                    g2.draw(colisao);
                }
            }
            // COR DO NAVIO
            Color corNavio;
            if (n.getEstado() == EstadoNavio.CHEGOU) {
                corNavio = new Color(90, 90, 90);
            }
            else if (n.getEstado() == EstadoNavio.EM_ESPERA) {
                corNavio = new Color(230, 80, 55);
            }
            else {
                int indiceCor = Math.abs(n.getCodigoViagem().hashCode()) % coresNavios.length;
                corNavio = coresNavios[indiceCor];
            }
            // DESENHO NAVIO: a forma é orientada pela direção real da rota.
            Path2D barco = criarFormaBarco(n, t, x, y);
            g2.setColor(corNavio);
            g2.fill(barco);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(barco);
            // CÓDIGO
            g2.setColor(Color.BLACK);
            g2.drawString(n.getCodigoViagem(), x + 12, y + 4);

        }
    }
    private Path2D criarFormaBarco(InfoNavio n, Transform t, int x, int y) {
        Ponto pontoDirecao = n.getProximoPonto();

        double dx = 0.0;
        double dy = -1.0;

        if (pontoDirecao != null) {
            dx = t.xDouble(pontoDirecao) - t.xDouble(n.getPosicao());
            dy = t.yDouble(pontoDirecao) - t.yDouble(n.getPosicao());
        }

        double modulo = Math.hypot(dx, dy);
        if (modulo < 1e-9) {
            dx = 0.0;
            dy = -1.0;
            modulo = 1.0;
        }

        double ux = dx / modulo;
        double uy = dy / modulo;
        double px = -uy;
        double py = ux;

        double frente = 14.0;
        double meioFrente = 3.0;
        double meioTraseira = 10.0;
        double traseira = 16.0;
        double larguraFrente = 6.0;
        double larguraCorpo = 7.0;
        double larguraTraseira = 5.0;

        Path2D barco = new Path2D.Double();
        barco.moveTo(x + ux * frente, y + uy * frente);
        barco.lineTo(x + ux * meioFrente + px * larguraFrente, y + uy * meioFrente + py * larguraFrente);
        barco.lineTo(x - ux * meioTraseira + px * larguraCorpo, y - uy * meioTraseira + py * larguraCorpo);
        barco.lineTo(x - ux * traseira + px * larguraTraseira, y - uy * traseira + py * larguraTraseira);
        barco.lineTo(x - ux * traseira - px * larguraTraseira, y - uy * traseira - py * larguraTraseira);
        barco.lineTo(x - ux * meioTraseira - px * larguraCorpo, y - uy * meioTraseira - py * larguraCorpo);
        barco.lineTo(x + ux * meioFrente - px * larguraFrente, y + uy * meioFrente - py * larguraFrente);
        barco.closePath();

        return barco;
    }


    private void desenharLegenda(Graphics2D g2) {
        int x = getWidth() - 210;
        int y = 20;
        int w = 190;
        int h = 118;

        g2.setColor(new Color(255, 255, 255, 210));
        g2.fillRoundRect(x, y, w, h, 12, 12);
        g2.setColor(new Color(100, 110, 120));
        g2.drawRoundRect(x, y, w, h, 12, 12);

        g2.setColor(Color.BLACK);
        g2.drawString("Legenda", x + 10, y + 20);
        itemLegenda(g2, x + 12, y + 42, new Color(1, 5, 7), "rota");
        itemLegenda(g2, x + 12, y + 62, new Color(35, 94, 65), "porto");
        itemLegenda(g2, x + 12, y + 82, new Color(16, 91, 184), "navio em movimento");
        itemLegenda(g2, x + 12, y + 102, new Color(230, 80, 55), "navio em espera");
    }

    private void itemLegenda(Graphics2D g2, int x, int y, Color color, String texto) {
        g2.setColor(color);
        g2.fillRect(x, y - 9, 13, 13);
        g2.setColor(Color.BLACK);
        g2.drawString(texto, x + 20, y + 2);
    }

    private Transform criarTransform(EstadoSimulacao estado) {
        Bounds b = calcularBounds(estado);
        return new Transform(
                b.minX,
                b.maxX,
                b.minY,
                b.maxY,
                getWidth(),
                getHeight(),
                zoom,
                panX,
                panY
        );
    }

    private Bounds calcularBounds(EstadoSimulacao estado) {
        Bounds b = new Bounds();

        for (InfoPorto p : estado.getPortos()) {
            b.add(p.getPosicao());
        }
        for (InfoNavio n : estado.getNavios()) {
            b.add(n.getPosicao());
        }
        for (Route r : estado.getRotas()) {
            for (Ponto p : r.getPontos()) {
                b.add(p);
            }
        }
        for (Obstaculo o : estado.getObstaculos()) {
            if (o instanceof Circulo) {
                Circulo c = (Circulo) o;
                b.add(c.getCentro().getX() - c.getRaio(), c.getCentro().getY() - c.getRaio());
                b.add(c.getCentro().getX() + c.getRaio(), c.getCentro().getY() + c.getRaio());
            } else if (o instanceof Poligono) {
                for (Ponto p : ((Poligono) o).getVertices()) {
                    b.add(p);
                }
            }
        }

        b.expand(DEFAULT_MARGIN);
        return b;
    }

    private static class Bounds {
        private double minX = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;

        void add(Ponto p) {
            add(p.getX(), p.getY());
        }

        void add(double x, double y) {
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        void expand(double margem) {
            if (!Double.isFinite(minX)) {
                minX = -10;
                maxX = 10;
                minY = -10;
                maxY = 10;
            }
            minX -= margem;
            maxX += margem;
            minY -= margem;
            maxY += margem;

            if (Math.abs(maxX - minX) < 1e-9) {
                minX -= 1;
                maxX += 1;
            }
            if (Math.abs(maxY - minY) < 1e-9) {
                minY -= 1;
                maxY += 1;
            }
        }
    }

    private static class Transform {
        private final double minX;
        private final double maxX;
        private final double minY;
        private final double maxY;

        private final double scale;
        private final double offsetX;
        private final double offsetY;
        private final double zoom;

        Transform(
                double minX,
                double maxX,
                double minY,
                double maxY,
                int width,
                int height,
                double zoom,
                double panX,
                double panY
        ) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            this.zoom = zoom;

            int safeWidth = Math.max(width, 2 * PADDING + 1);
            int safeHeight = Math.max(height, 2 * PADDING + 1);

            double sx = (safeWidth - 2.0 * PADDING) / (maxX - minX);
            double sy = (safeHeight - 2.0 * PADDING) / (maxY - minY);

            double baseScale = Math.max(1.0, Math.min(sx, sy));
            this.scale = baseScale * zoom;

            double mapWidth = (maxX - minX) * this.scale;
            double mapHeight = (maxY - minY) * this.scale;

            this.offsetX = (safeWidth - mapWidth) / 2.0 + panX;
            this.offsetY = (safeHeight - mapHeight) / 2.0 + panY;
        }

        int x(Ponto p) {
            return x(p.getX());
        }

        int y(Ponto p) {
            return y(p.getY());
        }

        int x(double x) {
            return (int) Math.round(xDouble(x));
        }

        int y(double y) {
            return (int) Math.round(yDouble(y));
        }

        double xDouble(Ponto p) {
            return xDouble(p.getX());
        }

        double yDouble(Ponto p) {
            return yDouble(p.getY());
        }

        double xDouble(double x) {
            return offsetX + (x - minX) * scale;
        }

        double yDouble(double y) {
            return offsetY + (maxY - y) * scale;
        }

        double worldX(double screenX) {
            return minX + (screenX - offsetX) / scale;
        }

        double worldY(double screenY) {
            return maxY - (screenY - offsetY) / scale;
        }
    }
}
