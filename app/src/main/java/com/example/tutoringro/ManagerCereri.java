package com.example.tutoringro;

import java.io.Serializable;

public class ManagerCereri implements Serializable {
    public static ManagerCereri cerere;
    private String materie, data, loc, judet, tip, username_student, key, username_profesor, ora;


    public ManagerCereri(){
    }


    public ManagerCereri(String materie, String data,String ora, String loc, String tip, String username_student,  String username_profesor, String judet) {
        this.materie = materie;
        this.data = data;
        this.ora = ora;
        this.loc = loc;
        this.tip = tip;
        this.judet = judet;
        this.username_student = username_student;
        this.username_profesor = username_profesor;
    }

    public String getMaterie() {
        return materie;
    }

    public static ManagerCereri getCerere() {
        if (cerere == null) {
            cerere = new ManagerCereri();
        }
        return cerere;

    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getUsername_student() {
        return username_student;
    }

    public void setUsername_student(String username_student) {
        this.username_student = username_student;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUsername_profesor() {
        return username_profesor;
    }

    public void setUsername_profesor(String username_profesor) {
        this.username_profesor = username_profesor;
    }

    public static void setCerere(ManagerCereri cerere) {
        ManagerCereri.cerere = cerere;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
