package Engine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Responsabilidade: fornecer métodos auxiliares reutilizados pelos testes unitários, reduzindo duplicação e mantendo asserções consistentes.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
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
