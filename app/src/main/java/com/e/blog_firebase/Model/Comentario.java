package com.e.blog_firebase.Model;

public class Comentario {
    public Comentario(String comentario, String idUser, String imgUser, String nomeUser){
        this.comentario = comentario;
        this.idUser = idUser;
        this.imgUser = imgUser;
        this.nomeUser = nomeUser;
    }
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Object getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(Object timetamp) {
        this.timetamp = timetamp;
    }

    private String comentario;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public Comentario(){}
    private String idUser;
    private String imgUser;
    private Object timetamp;
    private String nomeUser;

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }
}
