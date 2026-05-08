package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: representar um porto, a sua posição e a lista de viagens em espera.
 *
 * @inv nome != null && !nome.isBlank() && posicao != null
 */
public class Porto {
    private final String nome;
    private final Ponto posicao;
    private final List<Viagem> listaEspera;

    public Porto(String nome, Ponto posicao) {
        if (nome == null || nome.isBlank() || posicao == null) {
            throw new IllegalArgumentException("Porto:iv");
        }
        this.nome = nome;
        this.posicao = posicao;
        this.listaEspera = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public Ponto getPosicao() {
        return posicao;
    }

    public List<Viagem> getListaEspera() {
        return Collections.unmodifiableList(listaEspera);
    }

    public void adicionarViagem(Viagem v) {
        if (v == null) {
            throw new IllegalArgumentException("Porto.adicionarViagem: viagem null");
        }
        listaEspera.add(v);
    }

    public void removerViagem(Viagem v) {
        listaEspera.remove(v);
    }

    public boolean temViagensEmEspera() {
        return !listaEspera.isEmpty();
    }

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

    @Override
    public String toString() {
        return nome + " " + posicao;
    }
}
