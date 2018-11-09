package com.example.seamon.sikkacodingchallenge;

public class InstagramObject {
    private String imgUrl, linkUrl;
    private Integer likeCount;

    public InstagramObject() {

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer linkCount) {
        this.likeCount = linkCount;
    }
}
