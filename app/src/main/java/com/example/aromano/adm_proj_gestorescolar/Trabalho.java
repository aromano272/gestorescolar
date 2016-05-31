package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class Trabalho extends Evento {
    private int idtrabalho;

    public Trabalho(Cadeira cadeira) {
        super(cadeira);
    }

    public Trabalho(Cadeira cadeira, String datahora, String descricao) {
        super(cadeira, datahora, descricao);
    }

    public Trabalho(int idtrabalho, Cadeira cadeira, String datahora, String descricao) {
        super(cadeira, datahora, descricao);
        this.idtrabalho = idtrabalho;
    }

    public int getIdtrabalho() {
        return idtrabalho;
    }

    public void setIdtrabalho(int idtrabalho) {
        this.idtrabalho = idtrabalho;
    }
}
