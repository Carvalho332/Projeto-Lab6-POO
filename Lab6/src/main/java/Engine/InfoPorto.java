package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO imutável com a informação de um porto necessária para o GUI.
 */
public class InfoPorto {
    private final String nome;
    private final Ponto posicao;
    private final List<Viagem> viagensEmEspera;

    public InfoPorto(String nome, Ponto posicao, List<Viagem> viagensEmEspera) {
        if (nome == null || posicao == null || viagensEmEspera == null) {
            throw new IllegalArgumentException("InfoPorto:iv");
        }
        this.nome = nome;
        this.posicao = posicao;
        this.viagensEmEspera = new ArrayList<>(viagensEmEspera);
    }

    public InfoPorto(Porto porto) {
        this(porto.getNome(), porto.getPosicao(), porto.getListaEspera());
    }

    public String getNome() {
        return nome;
    }

    public Ponto getPosicao() {
        return posicao;
    }

    public List<Viagem> getViagensEmEspera() {
        return Collections.unmodifiableList(viagensEmEspera);
    }
}
