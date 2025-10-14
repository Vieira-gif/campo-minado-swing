package br.com.campominado.visao;

import br.com.campominado.modelo.Tabuleiro;

import java.net.InterfaceAddress;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Console {
    private Tabuleiro tabuleiro;
    private Scanner entrada = new Scanner(System.in);

    public Console(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        executarJogo();
    }

    private void executarJogo() {
        try {
            boolean continuar = true;

            while (continuar) {
                cicloDoJogo();

                System.out.println("Outra partida? [S/N]: ");
                String resposta = entrada.nextLine();

                if ("n".equalsIgnoreCase(resposta)) {
                    continuar = false;
                } else {
                    tabuleiro.reiniciar();
                }
            }
        } catch (Exception e){
            System.out.println("Saiu do jogo....");
        } finally {
            entrada.close();
        }
    }

    private void cicloDoJogo() {
        try {

            while (!tabuleiro.objetivoAlcancado()){
                System.out.println(tabuleiro);
                String digitado = capturarValorDigitado("Digite (x, y): ");

                Iterator<Integer> xy = Arrays.stream(digitado.split(",")).map(e -> Integer.parseInt(e.trim())).iterator();

                digitado = capturarValorDigitado("1 - Abrir ou 2 - (Des)Marcar");

                if (digitado.equalsIgnoreCase("1")){
                    tabuleiro.abrir(xy.next(), xy.next());
                } else if (digitado.equalsIgnoreCase("2")){
                    tabuleiro.marcar(xy.next(), xy.next());
                }
            }

            System.out.println("Você ganhou! :D");
        } catch (Exception e) {
            System.out.println(tabuleiro);
            System.out.println("Você perdeu... :(");
        }
    }

    private String capturarValorDigitado(String texto) throws Exception {
        System.out.print(texto);
        String digitado = entrada.nextLine();

        if ("sair".equalsIgnoreCase(digitado)){
            throw new Exception();
        }

        return digitado;
    }
}
