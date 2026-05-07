package Engine;

/**
 * Responsabilidade: representar um obstáculo geométrico.
 */
public abstract class Obstaculo {
    /**
     * Determina os pontos de interseção entre este obstáculo e um segmento de reta.
     *
     * @param s segmento de reta
     * @return pontos de interseção ou null
     */
    public abstract Ponto[] intersect(SegmentoReta s);
}
