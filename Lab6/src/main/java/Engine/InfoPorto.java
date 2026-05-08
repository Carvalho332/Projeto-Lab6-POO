package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO imutável com a informação de um porto necessária para o GUI.
 *
 * @version 2026-05-08
 * @inv nome != null && posicao != null && viagensEmEspera != null
 */
public class InfoPorto {
    private final String nome;
    private final Ponto posicao;
    private final List<InfoViagem> viagensEmEspera;

    public InfoPorto(String nome, Ponto posicao, List<InfoViagem> viagensEmEspera) {
        if (nome == null || nome.isBlank() || posicao == null) {
            throw new IllegalArgumentException("InfoPorto:iv");
        }
        this.nome = nome;
        this.posicao = posicao;
        this.viagensEmEspera = copiarListaSemNulls(viagensEmEspera);
    }

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

    public String getNome() {
        return nome;
    }

    public Ponto getPosicao() {
        return posicao;
    }

    public List<InfoViagem> getViagensEmEspera() {
        return Collections.unmodifiableList(viagensEmEspera);
    }

    public boolean temViagensEmEspera() {
        return !viagensEmEspera.isEmpty();
    }

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
