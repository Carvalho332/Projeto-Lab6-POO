package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: representar um porto do mapa e gerir as viagens que aguardam saída nesse porto.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class Porto {
    private final String nome;
    private final Ponto posicao;
    private final List<Viagem> listaEspera;

    /**
 * Responsabilidade: construir uma instância de Porto, validando os dados recebidos para preservar os invariantes.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @param posicao posição atual do elemento no mapa.
 */
    public Porto(String nome, Ponto posicao) {
        if (nome == null || nome.isBlank() || posicao == null) {
            throw new IllegalArgumentException("Porto:iv");
        }
        this.nome = nome;
        this.posicao = posicao;
        this.listaEspera = new ArrayList<>();
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
 * Responsabilidade: devolver lista espera associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Viagem> getListaEspera() {
        return Collections.unmodifiableList(listaEspera);
    }

    /**
 * Responsabilidade: adicionar viagem à estrutura respetiva mantendo a consistência dos dados.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 */
    public void adicionarViagem(Viagem v) {
        if (v == null) {
            throw new IllegalArgumentException("Porto.adicionarViagem: viagem null");
        }
        listaEspera.add(v);
    }

    /**
 * Responsabilidade: realizar a operação remover viagem no contexto da classe Porto.
 * @param v v usado pelo método para cumprir a responsabilidade descrita.
 */
    public void removerViagem(Viagem v) {
        listaEspera.remove(v);
    }

    /**
 * Responsabilidade: indicar se a condição tem viagens em espera se verifica no estado atual.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean temViagensEmEspera() {
        return !listaEspera.isEmpty();
    }

    /**
 * Responsabilidade: devolver viagens para sair no tempo associado à instância atual.
 * @param tempo tempo usado pelo método para cumprir a responsabilidade descrita.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Viagem> getViagensParaSairNoTempo(int tempo) {
        if (tempo < 0) {
            throw new IllegalArgumentException("Porto.getViagensParaSairNoTempo: tempo invalido");
        }
        List<Viagem> resultado = new ArrayList<>();
        for (Viagem viagem : listaEspera) {
            if (viagem.getTempoSaida() == tempo) {
                resultado.add(viagem);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    /**
 * Responsabilidade: produzir uma representação textual estável para debug e testes.
 * @return texto formatado ou identificador pedido.
 */
    @Override
    public String toString() {
        return nome + " " + posicao;
    }
}
