package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class Exame extends Evento {
    private int idexame;
    private String sala;

    public Exame(Cadeira cadeira) {
        super(cadeira);
    }

    public Exame(Cadeira cadeira, String datahora, String sala, String descricao) {
        super(cadeira, datahora, descricao);
        this.sala = sala;
    }

    public Exame(int idexame, Cadeira cadeira, String datahora, String sala, String descricao) {
        super(cadeira, datahora, descricao);
        this.idexame = idexame;
        this.sala = sala;
    }

    public int getIdexame() {
        return idexame;
    }

    public void setIdexame(int idexame) {
        this.idexame = idexame;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
}
