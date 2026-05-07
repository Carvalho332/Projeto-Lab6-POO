package Engine;

import java.util.List;

/**
 * Responsabilidade: detetar e resolver colisões entre navios.
 *
 * A área de colisão é modelada como um círculo de raio 1 centrado na próxima posição.
 * Se dois navios forem para áreas que se intersectam, o de código lexicograficamente menor espera.
 */
public class GestorColisoes {
    private static final double RAIO_COLISAO = 1.0;

    public void resolver(List<Navio> navios) {
        if (navios == null) {
            throw new IllegalArgumentException("GestorColisoes.resolver: navios null");
        }

        for (Navio n : navios) {
            if (n.getEstado() == EstadoNavio.EM_ESPERA) {
                n.retomar();
            }
            n.limparSinalizacaoColisao();
        }

        for (int i = 0; i < navios.size(); i++) {
            for (int j = i + 1; j < navios.size(); j++) {
                Navio n1 = navios.get(i);
                Navio n2 = navios.get(j);

                if (haColisao(n1, n2)) {
                    Navio espera = navioQueEspera(n1, n2);
                    espera.esperar();
                }
            }
        }
    }

    public boolean haColisao(Navio n1, Navio n2) {
        if (n1 == null || n2 == null) {
            throw new IllegalArgumentException("GestorColisoes.haColisao: navios null");
        }

        if (n1.getEstado() == EstadoNavio.CHEGOU || n2.getEstado() == EstadoNavio.CHEGOU) {
            return false;
        }

        Ponto p1 = n1.proximaPosicao();
        Ponto p2 = n2.proximaPosicao();

        return p1.dist(p2) <= 2.0 * RAIO_COLISAO;
    }

    public Navio navioQueEspera(Navio n1, Navio n2) {
        if (n1 == null || n2 == null) {
            throw new IllegalArgumentException("GestorColisoes.navioQueEspera: navios null");
        }

        return n1.getCodigoViagem().compareTo(n2.getCodigoViagem()) <= 0 ? n1 : n2;
    }
}
