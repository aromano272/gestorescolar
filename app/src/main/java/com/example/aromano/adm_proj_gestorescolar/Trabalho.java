package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class Trabalho {
    private int idtrabalho;
    private Cadeira cadeira;
    private String dataentrega;
    private String descricao;

    public Trabalho(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public Trabalho(Cadeira cadeira, String dataentrega, String descricao) {
        this.cadeira = cadeira;
        this.dataentrega = dataentrega;
        this.descricao = descricao;
    }

    public Trabalho(int idtrabalho, Cadeira cadeira, String dataentrega, String descricao) {
        this.idtrabalho = idtrabalho;
        this.cadeira = cadeira;
        this.dataentrega = dataentrega;
        this.descricao = descricao;
    }

    public int getIdtrabalho() {
        return idtrabalho;
    }

    public void setIdtrabalho(int idtrabalho) {
        this.idtrabalho = idtrabalho;
    }

    public Cadeira getCadeira() {
        return cadeira;
    }

    public void setCadeira(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public String getDataentrega() {
        return dataentrega;
    }

    public void setDataentrega(String dataentrega) {
        this.dataentrega = dataentrega;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
