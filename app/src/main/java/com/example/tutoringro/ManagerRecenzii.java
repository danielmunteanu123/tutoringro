package com.example.tutoringro;

public class ManagerRecenzii {
    String descriere, recomandare, nume_student;
    float nota;

    public ManagerRecenzii()
    {

    }

    public ManagerRecenzii(String descriere, String recomandare, float nota, String nume_student) {
        this.descriere = descriere;
        this.recomandare = recomandare;
        this.nota = nota;
        this.nume_student = nume_student;
    }

    public String getNume_student() {
        return nume_student;
    }

    public void setNume_student(String nume_student) {
        this.nume_student = nume_student;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }
}
