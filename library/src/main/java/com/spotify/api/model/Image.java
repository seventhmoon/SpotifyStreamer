package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 04/07/2015.
 */
public class Image implements Comparable<Image> {
    @SerializedName("height")
    private int height;
    @SerializedName("width")
    private int width;
    @SerializedName("url")
    private String url;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString() {
        return String.valueOf(width) + ", " + String.valueOf(height) + ", " + url;
    }

    @Override
    public int compareTo(Image another) {
        if (this.getWidth() == another.getWidth()) {
            if (this.getHeight() == another.getHeight()) {
                return this.url.compareTo(another.getUrl());
            } else {
                return this.getHeight() - another.getHeight();
            }
        } else {
            return this.getWidth() - another.getWidth();
        }

    }
}
