package com.example.tutoringro;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class ManagerProfesori implements Serializable{
    public static ManagerProfesori profesor;
    private String nume, username, email, telefon, parola, domiciliu, categorii, subcategorii, materii,judet, distanta, pret, key;


    public ManagerProfesori() {
    }

    public ManagerProfesori(String nume, String username, String email, String telefon, String parola, String domiciliu, String categorii, String subcategorii, String judet, String materii, String distanta, String pret) {

        this.nume = nume;
        this.username = username;
        this.email = email;
        this.telefon = telefon;
        this.parola = parola;
        this.domiciliu = domiciliu;
        this.categorii = categorii;
        this.subcategorii = subcategorii;
        this.materii = materii;
        this.distanta = distanta;
        this.pret = pret;
        this.judet = judet;
    }

    public static ManagerProfesori getProfesor() {
        if (profesor == null) {
            profesor = new ManagerProfesori();
        }
        return profesor;

    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public static void setProfesor(ManagerProfesori profesor) {
        ManagerProfesori.profesor = profesor;
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

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public void setSubcategorii(String subcategorii) {
        this.subcategorii = subcategorii;
    }

    public String getDistanta() {
        return distanta;
    }

    public void setDistanta(String distanta) {
        this.distanta = distanta;
    }

    public String getMaterii() {
        return materii;
    }

    public void setMaterii(String materii) {
        this.materii = materii;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;

    }
}

