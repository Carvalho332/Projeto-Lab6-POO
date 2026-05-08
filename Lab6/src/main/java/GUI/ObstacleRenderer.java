package GUI;

import Engine.Circulo;
import Engine.Obstaculo;
import Engine.ObstaculoMovel;
import Engine.Poligono;
import Engine.Ponto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.List;

/** Desenha os obstáculos fixos e móveis. */
final class ObstacleRenderer {
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

    private static void desenharFarol(Graphics2D g2, Path2D path, MapTransform t) {
        g2.setColor(new Color(240, 240, 240));
        g2.fill(path);

        Rectangle bounds = path.getBounds();
        Shape oldClip = g2.getClip();
        g2.setClip(path);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 3.0)));
        int step = Math.max(1, bounds.height / 5);
        for (int i = 1; i <= 4; i++) {
            int y = bounds.y + i * step;
            g2.drawLine(bounds.x, y, bounds.x + bounds.width, y);
        }
        g2.setClip(oldClip);

        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
    }

    private static void desenharTriangulo(Graphics2D g2, Path2D path, MapTransform t) {
        g2.setColor(new Color(200, 40, 40));
        g2.fill(path);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
    }

    private static void desenharRocha(Graphics2D g2, Path2D path, MapTransform t) {
        g2.setColor(new Color(110, 110, 110));
        g2.fill(path);
        g2.setColor(new Color(70, 70, 70));
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
    }

    private static void desenharIlha(Graphics2D g2, Path2D path, MapTransform t) {
        g2.setColor(new Color(90, 170, 90));
        g2.fill(path);
        g2.setColor(new Color(40, 110, 40));
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0)));
        g2.draw(path);
        desenharPalmeira(g2, path, t);
    }

    private static void desenharPalmeira(Graphics2D g2, Path2D path, MapTransform t) {
        Rectangle r = path.getBounds();
        double s = MapStyle.escalaIcone(t);
        double x = r.x + r.width / 2.0;
        double y = r.y + r.height / 2.0;
        double alturaTronco = 25.0 * s;
        double comprimentoFolha = 20.0 * s;

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
