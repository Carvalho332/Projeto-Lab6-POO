package GUI;

import Engine.InfoPorto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Responsabilidade: desenhar portos no mapa.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class PortRenderer {
    /**
 * Responsabilidade: construir uma instância de PortRenderer, validando os dados recebidos para preservar os invariantes.
 */
    private PortRenderer() {
    }

    static void desenhar(Graphics2D g2, MapTransform t, List<InfoPorto> portos) {
        for (InfoPorto porto : portos) {
            desenharPorto(g2, t, porto);
        }
    }

    /**
 * Responsabilidade: realizar a operação desenhar porto no contexto da classe PortRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param t tempo disponível para percorrer o segmento.
 * @param porto porto associado à operação.
 */
    private static void desenharPorto(Graphics2D g2, MapTransform t, InfoPorto porto) {
        int x = t.x(porto.getPosicao());
        int y = t.y(porto.getPosicao());
        double s = MapStyle.escalaIcone(t);

        desenharBasePorto(g2, x, y, s);
        desenharTorrePorto(g2, x, y, s);
        desenharCais(g2, x, y, s);
        desenharTextoPorto(g2, porto, x, y, s);
    }

    /**
 * Responsabilidade: realizar a operação desenhar base porto no contexto da classe PortRenderer.
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
        g2.setStroke(new BasicStroke((float) Math.max(1.0, 1.5 * s)));
        g2.draw(basePorto);
    }

    /**
 * Responsabilidade: realizar a operação desenhar torre porto no contexto da classe PortRenderer.
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
        g2.setColor(new Color(170, 55, 55));
        g2.fill(telhado);
        g2.setColor(Color.BLACK);
        g2.draw(telhado);

        g2.setColor(new Color(1, 5, 7));
        g2.fill(new Rectangle2D.Double(x - 8 * s, y - 2 * s, 4 * s, 4 * s));
        g2.fill(new Rectangle2D.Double(x + 4 * s, y - 2 * s, 4 * s, 4 * s));
    }

    /**
 * Responsabilidade: realizar a operação desenhar cais no contexto da classe PortRenderer.
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
        g2.setColor(new Color(138, 84, 27));
        g2.fill(cais);
        g2.draw(cais);

        g2.setColor(new Color(60, 50, 40));
        g2.fill(new Rectangle2D.Double(x - 12 * s, y + 13 * s, 3 * s, 6 * s));
        g2.fill(new Rectangle2D.Double(x - 2 * s, y + 13 * s, 3 * s, 6 * s));
        g2.fill(new Rectangle2D.Double(x + 8 * s, y + 13 * s, 3 * s, 6 * s));
    }

    /**
 * Responsabilidade: realizar a operação desenhar texto porto no contexto da classe PortRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param porto porto associado à operação.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharTextoPorto(Graphics2D g2, InfoPorto porto, int x, int y, double s) {
        g2.setColor(new Color(20, 40, 30));
        g2.drawString("Porto " + porto.getNome(), (int) Math.round(x + 24 * s), (int) Math.round(y - 6 * s));
        if (porto.temViagensEmEspera()) {
            g2.drawString("Fila: " + porto.getViagensEmEspera().size(),
                    (int) Math.round(x + 24 * s),
                    (int) Math.round(y + 8 * s));
        }
    }
}
