package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class NotaTrabalho {
    private int idnota;
    private int idaluno;
    private Trabalho trabalho;
    private float nota;

    public NotaTrabalho(int idaluno, Trabalho trabalho, float nota) {
        this.idaluno = idaluno;
        this.trabalho = trabalho;
        this.nota = nota;
    }

    public NotaTrabalho(int idnota, int idaluno, Trabalho trabalho, float nota) {
        this.idnota = idnota;
        this.idaluno = idaluno;
        this.trabalho = trabalho;
        this.nota = nota;
    }

    public int getIdnota() {
        return idnota;
    }

    public void setIdnota(int idnota) {
        this.idnota = idnota;
    }

    public int getIdaluno() {
        return idaluno;
    }

    public void setIdaluno(int idaluno) {
        this.idaluno = idaluno;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = trabalho;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }
}
