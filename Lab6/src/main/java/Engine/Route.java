package Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Responsabilidade: representar uma rota definida por uma sequência ordenada de pontos.
 *
 * @inv pontos != null && pontos.length >= 2 && pontos consecutivos diferentes
 */
public class Route {
    private final Ponto[] pontos;

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

    public int getNumeroPontos() {
        return pontos.length;
    }

    public int getNumeroSegmentos() {
        return pontos.length - 1;
    }

    public Ponto getPonto(int i) {
        return pontos[i];
    }

    public Ponto getInicio() {
        return pontos[0];
    }

    public Ponto getFim() {
        return pontos[pontos.length - 1];
    }

    public Ponto[] getPontos() {
        Ponto[] copia = new Ponto[pontos.length];
        System.arraycopy(pontos, 0, copia, 0, pontos.length);
        return copia;
    }

    public SegmentoReta getSegmento(int i) {
        if (i < 0 || i >= getNumeroSegmentos()) {
            throw new IndexOutOfBoundsException("Route.getSegmento: indice invalido");
        }
        return new SegmentoReta(pontos[i], pontos[i + 1]);
    }

    public List<SegmentoReta> getSegmentos() {
        List<SegmentoReta> segmentos = new ArrayList<>();
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            segmentos.add(getSegmento(i));
        }
        return Collections.unmodifiableList(segmentos);
    }

    public double comprimento() {
        double soma = 0.0;
        for (int i = 0; i < getNumeroSegmentos(); i++) {
            soma += comprimentoSegmento(i);
        }
        return soma;
    }

    public double comprimentoSegmento(int i) {
        return getSegmento(i).comprimento();
    }

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

    public double time(double vl) {
        if (vl <= 0.0) {
            throw new IllegalArgumentException("Route.time: velocidade invalida");
        }
        return comprimento() / vl;
    }

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

    public Route invertida() {
        Ponto[] invertidos = new Ponto[pontos.length];
        for (int i = 0; i < pontos.length; i++) {
            invertidos[i] = pontos[pontos.length - 1 - i];
        }
        return new Route(invertidos);
    }

    private Ponto interpolar(Ponto a, Ponto b, double fracao) {
        double x = a.getX() + (b.getX() - a.getX()) * fracao;
        double y = a.getY() + (b.getY() - a.getY()) * fracao;
        return new Ponto(x, y);
    }

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

    private void adicionarPontoUnico(List<Ponto> lista, Ponto p) {
        for (Ponto existente : lista) {
            if (Geometria.iguais(existente, p)) {
                return;
            }
        }
        lista.add(p);
    }

    private Ponto[] arrayOuNull(List<Ponto> pontos) {
        if (pontos.isEmpty()) {
            return null;
        }
        return pontos.toArray(new Ponto[0]);
    }
}
