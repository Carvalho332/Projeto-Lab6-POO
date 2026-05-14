package GUI;

import Engine.Ponto;
import Engine.Route;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * Responsabilidade: desenhar as rotas navegáveis no mapa.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class RouteRenderer {
    /**
 * Responsabilidade: construir uma instância de RouteRenderer, validando os dados recebidos para preservar os invariantes.
 */
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
