package com.autocana.listas;

public class UserListInsumos {

    String nomeInsumo, descricao, concentracao, unidade;

    public UserListInsumos(){

    }

    public UserListInsumos(String nomeInsumo, String descricao, String unidade, String concentracao) {
        this.nomeInsumo = nomeInsumo;
        this.descricao = descricao;
        this.unidade = unidade;
        this.concentracao = concentracao;

    }

    public String getConcentracao() {
        return concentracao;
    }

    public void setConcentracao(String concentracao) {
        this.concentracao = concentracao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getNomeInsumo() {
        return nomeInsumo;
    }

    public void setNomeInsumo(String nomeInsumo) {
        this.nomeInsumo = nomeInsumo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
