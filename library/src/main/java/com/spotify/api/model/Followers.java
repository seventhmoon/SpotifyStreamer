package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 05/07/2015.
 */
public class Followers {

    @SerializedName("href")
    private String link;

    @SerializedName("total")
    private long total;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
