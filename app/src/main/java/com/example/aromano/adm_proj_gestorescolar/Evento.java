package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 31/05/2016.
 */
public class Evento {
    private Cadeira cadeira;
    private String datahora;
    private String descricao;

    public String getDatahora() {
        return datahora;
    }

    public void setDatahora(String datahora) {
        this.datahora = datahora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cadeira getCadeira() {
        return cadeira;
    }

    public void setCadeira(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public Evento(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public Evento(Cadeira cadeira, String datahora, String descricao) {
        this.cadeira = cadeira;
        this.datahora = datahora;
        this.descricao = descricao;
    }
}
