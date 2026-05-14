package GUI;

import Engine.Circulo;
import Engine.Obstaculo;
import Engine.ObstaculoMovel;
import Engine.Poligono;
import Engine.Ponto;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * Responsabilidade: desenhar obstáculos fixos e móveis no mapa.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class ObstacleRenderer {
    /**
 * Responsabilidade: construir uma instância de ObstacleRenderer, validando os dados recebidos para preservar os invariantes.
 */
    private ObstacleRenderer() {
    }

    static void desenhar(Graphics2D g2, MapTransform t, List<Obstaculo> obstaculos) {
        for (Obstaculo o : obstaculos) {
            if (o instanceof Circulo) {
                desenharCirculo(g2, t, (Circulo) o, o instanceof ObstaculoMovel);
            } else if (o instanceof Poligono) {
                desenharPoligono(g2, t, (Poligono) o);
            }
        }
    }

    /**
 * Responsabilidade: realizar a operação desenhar circulo no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param t tempo disponível para percorrer o segmento.
 * @param c terceiro ponto usado na validação geométrica.
 * @param movel movel usado pelo método para cumprir a responsabilidade descrita.
 */
    private static void desenharCirculo(Graphics2D g2, MapTransform t, Circulo c, boolean movel) {
        double raio = c.getRaio() * t.getScale();
        double cx = t.xDouble(c.getCentro()) - raio;
        double cy = t.yDouble(c.getCentro()) - raio;
        Shape s = new Ellipse2D.Double(cx, cy, 2 * raio, 2 * raio);

        g2.setColor(movel ? MapStyle.COR_OBSTACULO_MOVEL : MapStyle.COR_OBSTACULO_FIXO);
        g2.fill(s);
        g2.setColor(movel ? MapStyle.COR_OBSTACULO_MOVEL_CONTORNO : MapStyle.COR_OBSTACULO_FIXO_CONTORNO);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(s);
    }

    /**
 * Responsabilidade: realizar a operação desenhar poligono no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param t tempo disponível para percorrer o segmento.
 * @param p ponto analisado, acrescentado ou convertido.
 */
    private static void desenharPoligono(Graphics2D g2, MapTransform t, Poligono p) {
        Path2D path = criarPath(t, p);
        int n = p.getNumeroVertices();

        if (n == 4) {
            desenharFarol(g2, path, t);
        } else if (n == 3) {
            desenharTriangulo(g2, path, t);
        } else if (n == 5) {
            desenharRocha(g2, path, t);
        } else {
            desenharIlha(g2, path, t);
        }
    }

    /**
 * Responsabilidade: criar path com a configuração necessária.
 * @param t tempo disponível para percorrer o segmento.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return objeto resultante da operação.
 */
    private static Path2D criarPath(MapTransform t, Poligono p) {
        Path2D path = new Path2D.Double();
        Ponto primeiro = p.getVertice(0);
        path.moveTo(t.x(primeiro), t.y(primeiro));
        for (int i = 1; i < p.getNumeroVertices(); i++) {
            Ponto v = p.getVertice(i);
            path.lineTo(t.x(v), t.y(v));
        }
        path.closePath();
        return path;
    }

    /**
 * Responsabilidade: realizar a operação desenhar farol no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param path path usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 */
    private static void desenharFarol(Graphics2D g2, Path2D path, MapTransform t) {

        Rectangle bounds = path.getBounds();

        Shape oldClip = g2.getClip();
        g2.setClip(path);

        g2.setColor(new Color(235, 235, 235));
        g2.fill(path);

        int faixas = 5;
        double faixaAltura = bounds.getHeight() / (faixas * 2.0);

        g2.setColor(new Color(200, 50, 50));

        for (int i = 0; i < faixas; i++) {
            double y = bounds.getY() + (2 * i + 1) * faixaAltura;
            g2.fill(new Rectangle(bounds.x, (int) y, bounds.width, (int) faixaAltura));
        }
        g2.setClip(oldClip);
        g2.setColor(new Color(120, 120, 120));
        g2.setStroke(new BasicStroke((float) MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
    }

    /**
 * Responsabilidade: realizar a operação desenhar triangulo no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param path path usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 */
    private static void desenharTriangulo(Graphics2D g2, Path2D path, MapTransform t) {

        g2.setColor(new Color(180, 40, 40));
        g2.fill(path);
        g2.setColor(new Color(60, 60, 60));
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
    }

    /**
 * Responsabilidade: realizar a operação desenhar rocha no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param path path usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 */
    private static void desenharRocha(Graphics2D g2, Path2D path, MapTransform t) {
        Rectangle bounds = path.getBounds();

        GradientPaint gradiente = new GradientPaint(bounds.x, bounds.y, new Color(140, 140, 140), bounds.x + bounds.width, bounds.y + bounds.height, new Color(70, 70, 70));
        g2.setPaint(gradiente);
        g2.fill(path);

        g2.setColor(new Color(55, 55, 55));
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.2)));
        g2.draw(path);

        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 1.0)));
        g2.setColor(new Color(180, 180, 180, 120));

        for (int i = 0; i < 6; i++) {
            int x1 = bounds.x + (int) (Math.random() * bounds.width);
            int y1 = bounds.y + (int) (Math.random() * bounds.height);
            int x2 = x1 + (int) (Math.random() * 12 - 6);
            int y2 = y1 + (int) (Math.random() * 12 - 6);

            if (path.contains(x1, y1) && path.contains(x2, y2)) {
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        g2.setColor(new Color(0, 0, 0, 40));
        g2.translate(3, 3);
        g2.draw(path);
        g2.translate(-3, -3);
    }

    /**
 * Responsabilidade: realizar a operação desenhar ilha no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param path path usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 */
    private static void desenharIlha(Graphics2D g2, Path2D path, MapTransform t) {

        g2.setColor(new Color(210, 190, 120));
        g2.fill(path);

        g2.setColor(new Color(120, 100, 60));
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);

        desenharPalmeira(g2, path, t);
    }

    /**
 * Responsabilidade: realizar a operação desenhar palmeira no contexto da classe ObstacleRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param path path usado pelo método para cumprir a responsabilidade descrita.
 * @param t tempo disponível para percorrer o segmento.
 */
    private static void desenharPalmeira(Graphics2D g2, Path2D path, MapTransform t) {

        Rectangle r = path.getBounds();

        double s = MapStyle.escalaIcone(t);

        double x = r.getCenterX();
        double y = r.getCenterY() + 6 * s;

        double alturaTronco = 24.0 * s;
        double comprimentoFolha = 18.0 * s;
        double relvaSize = 40 * s;

        g2.setColor(new Color(70, 150, 70));

        g2.fill(new Ellipse2D.Double(x - relvaSize / 2.0, y - relvaSize / 2.0, relvaSize, relvaSize));
        g2.setColor(new Color(139, 69, 19));

        g2.setStroke(new BasicStroke((float) Math.max(1.0, 3.0 * s)));

        g2.draw(new Line2D.Double(x, y, x, y - alturaTronco));
        g2.setColor(new Color(34, 139, 34));
        g2.setStroke(new BasicStroke((float) Math.max(1.0, 2.0 * s)));

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double x2 = x + Math.cos(angle) * comprimentoFolha;
            double y2 = y - alturaTronco + Math.sin(angle) * comprimentoFolha;
            g2.draw(new Line2D.Double(x, y - alturaTronco, x2, y2));
        }
    }
}
