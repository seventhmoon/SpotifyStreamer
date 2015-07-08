package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 06/07/2015.
 */
public class ExternalIds {

    @SerializedName("isrc")
    private String isrc;

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }
}
