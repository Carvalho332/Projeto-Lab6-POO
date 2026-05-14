package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Responsabilidade: guardar os portos, rotas e obstáculos que formam o mapa usado pela simulação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class MapaNavegacao {
    private final List<Porto> portos;
    private final List<Route> rotas;
    private final List<Obstaculo> obstaculosFixos;
    private List<ObstaculoMovel> obstaculosMoveis;

    /**
 * Responsabilidade: construir uma instância de MapaNavegacao, validando os dados recebidos para preservar os invariantes.
 */
    public MapaNavegacao() {
        this.portos = new ArrayList<>();
        this.rotas = new ArrayList<>();
        this.obstaculosFixos = new ArrayList<>();
        this.obstaculosMoveis = new ArrayList<>();
    }

    /**
 * Responsabilidade: devolver portos associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Porto> getPortos() {
        return Collections.unmodifiableList(portos);
    }

    /**
 * Responsabilidade: devolver rotas associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Route> getRotas() {
        return Collections.unmodifiableList(rotas);
    }

    /**
 * Responsabilidade: devolver obstaculos fixos associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Obstaculo> getObstaculosFixos() {
        return Collections.unmodifiableList(obstaculosFixos);
    }

    /**
 * Responsabilidade: devolver obstaculos moveis associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<ObstaculoMovel> getObstaculosMoveis() {
        return Collections.unmodifiableList(obstaculosMoveis);
    }

    /**
 * Responsabilidade: devolver todos obstaculos associado à instância atual.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<Obstaculo> getTodosObstaculos() {
        List<Obstaculo> todos = new ArrayList<>(obstaculosFixos);
        todos.addAll(obstaculosMoveis);
        return Collections.unmodifiableList(todos);
    }

    /**
 * Responsabilidade: adicionar porto à estrutura respetiva mantendo a consistência dos dados.
 * @param p ponto analisado, acrescentado ou convertido.
 */
    public void adicionarPorto(Porto p) {
        if (p == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarPorto: porto null");
        }
        portos.add(p);
    }

    /**
 * Responsabilidade: adicionar rota à estrutura respetiva mantendo a consistência dos dados.
 * @param r r usado pelo método para cumprir a responsabilidade descrita.
 */
    public void adicionarRota(Route r) {
        if (r == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarRota: rota null");
        }
        rotas.add(r);
    }

    /**
 * Responsabilidade: adicionar obstaculo fixo à estrutura respetiva mantendo a consistência dos dados.
 * @param o o usado pelo método para cumprir a responsabilidade descrita.
 */
    public void adicionarObstaculoFixo(Obstaculo o) {
        if (o == null) {
            throw new IllegalArgumentException("MapaNavegacao.adicionarObstaculoFixo: obstaculo null");
        }
        obstaculosFixos.add(o);
    }

    /**
 * Responsabilidade: realizar a operação definir obstaculos moveis no contexto da classe MapaNavegacao.
 * @param obstaculosMoveis obstaculos moveis usado pelo método para cumprir a responsabilidade descrita.
 */
    public void definirObstaculosMoveis(List<ObstaculoMovel> obstaculosMoveis) {
        this.obstaculosMoveis = copiarListaSemNulls(obstaculosMoveis, "obstaculosMoveis");
    }

    /**
 * Responsabilidade: realizar a operação procurar porto por nome no contexto da classe MapaNavegacao.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @return Optional com a rota encontrada; Optional.empty() quando não existe rota válida.
 */
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

    /**
 * Responsabilidade: devolver porto por nome associado à instância atual.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @return objeto resultante da operação.
 */
    public Porto getPortoPorNome(String nome) {
        return procurarPortoPorNome(nome).orElse(null);
    }

    /**
 * Responsabilidade: indicar se a condição tem porto se verifica no estado atual.
 * @param porto porto associado à operação.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean temPorto(Porto porto) {
        return porto != null && portos.contains(porto);
    }

    /**
 * Responsabilidade: realizar a operação rota liga portos no contexto da classe MapaNavegacao.
 * @param rota rota analisada, percorrida ou construída pelo método.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean rotaLigaPortos(Route rota) {
        if (rota == null) {
            return false;
        }
        return portos.stream().anyMatch(p -> p.getPosicao().igual(rota.getInicio())) &&
                portos.stream().anyMatch(p -> p.getPosicao().igual(rota.getFim()));
    }

    /**
 * Responsabilidade: realizar a operação cumpre minimos enunciado no contexto da classe MapaNavegacao.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean cumpreMinimosEnunciado() {
        return portos.size() >= 4 && rotas.size() >= 6 &&
                obstaculosFixos.size() >= 4 && obstaculosMoveis.size() >= 2;
    }

    /**
 * Responsabilidade: realizar a operação copiar lista sem nulls no contexto da classe MapaNavegacao.
 * @param lista lista usado pelo método para cumprir a responsabilidade descrita.
 * @param nome nome textual usado para identificar porto, classe ou entidade.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
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
