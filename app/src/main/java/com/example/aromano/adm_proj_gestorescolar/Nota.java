package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 01/06/2016.
 */
public class Nota {
    private int idnota;
    private Aluno aluno;
    private Evento evento;
    private float nota;

    public Nota(Aluno aluno, Evento evento, float nota) {
        this.aluno = aluno;
        this.evento = evento;
        this.nota = nota;
    }

    public Nota(int idnota, Aluno aluno, Evento evento, float nota) {
        this.idnota = idnota;
        this.aluno = aluno;
        this.evento = evento;
        this.nota = nota;
    }

    public int getIdnota() {
        return idnota;
    }

    public void setIdnota(int idnota) {
        this.idnota = idnota;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }
}
