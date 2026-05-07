package Engine;

/**
 * Responsabilidade: representar um obstáculo móvel.
 *
 * Nota: segundo os esclarecimentos do professor, o obstáculo móvel muda de posição
 * apenas no início de cada simulação e permanece fixo durante essa simulação.
 */
public class ObstaculoMovel extends Circulo {
    public ObstaculoMovel(Ponto centro, double raio) {
        super(centro, raio);
    }
}
