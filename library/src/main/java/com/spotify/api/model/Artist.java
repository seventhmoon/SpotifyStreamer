package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fung on 05/07/2015.
 */
public class Artist{

    @SerializedName("external_urls")
    private ExternalUrls externalUrls;

    @SerializedName("followers")
    private Followers followers;

    @SerializedName("herf")
    private String link;
    @SerializedName("id")
    private String id;

    @SerializedName("images")
    private List<Image> images;

//    @SerializedName("album_type")
//    private AlbumType albumType;
//    @SerializedName("available_market")
//    private List<String> availableMarkets;


    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private int popularity;

    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;



    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
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

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }




}
