package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 31/05/2016.
 */
public class Evento {
    private int idevento;
    private Cadeira cadeira;
    private String tipo;
    private String datahora;
    private String descricao;
    private String sala;

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

    public Evento(int idevento, Cadeira cadeira, String tipo, String datahora, String descricao, String sala) {
        this.idevento = idevento;
        this.cadeira = cadeira;
        this.tipo = tipo;
        this.datahora = datahora;
        this.descricao = descricao;
        this.sala = sala;
    }

    public int getIdevento() {
        return idevento;
    }

    public void setIdevento(int idevento) {
        this.idevento = idevento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
}
