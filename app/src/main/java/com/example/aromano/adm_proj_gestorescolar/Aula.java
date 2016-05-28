package com.example.aromano.adm_proj_gestorescolar;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Aula {
    private int idaula;
    private Cadeira cadeira;
    private int diaSemana;
    private String horaentrada;
    private String horasaida;
    private String sala;

    public Aula(Cadeira cadeira, int diaSemana, String horaentrada, String horasaida, String sala) {
        this.sala = sala;
        this.cadeira = cadeira;
        this.horaentrada = horaentrada;
        this.horasaida = horasaida;
    }

    public Aula(int idaula, Cadeira cadeira, int diaSemana, String horaentrada, String horasaida, String sala) {
        this.idaula = idaula;
        this.cadeira = cadeira;
        this.horaentrada = horaentrada;
        this.horasaida = horasaida;
        this.sala = sala;
    }

    public int getIdaula() {
        return idaula;
    }

    public void setIdaula(int idaula) {
        this.idaula = idaula;
    }

    public Cadeira getCadeira() {
        return cadeira;
    }

    public void setCadeira(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public String getHoraentrada() {
        return horaentrada;
    }

    public void setHoraentrada(String horaentrada) {
        this.horaentrada = horaentrada;
    }

    public String getHorasaida() {
        return horasaida;
    }

    public void setHorasaida(String horasaida) {
        this.horasaida = horasaida;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }
}
