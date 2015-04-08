package com.epsi.hugo.rssreaderhg2.pck_classes;

/**
 * Created by hugo on 07/04/15.
 */
public class PostData {

    public String postTitle;
    public String postURL;
    public String postDescripion;

    //TODO : Date + Images
    public String postImgUrl;
    public String postDate;

    public PostData(String title, String description, String link) {
        this.postTitle = title;
        this.postDescripion = description;
        this.postURL = link;
    }

    public String getPostDescripion() {
        return postDescripion;
    }

    public void setPostDescripion(String postDescripion) {
        this.postDescripion = postDescripion;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}

