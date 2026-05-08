package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Responsabilidade: agregar portos, rotas e obstáculos do mapa de navegação.
 */
public class MapaNavegacao {
    private final List<Porto> portos;
    private final List<Route> rotas;
    private final List<Obstaculo> obstaculosFixos;
    private List<ObstaculoMovel> obstaculosMoveis;

    public MapaNavegacao() {
        this.portos = new ArrayList<>();
        this.rotas = new ArrayList<>();
        this.obstaculosFixos = new ArrayList<>();
        this.obstaculosMoveis = new ArrayList<>();
    }

    public List<Porto> getPortos() {
        return Collections.unmodifiableList(portos);
    }

    public List<Route> getRotas() {
        return Collections.unmodifiableList(rotas);
    }

    public List<Obstaculo> getObstaculosFixos() {
        return Collections.unmodifiableList(obstaculosFixos);
    }

    public List<ObstaculoMovel> getObstaculosMoveis() {
        return Collections.unmodifiableList(obstaculosMoveis);
    }

    public List<Obstaculo> getTodosObstaculos() {
        List<Obstaculo> todos = new ArrayList<>(obstaculosFixos);
        todos.addAll(obstaculosMoveis);
        return Collections.unmodifiableList(todos);
    }

    public void adicionarPorto(Porto p) {
        if (p == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarPorto: porto null");
        }
        portos.add(p);
    }

    public void adicionarRota(Route r) {
        if (r == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarRota: rota null");
        }
        rotas.add(r);
    }

    public void adicionarObstaculoFixo(Obstaculo o) {
        if (o == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarObstaculoFixo: obstaculo null");
        }
        obstaculosFixos.add(o);
    }

    public void definirObstaculosMoveis(List<ObstaculoMovel> obstaculosMoveis) {
        this.obstaculosMoveis = copiarListaSemNulls(obstaculosMoveis, "obstaculosMoveis");
    }

    public Optional<Porto> procurarPortoPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return Optional.empty();
        }
        for (Porto p : portos) {
            if (p.getNome().equals(nome)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Porto getPortoPorNome(String nome) {
        return procurarPortoPorNome(nome).orElse(null);
    }

    public boolean temPorto(Porto porto) {
        return porto != null && portos.contains(porto);
    }

    public boolean rotaLigaPortos(Route rota) {
        if (rota == null) {
            return false;
        }
        return portos.stream().anyMatch(p -> p.getPosicao().igual(rota.getInicio())) &&
                portos.stream().anyMatch(p -> p.getPosicao().igual(rota.getFim()));
    }

    public boolean cumpreMinimosEnunciado() {
        return portos.size() >= 4 && rotas.size() >= 6 &&
                obstaculosFixos.size() >= 4 && obstaculosMoveis.size() >= 2;
    }

    private <T> List<T> copiarListaSemNulls(List<T> lista, String nome) {
        if (lista == null) {
            throw new IllegalArgumentException("MapaNavegacao: " + nome + " null");
        }
        List<T> copia = new ArrayList<>();
        for (T elemento : lista) {
            if (elemento == null) {
                throw new IllegalArgumentException("MapaNavegacao: " + nome + " contem null");
            }
            copia.add(elemento);
        }
        return copia;
    }
}
