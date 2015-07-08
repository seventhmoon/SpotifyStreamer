package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fung on 04/07/2015.
 */
public class Album {
    public enum AlbumType {
        @SerializedName("album")
        ALBUM,
        @SerializedName("single")
        SINGLE,
        @SerializedName("compilation")
        COMPILATION
    }


    @SerializedName("album_type")
    private AlbumType albumType;
    @SerializedName("available_market")
    private List<String> availableMarkets;
    @SerializedName("herf")
    private String link;
    @SerializedName("id")
    private String id;
    @SerializedName("images")
    private List<Image> images;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;

    @Override
    public String toString(){
        return this.id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public AlbumType getAlbumType() {
        return albumType;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public Image getImage()


}
