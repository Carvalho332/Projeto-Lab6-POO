package GUI;

import Engine.Ponto;
import Engine.Route;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

/** Desenha as rotas do mapa. */
final class RouteRenderer {
    private RouteRenderer() {
    }

    static void desenhar(Graphics2D g2, MapTransform t, List<Route> rotas) {
        g2.setColor(MapStyle.COR_ROTA);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 2.0), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

        for (Route r : rotas) {
            for (int i = 0; i < r.getNumeroSegmentos(); i++) {
                Ponto a = r.getPonto(i);
                Ponto b = r.getPonto(i + 1);
                g2.draw(new Line2D.Double(t.x(a), t.y(a), t.x(b), t.y(b)));
            }
        }
    }
}
