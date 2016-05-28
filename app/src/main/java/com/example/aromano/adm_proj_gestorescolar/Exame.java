package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class Exame {
    private int idexame;
    private Cadeira cadeira;
    private String datahora;
    private String sala;
    private String descricao;

    public Exame(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public Exame(Cadeira cadeira, String datahora, String sala, String descricao) {
        this.cadeira = cadeira;
        this.datahora = datahora;
        this.sala = sala;
        this.descricao = descricao;
    }

    public Exame(int idexame, Cadeira cadeira, String datahora, String sala, String descricao) {
        this.idexame = idexame;
        this.cadeira = cadeira;
        this.datahora = datahora;
        this.sala = sala;
        this.descricao = descricao;
    }

    public int getIdexame() {
        return idexame;
    }

    public void setIdexame(int idexame) {
        this.idexame = idexame;
    }

    public Cadeira getCadeira() {
        return cadeira;
    }

    public void setCadeira(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
