package GUI;

import java.awt.Color;
import java.awt.Graphics2D;

/** Desenha a legenda no canto superior direito. */
final class LegendRenderer {
    private LegendRenderer() {
    }

    static void desenhar(Graphics2D g2, int panelWidth) {
        int x = panelWidth - 210;
        int y = 20;
        int w = 190;
        int h = 118;

        g2.setColor(new Color(255, 255, 255, 210));
        g2.fillRoundRect(x, y, w, h, 12, 12);
        g2.setColor(new Color(100, 110, 120));
        g2.drawRoundRect(x, y, w, h, 12, 12);

        g2.setColor(Color.BLACK);
        g2.drawString("Legenda", x + 10, y + 20);
        itemLegenda(g2, x + 12, y + 42, Color.BLACK, "rota");
        itemLegenda(g2, x + 12, y + 62, new Color(35, 94, 65), "porto");
        itemLegenda(g2, x + 12, y + 82, new Color(16, 91, 184), "navio em movimento");
        itemLegenda(g2, x + 12, y + 102, MapStyle.COR_NAVIO_ESPERA, "navio em espera");
    }

    private static void itemLegenda(Graphics2D g2, int x, int y, Color color, String texto) {
        g2.setColor(color);
        g2.fillRect(x, y - 9, 13, 13);
        g2.setColor(Color.BLACK);
        g2.drawString(texto, x + 20, y + 2);
    }
}
