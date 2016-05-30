package com.example.aromano.adm_proj_gestorescolar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.GregorianCalendar;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Aluno implements Parcelable {
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


    // parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idaluno);
        dest.writeString(username);
        dest.writeString(nome);
        dest.writeString(apelido);
        dest.writeString(datanasc);
    }

    private Aluno(Parcel source) {
        this.idaluno = source.readInt();
        this.username = source.readString();
        this.nome = source.readString();
        this.apelido = source.readString();
        this.datanasc = source.readString();
    }

    public static final Parcelable.Creator<Aluno> CREATOR = new Parcelable.Creator<Aluno>() {
        @Override
        public Aluno createFromParcel(Parcel source) {
            return new Aluno(source);
        }

        @Override
        public Aluno[] newArray(int size) {
            return new Aluno[size];
        }
    };
}
