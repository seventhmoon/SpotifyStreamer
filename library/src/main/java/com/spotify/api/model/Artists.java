package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fung on 05/07/2015.
 */
public class Artists {
    @SerializedName("href")
    private String link;
    @SerializedName("items")
    private List<Artist> artists;
//    @SerializedName("genres")
//    private List<String> genres;

    @SerializedName("limit")
    private int limit;
    @SerializedName("next")
    private String next;
    @SerializedName("offset")
    private int offset;
    @SerializedName("pervious")
    private String pervious;
    @SerializedName("total")
    private int total;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPervious() {
        return pervious;
    }

    public void setPervious(String pervious) {
        this.pervious = pervious;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
