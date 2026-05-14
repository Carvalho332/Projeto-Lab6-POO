package Engine;

/**
 * Responsabilidade: definir o comportamento comum dos obstáculos que podem bloquear rotas.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv a classe mantém válidos os dados necessários à sua responsabilidade.
 */
public abstract class Obstaculo {

    /**
 * Responsabilidade: calcular interseções entre este objeto geométrico e o objeto recebido.
 * @param s s usado pelo método para cumprir a responsabilidade descrita.
 * @return array com os elementos calculados ou copiados.
 */
    public abstract Ponto[] intersect(SegmentoReta s);

    /**
 * Responsabilidade: verificar se o ponto pertence à área ou ao segmento representado.
 * @param p ponto analisado, acrescentado ou convertido.
 * @return true se a condição se verificar; false caso contrário.
 */
    public abstract boolean contem(Ponto p);
}
