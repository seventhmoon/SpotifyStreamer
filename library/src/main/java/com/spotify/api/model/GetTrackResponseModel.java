package com.spotify.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fung on 09/07/2015.
 */
public class GetTrackResponseModel {

    @SerializedName("album")
    private Album album;
    @SerializedName("artists")
    private List<Artist> artists;
    @SerializedName("available_markets")
    private List<String> availableMarkets;
    @SerializedName("disk_number")
    private int diskNumber;
    @SerializedName("duration_ms")
    private long duration;
    @SerializedName("explicit")
    private boolean explicit;
    @SerializedName("external_ids")
    private ExternalIds externalIds;
    @SerializedName("external_urls")
    private ExternalUrls externalUrls;
    @SerializedName("href")
    private String link;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("popularity")
    private int popularity;
    @SerializedName("preview_url")
    private String previewUrl;
    @SerializedName("track_number")
    private int trackNumber;
    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
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
