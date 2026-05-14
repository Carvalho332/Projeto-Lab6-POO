package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: suportar uma responsabilidade específica do simulador de navegação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class CalcularRotaTests {
    private static final double EPS = TestSupport.EPS;

    /**
 * Responsabilidade: validar rota bloqueada através de um teste unitário.
 */
    @Test
    public void testRotaBloqueada() {
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });
        Obstaculo bloqueante = new Circulo(new Ponto(5.0, 0.0), 1.0);
        Obstaculo afastado = new Circulo(new Ponto(5.0, 5.0), 1.0);
        CalcularRota calc = new CalcularRota();

        assertTrue(calc.rotaBloqueada(rota, List.of(bloqueante)));
        assertFalse(calc.rotaBloqueada(rota, List.of(afastado)));
    }

    /**
 * Responsabilidade: validar tempo rota através de um teste unitário.
 */
    @Test
    public void testTempoRota() {
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(10.0, 0.0) });
        CalcularRota calc = new CalcularRota();

        assertEquals(5.0, calc.tempoRota(rota, new Vetor(0.0, 0.0), 2.0), EPS);
    }

    /**
 * Responsabilidade: validar rota mais rapida escolhe direta através de um teste unitário.
 */
    @Test
    public void testRotaMaisRapidaEscolheDireta() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Porto c = TestSupport.porto("C", 0.0, 5.0);

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarPorto(c);
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), c.getPosicao() }));
        mapa.adicionarRota(new Route(new Ponto[] { c.getPosicao(), b.getPosicao() }));

        Route r = new CalcularRota().rotaMaisRapida(a, b, mapa, new Vetor(0.0, 0.0), 1.0);

        assertNotNull(r);
        assertEquals(2, r.getNumeroPontos());
        TestSupport.assertPonto(r.getInicio(), 0.0, 0.0);
        TestSupport.assertPonto(r.getFim(), 10.0, 0.0);
        assertEquals(10.0, r.comprimento(), EPS);
    }

    /**
 * Responsabilidade: validar rota mais rapida ignora rota bloqueada através de um teste unitário.
 */
    @Test
    public void testRotaMaisRapidaIgnoraRotaBloqueada() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Porto c = TestSupport.porto("C", 0.0, 5.0);

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarPorto(c);
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), c.getPosicao() }));
        mapa.adicionarRota(new Route(new Ponto[] { c.getPosicao(), b.getPosicao() }));
        mapa.adicionarObstaculoFixo(new Circulo(new Ponto(5.0, 0.0), 1.0));

        Route r = new CalcularRota().rotaMaisRapida(a, b, mapa, new Vetor(0.0, 0.0), 1.0);

        assertNotNull(r);
        assertEquals(3, r.getNumeroPontos());
        TestSupport.assertPonto(r.getPonto(0), 0.0, 0.0);
        TestSupport.assertPonto(r.getPonto(1), 0.0, 5.0);
        TestSupport.assertPonto(r.getPonto(2), 10.0, 0.0);
    }

    /**
 * Responsabilidade: validar rota mais rapida returns null when no path através de um teste unitário.
 */
    @Test
    public void testRotaMaisRapidaReturnsNullWhenNoPath() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);

        assertNull(new CalcularRota().rotaMaisRapida(a, b, mapa, new Vetor(0.0, 0.0), 1.0));
    }

    /**
 * Responsabilidade: validar calcular retorna optional vazio quando nao existe caminho através de um teste unitário.
 */
    @Test
    public void testCalcularRetornaOptionalVazioQuandoNaoExisteCaminho() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);

        assertTrue(new CalcularRota().calcular(a, b, mapa, new Vetor(0.0, 0.0), 1.0).isEmpty());
    }

    /**
 * Responsabilidade: validar rota bloqueada quando segmento esta dentro do obstaculo através de um teste unitário.
 */
    @Test
    public void testRotaBloqueadaQuandoSegmentoEstaDentroDoObstaculo() {
        Route rota = new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) });
        Obstaculo obstaculo = new Circulo(new Ponto(0.5, 0.0), 10.0);

        assertTrue(new CalcularRota().rotaBloqueada(rota, List.of(obstaculo)));
    }

    /**
 * Responsabilidade: validar invalid arguments através de um teste unitário.
 */
    @Test
    public void testInvalidArguments() {
        CalcularRota calc = new CalcularRota();
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 1.0, 0.0);
        MapaNavegacao mapa = new MapaNavegacao();
        Route rota = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });

        assertThrows(IllegalArgumentException.class, () -> calc.rotaMaisRapida(null, b, mapa, new Vetor(0.0, 0.0), 1.0));
        assertThrows(IllegalArgumentException.class, () -> calc.rotaMaisRapida(a, b, mapa, null, 1.0));
        assertThrows(IllegalArgumentException.class, () -> calc.rotaMaisRapida(a, b, mapa, new Vetor(0.0, 0.0), 0.0));
        assertThrows(IllegalArgumentException.class, () -> calc.rotaBloqueada(null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> calc.tempoRota(rota, new Vetor(0.0, 0.0), 0.0));
    }
}
