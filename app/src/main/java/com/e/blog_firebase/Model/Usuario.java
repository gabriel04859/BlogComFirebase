package com.e.blog_firebase.Model;

public class Usuario {
    public Usuario(){}
    public Usuario(String nome, String email, String senha, String id) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String nome;
    private String email;
    private String senha;
    private String id;
}
