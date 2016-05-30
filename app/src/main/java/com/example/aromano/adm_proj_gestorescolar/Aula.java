package com.example.aromano.adm_proj_gestorescolar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Aula implements Parcelable {
    private int idaula;
    private Cadeira cadeira;
    private int diaSemana; // 0-6, 0 is monday
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



    // parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idaula);
        dest.writeParcelable(cadeira, flags);
        dest.writeInt(diaSemana);
        dest.writeString(horaentrada);
        dest.writeString(horasaida);
        dest.writeString(sala);
    }

    private Aula(Parcel source) {
        this.idaula = source.readInt();
        this.cadeira = source.readParcelable(Cadeira.class.getClassLoader());
        this.diaSemana = source.readInt();
        this.horaentrada = source.readString();
        this.horasaida = source.readString();
        this.sala = source.readString();
    }

    public static final Parcelable.Creator<Aula> CREATOR = new Parcelable.Creator<Aula>() {
        @Override
        public Aula createFromParcel(Parcel source) {
            return new Aula(source);
        }

        @Override
        public Aula[] newArray(int size) {
            return new Aula[size];
        }
    };
}
