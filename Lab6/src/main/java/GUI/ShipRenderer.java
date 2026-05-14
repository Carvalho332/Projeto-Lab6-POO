package GUI;

import Engine.EstadoNavio;
import Engine.InfoNavio;
import Engine.Ponto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * Responsabilidade: desenhar navios e respetivas zonas de colisão no mapa.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
final class ShipRenderer {
    private static final Color[] CORES_NAVIOS = {
            new Color(0, 0, 0),
            new Color(255, 255, 255),
            new Color(40, 120, 70),
            new Color(120, 70, 160),
            new Color(210, 140, 30),
            new Color(50, 170, 200),
            new Color(230, 90, 140),
    };

    /**
 * Responsabilidade: construir uma instância de ShipRenderer, validando os dados recebidos para preservar os invariantes.
 */
    private ShipRenderer() {
    }

    static void desenhar(Graphics2D g2, MapTransform t, List<InfoNavio> navios) {
        for (InfoNavio navio : navios) {
            desenharNavio(g2, t, navio);
        }
    }

    /**
 * Responsabilidade: realizar a operação desenhar navio no contexto da classe ShipRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param t tempo disponível para percorrer o segmento.
 * @param navio navio cujos dados serão analisados ou apresentados.
 */
    private static void desenharNavio(Graphics2D g2, MapTransform t, InfoNavio navio) {
        int x = t.x(navio.getPosicao());
        int y = t.y(navio.getPosicao());

        desenharZonaColisao(g2, t, navio, x, y);

        Path2D barco = criarFormaBarco(navio, t, x, y);
        g2.setColor(corDoNavio(navio));
        g2.fill(barco);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke((float) Math.max(1.0, 1.2 * MapStyle.escalaIcone(t))));
        g2.draw(barco);

        g2.setColor(Color.BLACK);
        g2.drawString(navio.getCodigoViagem(), (int) Math.round(x + 12 * MapStyle.escalaIcone(t)), y + 4);
    }

    /**
 * Responsabilidade: realizar a operação desenhar zona colisao no contexto da classe ShipRenderer.
 * @param g2 contexto gráfico 2D onde o elemento será desenhado.
 * @param t tempo disponível para percorrer o segmento.
 * @param navio navio cujos dados serão analisados ou apresentados.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 */
    private static void desenharZonaColisao(Graphics2D g2, MapTransform t, InfoNavio navio, int x, int y) {
        double r = t.getScale();
        Shape colisao = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g2.setStroke(new BasicStroke(MapStyle.escalaTracoMapa(t, 1.5)));

        if (navio.estaEmEspera() || navio.deveMostrarCirculoColisao()) {
            g2.setColor(new Color(255, 0, 0, 100));
            g2.fill(colisao);
            g2.setColor(Color.RED);
            g2.draw(colisao);
        } else if (navio.estaEmMovimento()) {
            g2.setColor(Color.GREEN);
            g2.draw(colisao);
        }
    }

    /**
 * Responsabilidade: realizar a operação cor do navio no contexto da classe ShipRenderer.
 * @param navio navio cujos dados serão analisados ou apresentados.
 * @return objeto resultante da operação.
 */
    private static Color corDoNavio(InfoNavio navio) {
        if (navio.getEstado() == EstadoNavio.CHEGOU) {
            return MapStyle.COR_NAVIO_CHEGOU;
        }
        if (navio.getEstado() == EstadoNavio.EM_ESPERA) {
            return MapStyle.COR_NAVIO_ESPERA;
        }
        int indice = Math.abs(navio.getCodigoViagem().hashCode()) % CORES_NAVIOS.length;
        return CORES_NAVIOS[indice];
    }

    /**
 * Responsabilidade: criar forma barco com a configuração necessária.
 * @param navio navio cujos dados serão analisados ou apresentados.
 * @param t tempo disponível para percorrer o segmento.
 * @param x coordenada horizontal.
 * @param y coordenada vertical.
 * @return objeto resultante da operação.
 */
    private static Path2D criarFormaBarco(InfoNavio navio, MapTransform t, int x, int y) {
        double[] direcao = direcaoNormalizada(navio, t);
        double ux = direcao[0];
        double uy = direcao[1];
        double px = -uy;
        double py = ux;
        double s = MapStyle.escalaIcone(t);

        double frente = 14.0 * s;
        double meioFrente = 3.0 * s;
        double meioTraseira = 10.0 * s;
        double traseira = 16.0 * s;
        double larguraFrente = 6.0 * s;
        double larguraCorpo = 7.0 * s;
        double larguraTraseira = 5.0 * s;

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

    /**
 * Responsabilidade: realizar a operação direcao normalizada no contexto da classe ShipRenderer.
 * @param navio navio cujos dados serão analisados ou apresentados.
 * @param t tempo disponível para percorrer o segmento.
 * @return array com os elementos calculados ou copiados.
 */
    private static double[] direcaoNormalizada(InfoNavio navio, MapTransform t) {
        Ponto pontoDirecao = navio.getProximoPonto();
        double dx = 0.0;
        double dy = -1.0;
        if (pontoDirecao != null) {
            dx = t.xDouble(pontoDirecao) - t.xDouble(navio.getPosicao());
            dy = t.yDouble(pontoDirecao) - t.yDouble(navio.getPosicao());
        }
        double modulo = Math.hypot(dx, dy);
        if (modulo < 1e-9) {
            return new double[]{0.0, -1.0};
        }
        return new double[]{dx / modulo, dy / modulo};
    }
}
