package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe MapaNavegacao, validando a gestão de portos, rotas, obstáculos fixos e obstáculos móveis.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class MapaNavegacaoTests {

    @Test
    public void testAdicionarAndGetters() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        Route r = new Route(new Ponto[] { a.getPosicao(), b.getPosicao() });
        Obstaculo o = new Circulo(new Ponto(5.0, 5.0), 1.0);

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarRota(r);
        mapa.adicionarObstaculoFixo(o);

        assertEquals(2, mapa.getPortos().size());
        assertEquals(1, mapa.getRotas().size());
        assertEquals(1, mapa.getObstaculosFixos().size());
        assertEquals(1, mapa.getTodosObstaculos().size());
        assertSame(a, mapa.getPortoPorNome("A"));
        assertNull(mapa.getPortoPorNome("Z"));
    }

    @Test
    public void testDefinirObstaculosMoveis() {
        MapaNavegacao mapa = new MapaNavegacao();
        List<ObstaculoMovel> moveis = new ArrayList<>();
        moveis.add(new ObstaculoMovel(new Ponto(1.0, 1.0), 1.0));

        mapa.definirObstaculosMoveis(moveis);
        moveis.clear();

        assertEquals(1, mapa.getObstaculosMoveis().size());
        assertEquals(1, mapa.getTodosObstaculos().size());
    }

    @Test
    public void testListsAreUnmodifiable() {
        MapaNavegacao mapa = new MapaNavegacao();

        assertThrows(UnsupportedOperationException.class,
                () -> mapa.getPortos().add(TestSupport.porto("A", 0.0, 0.0)));
        assertThrows(UnsupportedOperationException.class,
                () -> mapa.getRotas().add(new Route(new Ponto[] { new Ponto(0.0, 0.0), new Ponto(1.0, 0.0) })));
    }

    @Test
    public void testInvalidArguments() {
        MapaNavegacao mapa = new MapaNavegacao();

        assertThrows(IllegalArgumentException.class, () -> mapa.adicionarPorto(null));
        assertThrows(IllegalArgumentException.class, () -> mapa.adicionarRota(null));
        assertThrows(IllegalArgumentException.class, () -> mapa.adicionarObstaculoFixo(null));
        assertThrows(IllegalArgumentException.class, () -> mapa.definirObstaculosMoveis(null));
    }
}
