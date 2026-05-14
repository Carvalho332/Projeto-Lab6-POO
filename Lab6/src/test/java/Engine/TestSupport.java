package Engine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Responsabilidade: fornecer métodos auxiliares reutilizáveis pelos testes unitários do Engine.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
class TestSupport {
    static final double EPS = 1e-9;

    static void assertPonto(Ponto p, double x, double y) {
        assertNotNull(p);
        assertEquals(x, p.getX(), EPS);
        assertEquals(y, p.getY(), EPS);
    }

    static void assertVetor(Vetor v, double x, double y) {
        assertNotNull(v);
        assertEquals(x, v.getX(), EPS);
        assertEquals(y, v.getY(), EPS);
    }

    static void assertContainsPonto(Ponto[] pontos, double x, double y) {
        assertNotNull(pontos);
        for (Ponto p : pontos) {
            if (Math.abs(p.getX() - x) < EPS && Math.abs(p.getY() - y) < EPS) return;
        }
        fail("Esperava encontrar o ponto (" + x + "," + y + ")");
    }

    static Porto porto(String nome, double x, double y) {
        return new Porto(nome, new Ponto(x, y));
    }
}
