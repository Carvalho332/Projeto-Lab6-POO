package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: representar uma rota poligonal formada por pontos consecutivos e segmentos navegáveis.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a rota contém pelo menos dois pontos.
 * @inv não existem pontos consecutivos iguais.
 */
public class Route {
    private final Ponto[] pontos;

    /**
 * Responsabilidade: construir uma instância de Route, validando os dados recebidos para preservar os invariantes.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 */
    public Route(Ponto[] pontos) {
        if (pontos == null || pontos.length < 2) {
            throw new IllegalArgumentException("Route:iv");
        }

        this.pontos = new Ponto[pontos.length];
        for (int i = 0; i < pontos.length; i++) {
            if (pontos[i] == null) {
                throw new IllegalArgumentException("Route: ponto null");
            }
            this.pontos[i] = pontos[i];
            if (i > 0 && Geometria.iguais(this.pontos[i - 1], this.pontos[i])) {
                throw new IllegalArgumentException("Route:iv");
            }
        }
    }

    /**
 * Responsabilidade: devolver a quantidade de pontos da rota.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getNumeroPontos() {
        return pontos.length;
    }

    /**
 * Responsabilidade: devolver a quantidade de segmentos consecutivos da rota.
 * @return valor inteiro associado à contagem, índice ou tempo calculado.
 */
    public int getNumeroSegmentos() {
        return pontos.length - 1;
    }

    /**
 * Responsabilidade: devolver o ponto da rota no índice indicado.
 * @param i índice do elemento a obter.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getPonto(int i) {
        return pontos[i];
    }

    /**
 * Responsabilidade: devolver o ponto inicial da rota.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getInicio() {
        return pontos[0];
    }

    /**
 * Responsabilidade: devolver o ponto final da rota.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto getFim() {
        return pontos[pontos.length - 1];
    }

    /**
 * Responsabilidade: devolver uma cópia dos pontos que definem a rota.
 * @return array com os elementos calculados ou copiados.
 */
    public Ponto[] getPontos() {
        Ponto[] copia = new Ponto[pontos.length];
        System.arraycopy(pontos, 0, copia, 0, pontos.length);
        return copia;
    }

    /**
 * Responsabilidade: devolver o segmento de reta correspondente ao índice indicado.
 * @param i índice do elemento a obter.
 * @return objeto resultante da operação.
 */
    public SegmentoReta getSegmento(int i) {
        if (i < 0 || i >= getNumeroSegmentos()) {
            throw new IndexOutOfBoundsException("Route.getSegmento: indice invalido");
        }
        return new SegmentoReta(pontos[i], pontos[i + 1]);
    }

