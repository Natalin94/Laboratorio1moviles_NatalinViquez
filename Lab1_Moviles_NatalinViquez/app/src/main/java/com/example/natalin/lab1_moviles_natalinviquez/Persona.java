package com.example.natalin.lab1_moviles_natalinviquez;

import android.net.Uri;

public class Persona {
    private String name;
    private String profesion;
    private String sexo;
    private String image;

    public Persona(String name, String profesion, String sexo, String image) {
        this.name = name;
        this.profesion = profesion;
        this.sexo = sexo;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getImage() {  return image; }

    public void setImage(String image) {
        this.image = image;
    }
}


