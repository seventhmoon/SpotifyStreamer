package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fung on 04/07/2015.
 */
public class GetArtistsTopTracksResponseModel {


    @SerializedName("tracks")
    private List<Track> tracks;


    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }


}
