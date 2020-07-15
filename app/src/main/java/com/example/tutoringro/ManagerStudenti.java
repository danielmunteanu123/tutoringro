package com.example.tutoringro;

import java.io.Serializable;

public class ManagerStudenti implements Serializable {
    private static ManagerStudenti student;
    private String nume;
    private String prenume;
    private String email;
    private String parola;
    private String telefon;
    private String username;
    private String domiciliu;
    private String categorii;
    private String subcategorii;
    private String materii;
    private String judet;
    private String varsta;
    private String key;


    public ManagerStudenti() {
    }

    public ManagerStudenti(String nume, String prenume, String email, String username, String parola, String telefon, String judet, String domiciliu, String categorii, String subcategorii, String materii, String varsta) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.parola = parola;
        this.username = username;
        this.telefon = telefon;
        this.domiciliu = domiciliu;
        this.categorii = categorii;
        this.subcategorii = subcategorii;
        this.materii = materii;
        this.varsta = varsta;
        this.judet = judet;
    }


    public static ManagerStudenti getStudent() {
        if (student == null) {
            student = new ManagerStudenti();
        }
        return student;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVarsta() {
        return varsta;
    }

    public void setVarsta(String varsta) {
        this.varsta = varsta;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDomiciliu() {
        return domiciliu;
    }

    public void setDomiciliu(String domiciliu) {
        this.domiciliu = domiciliu;
    }

    public String getCategorii() {
        return categorii;
    }

    public void setCategorii(String categorii) {
        this.categorii = categorii;
    }

    public String getSubcategorii() {
        return subcategorii;
    }

    public void setSubcategorii(String subcategorii) {
        this.subcategorii = subcategorii;
    }

    public String getMaterii() {
        return materii;
    }

    public void setMaterii(String materii) {
        this.materii = materii;
    }

    public static void setStudent(ManagerStudenti student) {
        ManagerStudenti.student = student;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
