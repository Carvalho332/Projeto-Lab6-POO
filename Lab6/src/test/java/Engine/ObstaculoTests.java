package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: suportar uma responsabilidade específica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class ObstaculoTests {
    private static class ObstaculoTeste extends Obstaculo {
        /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
        @Override
        public Ponto[] intersect(SegmentoReta s) {
            return new Ponto[] { s.getA() };
        }

        /**
 * Responsabilidade: verificar se o ponto pertence à área ou ao segmento representado.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
        @Override
        public boolean contem(Ponto p) {
            return p != null && p.igual(new Ponto(1.0, 2.0));
        }
    }

    /**
 * Responsabilidade: validar subclass can implement intersect através de um teste unitário.
 */
    @Test
    public void testSubclassCanImplementIntersect() {
        Obstaculo o = new ObstaculoTeste();
        SegmentoReta s = new SegmentoReta(new Ponto(1.0, 2.0), new Ponto(3.0, 4.0));

        Ponto[] inter = o.intersect(s);

        assertNotNull(inter);
        assertEquals(1, inter.length);
        TestSupport.assertPonto(inter[0], 1.0, 2.0);
    }
}
