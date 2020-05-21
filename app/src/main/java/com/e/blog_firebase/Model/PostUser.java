package com.e.blog_firebase.Model;

public class PostUser  {
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

    public PostUser(){}
    public PostUser(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
