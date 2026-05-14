package Engine;

/**
 * Responsabilidade: representar um obstáculo móvel colocado numa nova posição no início de cada simulação.
 * @author Francisco Mestre Nº 76914
 * @author Diogo Carvalho Nº 90247
 * @author Rudy Silva Nº 88487
 * @version 26-04-2026
 * @inv o centro não é nulo e o raio é positivo.
 */
public class ObstaculoMovel extends Circulo {
    /**
 * Responsabilidade: construir uma instância de ObstaculoMovel, validando os dados recebidos para preservar os invariantes.
 * @param centro centro usado pelo método para cumprir a responsabilidade descrita.
 * @param raio raio usado pelo método para cumprir a responsabilidade descrita.
 */
    public ObstaculoMovel(Ponto centro, double raio) {
        super(centro, raio);
    }
}
