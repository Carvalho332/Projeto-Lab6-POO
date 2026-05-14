package Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Responsabilidade: detetar conflitos entre navios e aplicar a regra de espera definida pelo enunciado.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public class GestorColisoes {
    private static final double RAIO_COLISAO = 1.0;
    private static final double DIAMETRO_COLISAO2 = 4.0 * RAIO_COLISAO * RAIO_COLISAO;

    /**
 * Responsabilidade: detetar colisões previstas e colocar em espera os navios com prioridade inferior.
 * @param navios navios ativos considerados no passo da simulação.
 */
    public void resolver(List<Navio> navios) {
        if (navios == null) {
            throw new IllegalArgumentException("GestorColisoes.resolver: navios null");
        }

        retomarNaviosEmEspera(navios);
        Map<Navio, Ponto> proximasPosicoes = calcularProximasPosicoes(navios);
        List<ConflitoNavios> conflitos = detetarConflitos(navios, proximasPosicoes);
        aplicarEspera(conflitos);
    }

    /**
 * Responsabilidade: verificar se dois navios colidem pelas próximas posições ou pelo cruzamento das trajetórias.
 * @param n1 primeiro navio da comparação.
 * @param n2 segundo navio da comparação.
 * @return true se a condição se verificar; false caso contrário.
 */
    public boolean haColisao(Navio n1, Navio n2) {
        if (n1 == null || n2 == null) {
            throw new IllegalArgumentException("GestorColisoes.haColisao: navios null");
        }
        if (n1.chegou() || n2.chegou()) {
            return false;
        }
        return areasColisaoSobrepostas(n1.proximaPosicao(), n2.proximaPosicao()) || trajetoriasCruzam(n1, n2);
    }

    /**
 * Responsabilidade: escolher o navio que fica parado segundo a ordem lexicográfica do código.
 * @param n1 primeiro navio da comparação.
 * @param n2 segundo navio da comparação.
 * @return objeto resultante da operação.
 */
    public Navio navioQueEspera(Navio n1, Navio n2) {
        if (n1 == null || n2 == null) {
            throw new IllegalArgumentException("GestorColisoes.navioQueEspera: navios null");
        }
        return n1.getCodigoViagem().compareTo(n2.getCodigoViagem()) <= 0 ? n1 : n2;
    }

    /**
 * Responsabilidade: realizar a operação retomar navios em espera no contexto da classe GestorColisoes.
 * @param navios navios ativos considerados no passo da simulação.
 */
    private void retomarNaviosEmEspera(List<Navio> navios) {
        for (Navio navio : navios) {
            if (navio == null) {
                throw new IllegalArgumentException("GestorColisoes.resolver: navio null");
            }
            navio.limparSinalizacaoColisao();
            navio.retomar();
        }
    }

    private Map<Navio, Ponto> calcularProximasPosicoes(List<Navio> navios) {
        Map<Navio, Ponto> resultado = new HashMap<>();
        for (Navio navio : navios) {
            resultado.put(navio, navio.proximaPosicao());
        }
        return resultado;
    }

    /**
 * Responsabilidade: realizar a operação detetar conflitos no contexto da classe GestorColisoes.
 * @param navios navios ativos considerados no passo da simulação.
 * @param proximasPosicoes proximas posicoes usado pelo método para cumprir a responsabilidade descrita.
 * @return lista com os elementos pedidos, sem permitir alteração indevida do estado interno.
 */
    private List<ConflitoNavios> detetarConflitos(List<Navio> navios, Map<Navio, Ponto> proximasPosicoes) {
        List<ConflitoNavios> conflitos = new ArrayList<>();
        for (int i = 0; i < navios.size(); i++) {
            for (int j = i + 1; j < navios.size(); j++) {
                Navio n1 = navios.get(i);
                Navio n2 = navios.get(j);
                if (n1.chegou() || n2.chegou()) {
                    continue;
                }
                if (haColisao(n1, n2, proximasPosicoes)) {
                    conflitos.add(new ConflitoNavios(n1, n2));
                }
            }
        }
        return conflitos;
    }

    /**
 * Responsabilidade: verificar se dois navios colidem pelas próximas posições ou pelo cruzamento das trajetórias.
 * @param n1 primeiro navio da comparação.
 * @param n2 segundo navio da comparação.
 * @param proximasPosicoes proximas posicoes usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean haColisao(Navio n1, Navio n2, Map<Navio, Ponto> proximasPosicoes) {
        return areasColisaoSobrepostas(proximasPosicoes.get(n1), proximasPosicoes.get(n2)) || trajetoriasCruzam(n1, n2);
    }

    /**
 * Responsabilidade: realizar a operação aplicar espera no contexto da classe GestorColisoes.
 * @param conflitos conflitos de colisão detetados no passo atual.
 */
    private void aplicarEspera(List<ConflitoNavios> conflitos) {
        Set<Navio> naviosQueDevemEsperar = new HashSet<>();
        for (ConflitoNavios conflito : conflitos) {
            naviosQueDevemEsperar.add(navioQueEspera(conflito.n1, conflito.n2));
        }
        for (Navio navio : naviosQueDevemEsperar) {
            navio.esperar();
        }
    }

    /**
 * Responsabilidade: realizar a operação areas colisao sobrepostas no contexto da classe GestorColisoes.
 * @param p1 p1 usado pelo método para cumprir a responsabilidade descrita.
 * @param p2 p2 usado pelo método para cumprir a responsabilidade descrita.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean areasColisaoSobrepostas(Ponto p1, Ponto p2) {
        return Geometria.distancia2(p1, p2) <= DIAMETRO_COLISAO2 + Geometria.EPS;
    }

    /**
 * Responsabilidade: realizar a operação trajetorias cruzam no contexto da classe GestorColisoes.
 * @param n1 primeiro navio da comparação.
 * @param n2 segundo navio da comparação.
 * @return true se a condição se verificar; false caso contrário.
 */
    private boolean trajetoriasCruzam(Navio n1, Navio n2) {
        Ponto a1 = n1.getPosicaoAtual();
        Ponto b1 = n1.proximaPosicao();
        Ponto a2 = n2.getPosicaoAtual();
        Ponto b2 = n2.proximaPosicao();

        if (Geometria.iguais(a1, b1) || Geometria.iguais(a2, b2)) {
            return false;
        }

        SegmentoReta s1 = new SegmentoReta(a1, b1);
        SegmentoReta s2 = new SegmentoReta(a2, b2);
        return s1.intersect(s2) != null;
    }

    private static class ConflitoNavios {
        private final Navio n1;
        private final Navio n2;

        private ConflitoNavios(Navio n1, Navio n2) {
            this.n1 = n1;
            this.n2 = n2;
        }
    }
}