    /**
 * Responsabilidade: devolver todos os segmentos da rota numa lista não modificável.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    public List<SegmentoReta> getSegmentos() {
        List<SegmentoReta> segmentos = new ArrayList<>();
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            segmentos.add(getSegmento(i));
        }
        return Collections.unmodifiableList(segmentos);
    }

    /**
 * Responsabilidade: calcular o comprimento total do objeto geométrico.
 * @return distância ou comprimento calculado.
 */
    public double comprimento() {
        double soma = 0.0;
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            soma += comprimentoSegmento(i);
        }
        return soma;
    }

    /**
 * Responsabilidade: calcular o comprimento do segmento da rota indicado pelo índice.
 * @param i índice do elemento a obter.
 * @return distância ou comprimento calculado.
 */
    public double comprimentoSegmento(int i) {
        return getSegmento(i).comprimento();
    }

    /**
 * Responsabilidade: verificar se um ponto pertence a algum segmento da rota.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean contemPonto(Ponto p) {
        if (p == null) {
            throw new IllegalArgumentException("Route.contemPonto: ponto null");
        }
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            if (getSegmento(i).contem(p)) {
                return true;
            }
        }
        return false;
    }

    /**
 * Responsabilidade: calcular o tempo de percurso usando comprimento e velocidade linear.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return tempo de percurso calculado.
 */
    public double time(double vl) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("Route.time: velocidade invalida");
        }
        return comprimento() / vl;
    }

    /**
 * Responsabilidade: calcular o tempo necessário para chegar a um ponto pertencente à rota.
 * @param p ponto analisado, acrescentado ou convertido.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return tempo de percurso calculado.
 */
    public double tempoAtePonto(Ponto p, double vl) {
        if (p == null || vl <= 0.0) {
            throw new IllegalArgumentException("Route.tempoAtePonto: argumentos invalidos");
        }

        double distanciaAcumulada = 0.0;
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            Ponto a = pontos[i];
            Ponto b = pontos[i + 1];
            SegmentoReta segmento = new SegmentoReta(a, b);
            if (segmento.contem(p)) {
                return (distanciaAcumulada + a.dist(p)) / vl;
            }
            distanciaAcumulada += a.dist(b);
        }

        throw new IllegalArgumentException("Route.tempoAtePonto: ponto nao pertence a rota");
    }

    /**
 * Responsabilidade: calcular a velocidade vetorial que compensa a corrente no percurso.
 * @param w vetor da corrente a compensar no cálculo da velocidade própria do navio.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @return array com os elementos calculados ou copiados.
 */
    public Vetor[] speed(Vetor w, double vl) {
        if (w == null) {
            throw new IllegalArgumentException("Route.speed: corrente null");
        }
        Vetor[] velocidades = new Vetor[getNumeroSegmentos()];
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            AutoPilot ap = new AutoPilot(pontos[i], pontos[i + 1]);
            double t = ap.time(vl);
            velocidades[i] = ap.speed(w, t);
        }
        return velocidades;
    }

    /**
 * Responsabilidade: calcular a posição atingida após determinado tempo de viagem.
 * @param vl velocidade linear pretendida ao longo da rota.
 * @param t tempo disponível para percorrer o segmento.
 * @return ponto calculado ou guardado pela instância.
 */
    public Ponto position(double vl, double t) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("Route.position: velocidade invalida");
        }
        if (t <= 0.0) {
            return getInicio();
        }

        double tempoDecorrido = 0.0;
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            double tempoSegmento = comprimentoSegmento(i) / vl;

            if (t <= tempoDecorrido + tempoSegmento + Geometria.EPS) {
                double dt = t - tempoDecorrido;
                double fracao = Math.max(0.0, Math.min(1.0, dt / tempoSegmento));
                return interpolar(pontos[i], pontos[i + 1], fracao);
            }

            tempoDecorrido += tempoSegmento;
        }

        return getFim();
    }

    /**
 * Responsabilidade: criar uma rota parcial entre dois pontos existentes na rota original.
 * @param inicio inicio usado pelo método para cumprir a responsabilidade descrita.
 * @param fim fim usado pelo método para cumprir a responsabilidade descrita.
 * @return rota calculada ou construída pela operação.
 */
    public Route subRota(Ponto inicio, Ponto fim) {
        if (inicio == null || fim == null || !contemPonto(inicio) || !contemPonto(fim)) {
            throw new IllegalArgumentException("Route.subRota: pontos invalidos");
        }
        if (Geometria.iguais(inicio, fim)) {
            throw new IllegalArgumentException("Route.subRota: inicio e fim iguais");
        }

        double tInicio = parametroAoLongoDaRota(inicio);
        double tFim = parametroAoLongoDaRota(fim);
        if (tFim < tInicio) {
            return subRota(fim, inicio).invertida();
        }

        List<Ponto> novos = new ArrayList<>();
        adicionarPontoUnico(novos, inicio);
        for (Ponto p : pontos) {
            double tp = parametroAoLongoDaRota(p);
            if (tp > tInicio + Geometria.EPS && tp < tFim - Geometria.EPS) {
                adicionarPontoUnico(novos, p);
            }
        }
        adicionarPontoUnico(novos, fim);
        return new Route(novos.toArray(new Ponto[0]));
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
    public Ponto[] intersect(SegmentoReta s) {
        if (s == null) {
            throw new IllegalArgumentException("Route.intersect: segmento null");
        }

        List<Ponto> intersecoes = new ArrayList<>();
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            Ponto p = getSegmento(i).intersect(s);
            if (p != null) {
                adicionarPontoUnico(intersecoes, p);
            }
        }
        return arrayOuNull(intersecoes);
    }

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param o o usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
    public Ponto[] intersect(Obstaculo o) {
        if (o == null) {
            throw new IllegalArgumentException("Route.intersect: obstaculo null");
        }

        List<Ponto> intersecoes = new ArrayList<>();
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            Ponto[] pontosIntersecao = o.intersect(getSegmento(i));
            if (pontosIntersecao != null) {
                for (Ponto p : pontosIntersecao) {
                    if (p != null) {
                        adicionarPontoUnico(intersecoes, p);
                    }
                }
            }
        }
        return arrayOuNull(intersecoes);
    }

    /**
 * Responsabilidade: realizar a operação invertida no contexto da classe Route.
 * @return rota calculada ou construída pela operação.
 */
    public Route invertida() {
        Ponto[] invertidos = new Ponto[pontos.length];
        for (int i = 0; i < pontos.length; i++) {
            invertidos[i] = pontos[pontos.length - 1 - i];
        }
        return new Route(invertidos);
    }

    /**
 * Responsabilidade: realizar a operação interpolar no contexto da classe Route.
 * @param a primeiro ponto, vetor ou valor da operação.
 * @param b segundo ponto, vetor ou valor da operação.
 * @param fracao fracao usado pelo método para cumprir a responsabilidade descrita.
 * @return ponto calculado ou guardado pela instância.
 */
    private Ponto interpolar(Ponto a, Ponto b, double fracao) {
        double x = a.getX() + (b.getX() - a.getX()) * fracao;
        double y = a.getY() + (b.getY() - a.getY()) * fracao;
        return new Ponto(x, y);
    }

    /**
 * Responsabilidade: realizar a operação parametro ao longo da rota no contexto da classe Route.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return valor real resultante do cálculo.
 */
    private double parametroAoLongoDaRota(Ponto p) {
        double acumulado = 0.0;
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            Ponto a = pontos[i];
            Ponto b = pontos[i + 1];
            SegmentoReta segmento = new SegmentoReta(a, b);
            if (segmento.contem(p)) {
                return acumulado + a.dist(p);
            }
            acumulado += a.dist(b);
        }
        return Double.NaN;
    }

    /**
 * Responsabilidade: adicionar ponto unico à estrutura respetiva mantendo a consistência dos dados.
 * @param lista lista usado pelo método para cumprir a responsabilidade descrita.
 * @param p ponto analisado, acrescentado ou convertido.
 */
    private void adicionarPontoUnico(List<Ponto> lista, Ponto p) {
        for (Ponto existente : lista) {
            if (Geometria.iguais(existente, p)) {
                return;
            }
        }
        lista.add(p);
    }

    /**
 * Responsabilidade: realizar a operação array ou null no contexto da classe Route.
 * @param pontos lista ou array de pontos usado para construir uma rota ou um polígono.
 * @return array com os elementos calculados ou copiados.
 */
    private Ponto[] arrayOuNull(List<Ponto> pontos) {
        if (pontos.isEmpty()) {
            return null;
        }
        return pontos.toArray(new Ponto[0]);
    }
}
