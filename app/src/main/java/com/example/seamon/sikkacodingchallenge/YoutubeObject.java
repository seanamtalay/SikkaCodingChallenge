package com.example.seamon.sikkacodingchallenge;

public class YoutubeObject {
    private String title;
    private String description;
    private Integer viewsCount;
    private String channel;
    private String thumbnailUrl;
    private String id;

    public YoutubeObject(String title, String description, int viewsCount, String channel, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.viewsCount = viewsCount;
        this.channel = channel;
        this.thumbnailUrl = thumbnailUrl;
    }

    public YoutubeObject(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
