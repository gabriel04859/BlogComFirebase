package com.e.blog_firebase.Model;

public class Comentario {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String idUser;
    private String imgUser;
    private String userName;
    private Object timetamp;
}
