package Engine;

/**
 * Cliente simples apenas para testar manualmente a estrutura inicial.
 */
public class Cliente {
    public static void main(String[] args) {
        Porto a = new Porto("A", new Ponto(0, 0));
        Porto b = new Porto("B", new Ponto(10, 0));

        a.adicionarViagem(new Viagem(0, b, 2.0));

        MapaNavegacao mapa = new MapaNavegacao();
        mapa.adicionarPorto(a);
        mapa.adicionarPorto(b);
        mapa.adicionarRota(new Route(new Ponto[] { a.getPosicao(), b.getPosicao() }));

        Simulador simulador = new Simulador(mapa, new Vetor(0, 0));
        EstadoSimulacao estado = simulador.iniciar();

        for (int i = 0; i < 7; i++) {
            estado = simulador.passo();
            System.out.println("t=" + estado.getTempoAtual() + " navios=" + estado.getNavios().size());
        }
    }
}
