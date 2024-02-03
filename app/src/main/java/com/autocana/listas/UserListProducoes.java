package com.autocana.listas;

public class UserListProducoes {

    String nomePlantacao, dataInicial, dataFinal, colhido, descricao;

    public UserListProducoes(){

    }

    public UserListProducoes(String nomePlantacao, String dataFinal, String dataInicial, String descricao) {
        this.nomePlantacao = nomePlantacao;
        this.dataFinal = dataFinal;
        this.dataInicial = dataInicial;
        this.descricao = descricao;
    }

    public String getNomePlantacao() {
        return nomePlantacao;
    }

    public void setNomePlantacao(String nomePlantacao) {
        this.nomePlantacao = nomePlantacao;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getColhido() {
        return colhido;
    }

    public void setColhido(String colhido) {
        this.colhido = colhido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
