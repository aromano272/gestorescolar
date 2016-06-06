package com.example.aromano.adm_proj_gestorescolar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aRomano on 01/06/2016.
 */
public class Nota implements Parcelable {
    private int idnota;
    private Aluno aluno;
    private Cadeira cadeira;
    private float nota;

    public Nota(Aluno aluno, Cadeira cadeira, float nota) {
        this.aluno = aluno;
        this.cadeira = cadeira;
        this.nota = nota;
    }

    public Nota(int idnota, Aluno aluno, Cadeira cadeira, float nota) {
        this.idnota = idnota;
        this.aluno = aluno;
        this.cadeira = cadeira;
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

    public Cadeira getCadeira() {
        return cadeira;
    }

    public void setCadeira(Cadeira cadeira) {
        this.cadeira = cadeira;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }


    // parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idnota);
        dest.writeParcelable(aluno, flags);
        dest.writeParcelable(cadeira, flags);
        dest.writeFloat(nota);
    }

    private Nota(Parcel source) {
        this.idnota = source.readInt();
        this.aluno = source.readParcelable(Aluno.class.getClassLoader());
        this.cadeira = source.readParcelable(Cadeira.class.getClassLoader());
        this.nota = source.readFloat();
    }

    public static final Parcelable.Creator<Nota> CREATOR = new Parcelable.Creator<Nota>() {
        @Override
        public Nota createFromParcel(Parcel source) {
            return new Nota(source);
        }

        @Override
        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };
}
