package com.autocana.listas;

public class UserListEstimativas {

    String nomePlantacao, nomeInsumo, quantInsumo, quantProducao, hectares, alqueires;

    public UserListEstimativas(){

    }

    public UserListEstimativas(String nomePlantacao, String nomeInsumo, String quantInsumo, String quantProducao, String hectares, String alqueires ){
        this.nomePlantacao = nomePlantacao;
        this.nomeInsumo = nomeInsumo;
        this.quantInsumo = quantInsumo;
        this.quantProducao = quantProducao;
        this.hectares = hectares;
        this.alqueires = alqueires;
    }

    public String getNomePlantacao() {
        return nomePlantacao;
    }

    public void setNomePlantacao(String nomePlantacao) {
        this.nomePlantacao = nomePlantacao;
    }

    public String getNomeInsumo() {
        return nomeInsumo;
    }

    public void setNomeInsumo(String nomeInsumo) {
        this.nomeInsumo = nomeInsumo;
    }

    public String getQuantInsumo() {
        return quantInsumo;
    }

    public void setQuantInsumo(String quantInsumo) {
        this.quantInsumo = quantInsumo;
    }

    public String getQuantProducao() {
        return quantProducao;
    }

    public void setQuantProducao(String quantProducao) {
        this.quantProducao = quantProducao;
    }

    public String getHectares() {
        return hectares;
    }

    public void setHectares(String hectares) {
        this.hectares = hectares;
    }

    public String getAlqueires() {
        return alqueires;
    }

    public void setAlqueires(String alqueires) {
        this.alqueires = alqueires;
    }
}
