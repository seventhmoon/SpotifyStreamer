package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 04/07/2015.
 */
public class SearchArtistResponseModel {



    @SerializedName("artists")

    private Artists artists;


    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }
}
