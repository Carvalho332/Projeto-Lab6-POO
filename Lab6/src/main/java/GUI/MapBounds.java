package GUI;

import Engine.Circulo;
import Engine.EstadoSimulacao;
import Engine.InfoNavio;
import Engine.InfoPorto;
import Engine.Obstaculo;
import Engine.Poligono;
import Engine.Ponto;
import Engine.Route;

/**
 * Limites em coordenadas do mundo usados para converter o mapa para ecrã.
 */
final class MapBounds {
    private double minX = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    static MapBounds from(EstadoSimulacao estado) {
        MapBounds b = new MapBounds();
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
        b.expand(MapStyle.DEFAULT_MARGIN);
        return b;
    }

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

    double getMinX() { return minX; }
    double getMaxX() { return maxX; }
    double getMinY() { return minY; }
    double getMaxY() { return maxY; }
}
