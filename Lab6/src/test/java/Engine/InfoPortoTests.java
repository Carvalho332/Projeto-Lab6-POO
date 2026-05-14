package Engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
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
public class InfoPortoTests {
    /**
 * Responsabilidade: validar constructor and getters através de um teste unitário.
 */
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

    /**
 * Responsabilidade: validar constructor from porto através de um teste unitário.
 */
    @Test
    public void testConstructorFromPorto() {
        Porto a = TestSupport.porto("A", 0.0, 0.0);
        Porto b = TestSupport.porto("B", 10.0, 0.0);
        a.adicionarViagem(new Viagem(5, b, 2.0));

        InfoPorto info = new InfoPorto(a);

        assertEquals("A", info.getNome());
        assertEquals(1, info.getViagensEmEspera().size());
    }

    /**
 * Responsabilidade: validar viagens em espera is unmodifiable através de um teste unitário.
 */
    @Test
    public void testViagensEmEsperaIsUnmodifiable() {
        InfoPorto info = new InfoPorto("A", new Ponto(0.0, 0.0), List.of());

        assertThrows(UnsupportedOperationException.class,
                () -> info.getViagensEmEspera().add(new InfoViagem(0, "B", 1.0)));
    }

    /**
 * Responsabilidade: validar invalid info porto através de um teste unitário.
 */
    @Test
    public void testInvalidInfoPorto() {
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto(null, new Ponto(0.0, 0.0), List.of()));
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto("A", null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new InfoPorto("A", new Ponto(0.0, 0.0), null));
    }
}
