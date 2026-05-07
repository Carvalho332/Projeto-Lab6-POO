package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (obstaculosMoveis == null) {
            throw new IllegalArgumentException("MapaNavegacao.definirObstaculosMoveis: lista null");
        }
        this.obstaculosMoveis = new ArrayList<>(obstaculosMoveis);
    }

    public Porto getPortoPorNome(String nome) {
        for (Porto p : portos) {
            if (p.getNome().equals(nome)) {
                return p;
            }
        }
        return null;
    }
}
