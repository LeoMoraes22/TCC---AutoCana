package com.autocana.listas;

public class UserListColheita {

    String plantacao, anotacoes, placa, unidade, dataColheita, quantidade;

    public UserListColheita(){

    }

    public UserListColheita(String plantacao, String anotacoes, String placa, String unidade, String quantidade, String dataColheita){
        this.plantacao = plantacao;
        this.anotacoes = anotacoes;
        this.placa = placa;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.dataColheita = dataColheita;
    }

    public String getDataColheita() {
        return dataColheita;
    }

    public void setDataColheita(String dataColheita) {
        this.dataColheita = dataColheita;
    }

    public String getPlantacao() {
        return plantacao;
    }

    public void setPlantacao(String plantacao) {
        this.plantacao = plantacao;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

}
