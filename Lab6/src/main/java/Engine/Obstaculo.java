package Engine;

/**
 * Responsabilidade: representar um obstáculo geométrico que pode bloquear rotas.
 *
 * @version 2026-05-08
 */
public abstract class Obstaculo {
    /**
     * Determina os pontos de interseção entre este obstáculo e um segmento de reta.
     *
     * @param s segmento de reta
     * @return pontos de interseção ou null se não houver interseção com a fronteira
     */
    public abstract Ponto[] intersect(SegmentoReta s);

    /**
     * Indica se um ponto pertence ao interior ou à fronteira do obstáculo.
     * Este método complementa intersect(), permitindo detetar rotas totalmente
     * contidas dentro de um obstáculo.
     *
     * @param p ponto a testar
     * @return true se o ponto pertencer ao obstáculo
     */
    public abstract boolean contem(Ponto p);
}
