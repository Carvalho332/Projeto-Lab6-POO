package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Responsabilidade: testar a classe InfoPorto, validando a informação enviada pelo Engine ao GUI sobre cada porto e respetiva lista de espera.
 *
 * Autores:
 * - Francisco Mestre Nº 76914
 * - Diogo Carvalho Nº 90247
 * - Rudy Silva Nº 88487
 *
 * Data: 26/04/2026
 */
public class InfoPortoTests {
    @Test
    public void testConstructorAndGetters() {
        Porto destino = TestSupport.porto("B", 10.0, 0.0);
        InfoViagem viagem = new InfoViagem(0, destino.getNome(), 1.0);
        List<InfoViagem> viagens = new ArrayList<>();
        viagens.add(viagem);

        InfoPorto info = new InfoPorto("A", new Ponto(0.0, 0.0), viagens);
        viagens.clear();

        assertEquals("A", info.getNome());
        TestSupport.assertPonto(info.getPosicao(), 0.0, 0.0);
        assertEquals(1, info.getViagensEmEspera().size());
        assertSame(viagem, info.getViagensEmEspera().get(0));
    }

    @Test
    public void testConstructorFromPorto() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        a.adicionarViagem(new Viagem(5, b, 2.0));

        InfoPorto info = new InfoPorto(a);

        assertEquals("A", info.getNome());
        assertEquals(1, info.getViagensEmEspera().size());
    }

    @Test
    public void testViagensEmEsperaIsUnmodifiable() {
        InfoPorto info = new InfoPorto("A", new Ponto(0.0, 0.0), List.of());

        assertThrows(UnsupportedOperationException.class,
                () -> info.getViagensEmEspera().add(new InfoViagem(0, "B", 1.0)));
    }

    @Test
    public void testInvalidInfoPorto() {
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto(null, new Ponto(0.0, 0.0), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto("A", null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto("A", new Ponto(0.0, 0.0), null));
    }
}
