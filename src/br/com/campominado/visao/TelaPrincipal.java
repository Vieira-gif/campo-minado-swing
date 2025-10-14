package br.com.campominado.visao;

import br.com.campominado.modelo.Tabuleiro;

public class TelaPrincipal extends javax.swing.JFrame {
    public TelaPrincipal(){

        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 5);

        add(new PainelTabuleiro(tabuleiro));

        setTitle("Campo Minado");
        setSize(690, 438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {

        new TelaPrincipal();
    }
}
