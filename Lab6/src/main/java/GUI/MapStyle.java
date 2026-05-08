package GUI;

import java.awt.Color;

/**
 * Constantes visuais e pequenas funções auxiliares usadas pelos renderers do mapa.
 */
final class MapStyle {
    static final int PADDING = 50;
    static final double DEFAULT_MARGIN = 2.0;

    static final Color COR_MAR = new Color(120, 185, 255);
    static final Color COR_GRELHA = new Color(214, 226, 238);
    static final Color COR_TEXTO_GRELHA = new Color(130, 145, 160);
    static final Color COR_ROTA = Color.BLACK;
    static final Color COR_OBSTACULO_FIXO = new Color(220, 64, 64, 125);
    static final Color COR_OBSTACULO_FIXO_CONTORNO = new Color(145, 32, 32);
    static final Color COR_OBSTACULO_MOVEL = new Color(255, 155, 79, 130);
    static final Color COR_OBSTACULO_MOVEL_CONTORNO = new Color(194, 88, 12);
    static final Color COR_NAVIO_ESPERA = new Color(230, 80, 55);
    static final Color COR_NAVIO_CHEGOU = new Color(90, 90, 90);

    private MapStyle() {
    }

    static double limitar(double valor, double minimo, double maximo) {
        return Math.max(minimo, Math.min(maximo, valor));
    }

    static double escalaIcone(MapTransform t) {
        return limitar(t.getScale() / 30.0, 0.45, 4.0);
    }

    static float escalaTracoMapa(MapTransform t, double base) {
        return (float) limitar(base * escalaIcone(t), 1.0, 8.0);
    }
}
