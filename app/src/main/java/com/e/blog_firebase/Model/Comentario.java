package com.e.blog_firebase.Model;

public class Comentario {
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(Object timetamp) {
        this.timetamp = timetamp;
    }

    private String comentario, uid, uimg, uname;
    private Object timetamp;
}
