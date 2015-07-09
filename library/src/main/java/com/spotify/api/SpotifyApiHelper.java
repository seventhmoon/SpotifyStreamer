package com.spotify.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.spotify.api.model.GetArtistsTopTracksResponseModel;
import com.spotify.api.model.GetTrackResponseModel;
import com.spotify.api.model.SearchAlbumResponseModel;
import com.spotify.api.model.SearchArtistResponseModel;
import com.spotify.api.network.GsonObjectRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fung on 04/07/2015.
 */
public class SpotifyApiHelper {

    public static final String SEARCH_URL = "https://api.spotify.com/v1/search";
    public static final String GET_TOP_TRACKS_URL = "https://api.spotify.com/v1/artists/%1$s/top-tracks";
    public static final String GET_TRACK_URL = "https://api.spotify.com/v1/tracks/%1$s";

    private RequestQueue mRequestQueue;
    private String mDefaultCountryCode;
    public SpotifyApiHelper(RequestQueue requestQueue){
        mRequestQueue = requestQueue;
        mDefaultCountryCode = Locale.getDefault().getCountry();
    }

    public Request<SearchArtistResponseModel> searchArtist(String keyword, int limit, Response.Listener<SearchArtistResponseModel> listener, Response.ErrorListener errorListener){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("type", "artist");
        params.put("limit", Integer.toString(limit));
        params.put("q", keyword);

        String url = SEARCH_URL + "?" + toUrlParams(params);

        GsonObjectRequest gsonReq = new GsonObjectRequest(Request.Method.GET,
                url, SearchArtistResponseModel.class, null, listener, errorListener);

        // Adding request to request queue
        return mRequestQueue.add(gsonReq);

    }



    public Request searchArtistsTopTracks(String artistId,  Response.Listener<GetArtistsTopTracksResponseModel> listener, Response.ErrorListener errorListener){
        return searchArtistsTopTracks(artistId, mDefaultCountryCode, listener, errorListener);
    }

    public Request requestGetTrack(String trackId, Response.Listener<GetTrackResponseModel> listener, Response.ErrorListener errorListener){
        String url = String.format(GET_TRACK_URL,trackId);

        GsonObjectRequest gsonReq = new GsonObjectRequest(Request.Method.GET,
                url, GetTrackResponseModel.class, null, listener, errorListener);

        // Adding request to request queue
        return mRequestQueue.add(gsonReq);

    }

    public Request searchArtistsTopTracks(String artistId, String countryCode, Response.Listener<GetArtistsTopTracksResponseModel> listener, Response.ErrorListener errorListener){
        TreeMap<String, String> params = new TreeMap<>();

        params.put("country", countryCode);

        String url = String.format(GET_TOP_TRACKS_URL,artistId) + "?" + toUrlParams(params);

        GsonObjectRequest gsonReq = new GsonObjectRequest(Request.Method.GET,
                url, GetArtistsTopTracksResponseModel.class, null, listener, errorListener);

        // Adding request to request queue
        return mRequestQueue.add(gsonReq);

    }

    public Request<SearchAlbumResponseModel> searchAlbum(String keyword, int limit, Response.Listener<SearchAlbumResponseModel> listener, Response.ErrorListener errorListener){
        TreeMap<String, String> params = new TreeMap<>();
        params.put("type", "album");
        params.put("limit", Integer.toString(limit));
        params.put("q", keyword);

        String url = SEARCH_URL + "?" + toUrlParams(params);

        GsonObjectRequest gsonReq = new GsonObjectRequest(Request.Method.GET,
                url, SearchAlbumResponseModel.class, null, listener, errorListener);

        // Adding request to request queue
        return mRequestQueue.add(gsonReq);

    }


    private static String toUrlParams(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            try {
                sb.append("&" + key + "=" + URLEncoder.encode(params.get(key), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString().substring(1);
    }

}
