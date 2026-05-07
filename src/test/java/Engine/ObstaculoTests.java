package Engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe Obstaculo, validando o contrato comum das classes de obstáculos.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class ObstaculoTests {
    private static class ObstaculoTeste extends Obstaculo {
        @Override
        public Ponto[] intersect(SegmentoReta s) {
            return new Ponto[] { s.getA() };
        }
    }

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
