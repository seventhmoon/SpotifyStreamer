package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 05/07/2015.
 */
public class ExternalUrls {

    @SerializedName("spotify")
    private String spotifyUrl;


    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }
}
