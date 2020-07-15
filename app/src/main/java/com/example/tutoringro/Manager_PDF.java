package com.example.tutoringro;

public class Manager_PDF {

    public String name;
    public String url;
    public String username;

    public Manager_PDF(){

    }

    public Manager_PDF(String name, String url,String username) {
        this.name = name;
        this.url = url;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
