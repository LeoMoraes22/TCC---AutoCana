package com.autocana.listas;

public class UserListProcPlantacoes {

    String nome, area, cidade, estado, descricao;

    public UserListProcPlantacoes(){

    }

    public UserListProcPlantacoes(String nome, String cidade, String estado, String area, String descricao){
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
        this.area = area;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
