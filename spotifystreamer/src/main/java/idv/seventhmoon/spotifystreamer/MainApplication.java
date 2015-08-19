package idv.seventhmoon.spotifystreamer;
/**
 * Created by fung.lam on 27/11/2014.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.spotify.api.model.Track;

import java.util.List;

import idv.seventhmoon.spotifystreamer.activity.SettingsActivity;

public class MainApplication extends Application {



    public static final String TAG = MainApplication.class
            .getSimpleName();
    private final String DEFAULT_LOCALE = "HK";
    private final boolean DEFAULT_NOTIFICATION_ENABLED = true;

    private static MainApplication mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public String getCountryCode(){
//        return Locale.getDefault().getCountry();
        return getLocalePref();
    }

    private String getLocalePref(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String localePref = sharedPref.getString(SettingsActivity.KEY_PREF_LOCALE, DEFAULT_LOCALE);
        return localePref;
    }

    private boolean getNotificationPref(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notiPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION, DEFAULT_NOTIFICATION_ENABLED);
        return notiPref;
    }

    private void saveTracksInfo(List<Track>tracks, int trackPosition){


    }
}