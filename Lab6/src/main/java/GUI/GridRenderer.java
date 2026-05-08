package GUI;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Locale;

/** Desenha a grelha e a informação de zoom. */
final class GridRenderer {
    private GridRenderer() {
    }

    static void desenhar(Graphics2D g2, MapTransform t, int width, int height) {
        g2.setColor(MapStyle.COR_GRELHA);
        g2.setStroke(new BasicStroke(1f));

        int minX = (int) Math.floor(t.getMinX());
        int maxX = (int) Math.ceil(t.getMaxX());
        int minY = (int) Math.floor(t.getMinY());
        int maxY = (int) Math.ceil(t.getMaxY());

        for (int x = minX; x <= maxX; x++) {
            int sx = t.x(x);
            g2.draw(new Line2D.Double(sx, 0, sx, height));
        }
        for (int y = minY; y <= maxY; y++) {
            int sy = t.y(y);
            g2.draw(new Line2D.Double(0, sy, width, sy));
        }

        g2.setColor(MapStyle.COR_TEXTO_GRELHA);
        g2.drawString(
                String.format(Locale.US, "x:[%.1f, %.1f] y:[%.1f, %.1f]   Zoom: %.0f%%",
                        t.getMinX(), t.getMaxX(), t.getMinY(), t.getMaxY(), t.getZoom() * 100.0),
                MapStyle.PADDING,
                height - 15
        );
    }
}
