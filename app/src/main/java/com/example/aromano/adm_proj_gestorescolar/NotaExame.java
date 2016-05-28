package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 28/05/2016.
 */
public class NotaExame {
    private int idnota;
    private int idaluno;
    private Exame exame;
    private float nota;

    public NotaExame(int idaluno, Exame exame, float nota) {
        this.idaluno = idaluno;
        this.exame = exame;
        this.nota = nota;
    }

    public NotaExame(int idnota, int idaluno, Exame exame, float nota) {
        this.idnota = idnota;
        this.idaluno = idaluno;
        this.exame = exame;
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

    public Exame getExame() {
        return exame;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }
}
