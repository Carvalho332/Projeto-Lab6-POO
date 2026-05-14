package GUI;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Responsabilidade: desenhar a legenda dos elementos visuais apresentados no mapa.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class LegendRenderer {
    private static final Color COR_FUNDO = new Color(255, 255, 255, 220);
    private static final Color COR_CONTORNO = new Color(100, 110, 120);
    private static final Color COR_TELHADO_PORTO = new Color(170, 55, 55);
    private static final Color COR_CAIS = new Color(138, 84, 27);
    private static final Color COR_BARCO_LEGENDA = new Color(16, 91, 184);
    private static final Color COR_CIRCULO_NAVIO = new Color(0, 180, 0);
    private static final Color COR_CIRCULO_ESPERA = new Color(255, 0, 0, 95);

    /**
 * Responsabilidade: construir uma instância de LegendRenderer, validando os dados recebidos para preservar os invariantes.
 */
    private LegendRenderer() {
    }

    static void desenhar(Graphics2D g2, int panelWidth) {
        int w = 210;
        int h = 210;
        int x = panelWidth - w - 20;
        int y = 20;

        Object antialias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        desenharCaixa(g2, x, y, w, h);

        Font fonteOriginal = g2.getFont();
        Font fonteNormal = new Font("Arial", Font.PLAIN, 12);
        Font fonteTitulo = new Font("Arial", Font.BOLD, 12);

        g2.setColor(Color.BLACK);

        g2.setFont(fonteTitulo);
        g2.drawString("Legenda", x + 10, y + 20);
        g2.setFont(fonteNormal);

        int iconX = x + 25;
        int textX = x + 53;
        int linhaY = y + 45;
        int passo = 35;

        desenharIconeRota(g2, iconX, linhaY);
        desenharTexto(g2, textX, linhaY, "Rota");

        linhaY += passo;
        desenharIconeObstaculoMovel(g2, iconX, linhaY);
        desenharTexto(g2, textX, linhaY, "Obstaculo Movél");

        linhaY += passo;
        desenharIconePorto(g2, iconX, linhaY);
        desenharTexto(g2, textX, linhaY, "Porto");

        linhaY += passo;
        desenharIconeNavioEmMovimento(g2, iconX, linhaY);
        desenharTexto(g2, textX, linhaY, "Navio em Movimento");

        linhaY += passo;
        desenharIconeNavioEmEspera(g2, iconX, linhaY);
        desenharTexto(g2, textX, linhaY, "Navio em Espera / Colisao");

        g2.setFont(fonteOriginal);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
    }

    /**
 * Responsabilidade: realizar a operação desenhar caixa no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param w vetor da corrente a compensar no cálculo da velocidade própria do navio.
 * @param h h usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharCaixa(Graphics2D g2, int x, int y, int w, int h) {
        g2.setColor(COR_FUNDO);
        g2.fillRoundRect(x, y, w, h, 12, 12);
        g2.setColor(COR_CONTORNO);
        g2.drawRoundRect(x, y, w, h, 12, 12);
    }

    /**
 * Responsabilidade: realizar a operação desenhar texto no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param texto texto apresentado no componente gráfico.
 */
    private static void desenharTexto(Graphics2D g2, int x, int y, String texto) {
        g2.setColor(Color.BLACK);
        g2.drawString(texto, x, y + 4);
    }

    /**
 * Responsabilidade: realizar a operação desenhar icone rota no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharIconeRota(Graphics2D g2, int x, int y) {
        g2.setColor(MapStyle.COR_ROTA);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(x - 10, y, x + 16, y);
        g2.fillOval(x - 11, y - 2, 4, 4);
        g2.fillOval(x + 14, y - 2, 4, 4);
    }

    /**
 * Responsabilidade: realizar a operação desenhar icone obstaculo movel no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharIconeObstaculoMovel(Graphics2D g2, int x, int y) {
        Shape circulo = new Ellipse2D.Double(x - 11, y - 11, 22, 22);
        g2.setColor(MapStyle.COR_OBSTACULO_MOVEL);
        g2.fill(circulo);
        g2.setColor(MapStyle.COR_OBSTACULO_MOVEL_CONTORNO);
        g2.setStroke(new BasicStroke(1.7f));
        g2.draw(circulo);
    }

    /**
 * Responsabilidade: realizar a operação desenhar icone porto no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharIconePorto(Graphics2D g2, int x, int y) {
        double s = 0.55;
        desenharBasePorto(g2, x, y, s);
        desenharTorrePorto(g2, x, y, s);
        desenharCais(g2, x, y, s);
    }

    /**
 * Responsabilidade: realizar a operação desenhar base porto no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharBasePorto(Graphics2D g2, int x, int y, double s) {
        Path2D basePorto = new Path2D.Double();
        basePorto.moveTo(x - 14 * s, y + 8 * s);
        basePorto.lineTo(x - 14 * s, y - 6 * s);
        basePorto.lineTo(x - 8 * s, y - 12 * s);
        basePorto.lineTo(x + 8 * s, y - 12 * s);
        basePorto.lineTo(x + 14 * s, y - 6 * s);
        basePorto.lineTo(x + 14 * s, y + 8 * s);
        basePorto.closePath();

        g2.setColor(Color.WHITE);
        g2.fill(basePorto);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(basePorto);
    }

    /**
 * Responsabilidade: realizar a operação desenhar torre porto no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharTorrePorto(Graphics2D g2, int x, int y, double s) {
        Path2D torre = new Path2D.Double();
        torre.moveTo(x - 4 * s, y - 12 * s);
        torre.lineTo(x - 4 * s, y - 24 * s);
        torre.lineTo(x + 4 * s, y - 24 * s);
        torre.lineTo(x + 4 * s, y - 12 * s);
        torre.closePath();

        g2.setColor(Color.WHITE);
        g2.fill(torre);
        g2.setColor(Color.BLACK);
        g2.draw(torre);

        Path2D telhado = new Path2D.Double();
        telhado.moveTo(x - 7 * s, y - 24 * s);
        telhado.lineTo(x, y - 30 * s);
        telhado.lineTo(x + 7 * s, y - 24 * s);
        telhado.closePath();

        g2.setColor(COR_TELHADO_PORTO);
        g2.fill(telhado);
        g2.setColor(Color.BLACK);
        g2.draw(telhado);
    }

    /**
 * Responsabilidade: realizar a operação desenhar cais no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharCais(Graphics2D g2, int x, int y, double s) {
        Path2D cais = new Path2D.Double();
        cais.moveTo(x - 18 * s, y + 8 * s);
        cais.lineTo(x + 18 * s, y + 8 * s);
        cais.lineTo(x + 14 * s, y + 13 * s);
        cais.lineTo(x - 14 * s, y + 13 * s);
        cais.closePath();

        g2.setColor(COR_CAIS);
        g2.fill(cais);
        g2.setColor(Color.BLACK);
        g2.draw(cais);

        g2.setColor(new Color(60, 50, 40));
        g2.fill(new Rectangle2D.Double(x - 12 * s, y + 13 * s, 3 * s, 5 * s));
        g2.fill(new Rectangle2D.Double(x - 2 * s, y + 13 * s, 3 * s, 5 * s));
        g2.fill(new Rectangle2D.Double(x + 8 * s, y + 13 * s, 3 * s, 5 * s));
    }

    /**
 * Responsabilidade: realizar a operação desenhar icone navio em movimento no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharIconeNavioEmMovimento(Graphics2D g2, int x, int y) {
        desenharCirculoColisao(g2, x, y, COR_CIRCULO_NAVIO, false);
        desenharBarcoLegenda(g2, x, y, COR_BARCO_LEGENDA);
    }

    /**
 * Responsabilidade: realizar a operação desenhar icone navio em espera no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharIconeNavioEmEspera(Graphics2D g2, int x, int y) {
        desenharCirculoColisao(g2, x, y, COR_CIRCULO_ESPERA, true);
        desenharBarcoLegenda(g2, x, y, MapStyle.COR_NAVIO_ESPERA);
    }

    /**
 * Responsabilidade: realizar a operação desenhar circulo colisao no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param cor cor usado pelo método para cumprir a responsabilidade descrita.
 * @param preenchido preenchido usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharCirculoColisao(Graphics2D g2, int x, int y, Color cor, boolean preenchido) {
        Shape circulo = new Ellipse2D.Double(x - 12, y - 12, 24, 24);
        g2.setStroke(new BasicStroke(1.8f));
        if (preenchido) {
            g2.setColor(cor);
            g2.fill(circulo);
            g2.setColor(Color.RED);
        } else {
            g2.setColor(cor);
        }
        g2.draw(circulo);
    }

    /**
 * Responsabilidade: realizar a operação desenhar barco legenda no contexto da classe LegendRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param cor cor usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharBarcoLegenda(Graphics2D g2, int x, int y, Color cor) {
        Path2D barco = new Path2D.Double();
        barco.moveTo(x + 13, y);
        barco.lineTo(x + 4, y - 6);
        barco.lineTo(x - 10, y - 5);
        barco.lineTo(x - 14, y);
        barco.lineTo(x - 10, y + 5);
        barco.lineTo(x + 4, y + 6);
        barco.closePath();

        g2.setColor(cor);
        g2.fill(barco);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(barco);
    }
}
