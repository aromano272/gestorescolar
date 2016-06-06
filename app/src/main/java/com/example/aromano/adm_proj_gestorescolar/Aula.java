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
    private int horaentrada;
    private String sala;

    public Aula(Cadeira cadeira, int diaSemana, int horaentrada, String sala) {
        this.sala = sala;
        this.cadeira = cadeira;
        this.diaSemana = diaSemana;
        this.horaentrada = horaentrada;
    }

    public Aula(int idaula, Cadeira cadeira, int diaSemana, int horaentrada, String sala) {
        this.idaula = idaula;
        this.cadeira = cadeira;
        this.diaSemana = diaSemana;
        this.horaentrada = horaentrada;
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

    public int getHoraentrada() {
        return horaentrada;
    }

    public void setHoraentrada(int horaentrada) {
        this.horaentrada = horaentrada;
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
        dest.writeInt(horaentrada);
        dest.writeString(sala);
    }

    private Aula(Parcel source) {
        this.idaula = source.readInt();
        this.cadeira = source.readParcelable(Cadeira.class.getClassLoader());
        this.diaSemana = source.readInt();
        this.horaentrada = source.readInt();
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
