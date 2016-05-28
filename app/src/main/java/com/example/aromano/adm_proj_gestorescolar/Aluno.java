package com.example.aromano.adm_proj_gestorescolar;

import java.util.GregorianCalendar;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Aluno {
    private int idaluno;
    private String username;
    private String nome;
    private String apelido;
    private String datanasc;

    public Aluno(String username, String nome, String apelido, String datanasc) {
        this.username = username;
        this.nome = nome;
        this.apelido = apelido;
        this.datanasc = datanasc;
    }

    public Aluno(int idaluno, String username, String nome, String apelido, String datanasc) {
        this.idaluno = idaluno;
        this.username = username;
        this.nome = nome;
        this.apelido = apelido;
        this.datanasc = datanasc;
    }

    public int getIdaluno() {
        return idaluno;
    }

    public void setIdaluno(int idaluno) {
        this.idaluno = idaluno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(String datanasc) {
        this.datanasc = datanasc;
    }
}
