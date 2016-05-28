package com.example.aromano.adm_proj_gestorescolar;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Cadeira {
    private int idcadeira;
    private String name;
    private String abbr;
    private int creditos;
    private ArrayList<Aula> aulas;

    public Cadeira(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
    }

    public Cadeira(String name, int creditos, ArrayList<Aula> aulas) {
        this.aulas = aulas;
        this.name = name;
        this.creditos = creditos;
    }

    public Cadeira(int idcadeira, String name, int creditos, ArrayList<Aula> aulas) {
        this.idcadeira = idcadeira;
        this.name = name;
        this.creditos = creditos;
        this.aulas = aulas;
    }

    public ArrayList<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(ArrayList<Aula> aulas) {
        this.aulas = aulas;
    }

    public int getIdcadeira() {
        return idcadeira;
    }

    public void setIdcadeira(int idcadeira) {
        this.idcadeira = idcadeira;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
}
