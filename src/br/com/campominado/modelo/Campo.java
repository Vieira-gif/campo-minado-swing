package br.com.campominado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean aberto = false;
    private boolean minado = false;
    private boolean marcado = false;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }

    private void notificarObservador(CampoEvento evento) {
        observadores.forEach(observador -> observador.eventoOcorreu(this, evento));
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if(deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if(deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alterarMarcacao() {
        if (!aberto) {
            marcado = !marcado;

            if (marcado){
                notificarObservador(CampoEvento.MARCAR);
            } else {
                notificarObservador(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir(){
        if(!aberto && !marcado) {
            if(minado) {
                notificarObservador(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            if (vizinhacaSegura()) {
                vizinhos.forEach(vizinho -> vizinho.abrir());
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean vizinhacaSegura() {
        return vizinhos.stream().noneMatch(vizinho -> vizinho.minado);
    }

    public boolean isMarcado () {
        return marcado;
    }

    void minar() {
        minado = true;
    }

    public boolean isAberto() {
        return aberto;
    }

    public boolean isMinado() {
        return minado;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if (aberto) notificarObservador(CampoEvento.ABRIR);
    }

    public boolean isFechado() {
        return !isAberto();
    }

    public int getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;

        return desvendado || protegido;
    }

    public int minasNaVizinhaca(){
        return (int) vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservador(CampoEvento.REINICIAR);
    }

    @Override
    public String toString() {
        if (marcado) {
            return "x";
        } else if (aberto && minado) {
            return "*";
        } else if (aberto && minasNaVizinhaca() > 0) {
            return Long.toString(minasNaVizinhaca());
        } else if (aberto) {
            return "";
        } else {
            return "?";
        }
    }
}
