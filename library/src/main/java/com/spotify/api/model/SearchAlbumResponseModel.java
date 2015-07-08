package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fung on 04/07/2015.
 */
public class SearchAlbumResponseModel {



    @SerializedName("albums")

    private Albums Albums;

    public com.spotify.api.model.Albums getAlbums() {
        return Albums;
    }

    public void setAlbums(com.spotify.api.model.Albums albums) {
        Albums = albums;
    }
}
