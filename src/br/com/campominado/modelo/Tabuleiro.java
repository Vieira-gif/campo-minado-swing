package br.com.campominado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
    private final int linhas;
    private final int colunas;
    private final int minas;

    private final List<Campo> campos  = new ArrayList<Campo>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarOsVizinhos();
        sortearMinas();
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador) {
        observadores.add(observador);
    }

    private void notificarObservador(boolean resultado) {
        observadores.forEach(observador -> observador.accept(new ResultadoEvento(resultado)));
    }

    public void abrir(int linha, int coluna) {
            campos.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.abrir());
    }


    public void marcar(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().ifPresent(c -> c.alterarMarcacao());
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();

        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        } while(minasArmadas < minas);
    }

    private void associarOsVizinhos() {
        for(Campo c1 : campos) {
            for(Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                Campo campo = new Campo(linha, coluna);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }
    }

    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(Campo::objetivoAlcancado);
    }

    public void reiniciar() {
        campos.forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo c, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR) {
            System.out.println("Perdeu");
            mostrarMinas();
            notificarObservador(false);
        } else if (objetivoAlcancado()) {
            System.out.println("Alcançado");
            notificarObservador(true);
        }
    }

    private void mostrarMinas() {
        campos
                .stream()
                .filter(campo -> campo.isMinado())
                .filter(campo -> !campo.isMinado())
                .forEach(campo -> campo.setAberto(true));
    }

    public void paraCadaCampo(Consumer<Campo> function) {
        campos.forEach(function);
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }
}
