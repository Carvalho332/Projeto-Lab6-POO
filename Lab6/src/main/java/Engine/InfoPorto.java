package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: transportar para o GUI a informação de um porto e das suas viagens em espera.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv os dados transportados não expõem listas internas modificáveis.
 */
public class InfoPorto {
    private final String nome;
    private final Ponto posicao;
    private final List<InfoViagem> viagensEmEspera;

    /**
 * Responsabilidade: construir uma instância de InfoPorto, validando os dados recebidos para preservar os invariantes.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @param posicao posição atual do elemento no mapa.
 * @param viagensEmEspera viagens em espera usado pelo método para cumprir a responsabilidade descrita.
 */
    public InfoPorto(String nome, Ponto posicao, List<InfoViagem> viagensEmEspera) {
        if (nome == null || nome.isBlank() || posicao == null) {
            throw new IllegalArgumentException("InfoPorto:iv");
        }
        this.nome = nome;
        this.posicao = posicao;
        this.viagensEmEspera = copiarListaSemNulls(viagensEmEspera);
    }

    /**
 * Responsabilidade: construir uma instância de InfoPorto, validando os dados recebidos para preservar os invariantes.
 * @param porto porto associado à operação.
 */
    public InfoPorto(Porto porto) {
        if (porto == null) {
            throw new IllegalArgumentException("InfoPorto: porto null");
        }
        this.nome = porto.getNome();
        this.posicao = porto.getPosicao();
        this.viagensEmEspera = new ArrayList<>();
        for (Viagem viagem : porto.getListaEspera()) {
            this.viagensEmEspera.add(new InfoViagem(viagem));
        }
    }

    /**
 * Responsabilidade: devolver nome associado à instância atual.
 * @return texto formatado ou identificador pedido.
 */
    public String getNome() {
        return nome;
    }

    /**
 * Responsabilidade: devolver posicao associado à instância atual.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getPosicao() {
        return posicao;
    }

    /**
 * Responsabilidade: devolver viagens em espera associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<InfoViagem> getViagensEmEspera() {
        return Collections.unmodifiableList(viagensEmEspera);
    }

    /**
 * Responsabilidade: indicar se a condição tem viagens em espera se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean temViagensEmEspera() {
        return !viagensEmEspera.isEmpty();
    }

    /**
 * Responsabilidade: realizar a operação copiar lista sem nulls no contexto da classe InfoPorto.
 * @param lista lista usado pelo método para cumprir a responsabilidade descrita.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<InfoViagem> copiarListaSemNulls(List<InfoViagem> lista) {
        if (lista == null) {
            throw new IllegalArgumentException("InfoPorto: viagensEmEspera null");
        }
        List<InfoViagem> copia = new ArrayList<>();
        for (InfoViagem viagem : lista) {
            if (viagem == null) {
                throw new IllegalArgumentException("InfoPorto: viagem null");
            }
            copia.add(viagem);
        }
        return copia;
    }
}
