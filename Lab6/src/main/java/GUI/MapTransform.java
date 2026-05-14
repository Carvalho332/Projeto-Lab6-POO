package GUI;

import Engine.Ponto;

/**
 * Responsabilidade: converter coordenadas do mundo da simulação em coordenadas do painel gráfico.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class MapTransform {
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;
    private final double scale;
    private final double offsetX;
    private final double offsetY;
    private final double zoom;

    MapTransform(MapBounds bounds, int width, int height, double zoom, double panX, double panY) {
        this.minX = bounds.getMinX();
        this.maxX = bounds.getMaxX();
        this.minY = bounds.getMinY();
        this.maxY = bounds.getMaxY();
        this.zoom = zoom;

        int safeWidth = Math.max(width, 2 * MapStyle.PADDING + 1);
        int safeHeight = Math.max(height, 2 * MapStyle.PADDING + 1);

        double sx = (safeWidth - 2.0 * MapStyle.PADDING) / (maxX - minX);
        double sy = (safeHeight - 2.0 * MapStyle.PADDING) / (maxY - minY);
        double baseScale = Math.max(1.0, Math.min(sx, sy));
        this.scale = baseScale * zoom;

        double mapWidth = (maxX - minX) * this.scale;
        double mapHeight = (maxY - minY) * this.scale;
        this.offsetX = (safeWidth - mapWidth) / 2.0 + panX;
        this.offsetY = (safeHeight - mapHeight) / 2.0 + panY;
    }

    int x(Ponto p) { return x(p.getX()); }
    int y(Ponto p) { return y(p.getY()); }
    int x(double x) { return (int) Math.round(xDouble(x)); }
    int y(double y) { return (int) Math.round(yDouble(y)); }
    double xDouble(Ponto p) { return xDouble(p.getX()); }
    double yDouble(Ponto p) { return yDouble(p.getY()); }
    double xDouble(double x) { return offsetX + (x - minX) * scale; }
    double yDouble(double y) { return offsetY + (maxY - y) * scale; }
    double worldX(double screenX) { return minX + (screenX - offsetX) / scale; }
    double worldY(double screenY) { return maxY - (screenY - offsetY) / scale; }

    double getMinX() { return minX; }
    double getMaxX() { return maxX; }
    double getMinY() { return minY; }
    double getMaxY() { return maxY; }
    double getScale() { return scale; }
    double getZoom() { return zoom; }
}
