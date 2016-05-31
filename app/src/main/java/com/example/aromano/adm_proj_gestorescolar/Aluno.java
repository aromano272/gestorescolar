package com.example.aromano.adm_proj_gestorescolar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aRomano on 27/05/2016.
 */
public class Aluno implements Parcelable {
    private int idaluno;
    private String username;
    private String nome;
    private String email;

    public Aluno(String username, String nome, String email) {
        this.username = username;
        this.nome = nome;
        this.email = email;
    }

    public Aluno(int idaluno, String username, String nome, String email) {
        this.idaluno = idaluno;
        this.username = username;
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        dest.writeString(email);
    }

    private Aluno(Parcel source) {
        this.idaluno = source.readInt();
        this.username = source.readString();
        this.nome = source.readString();
        this.email = source.readString();
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
