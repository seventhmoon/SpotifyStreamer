package idv.seventhmoon.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spotify.api.model.Artist;
import com.spotify.api.model.Track;

import java.util.List;

/**
 * Created by fung.lam on 20/08/2015.
 */
public class PlayerSession implements Parcelable {


    private static final String TAG = PlayerSession.class.getSimpleName();
//    private Artist mArtist;
    private List<Artist> mArtists;
    private List<Track> mTracks;
    private int mStartPositon;
    private int mCurrentPosition;
    private String mSessionToken;


    // Constructor
    public PlayerSession(List<Track> tracks, int startingPosition) {
//        mArtist = artist;
        mArtists = tracks.get(startingPosition).getArtists();
        mTracks = tracks;
        mCurrentPosition = mStartPositon = startingPosition;
        mSessionToken = tracks.get(getCurrentPosition()).getId();
//        mSessionToken = (mA == null ? "null" : artist.getId()) + "_" + startingPosition;
    }

    public static final Creator<PlayerSession> CREATOR = new Creator<PlayerSession>() {
        @Override
        public PlayerSession createFromParcel(Parcel in) {
            return new PlayerSession(in);
        }

        @Override
        public PlayerSession[] newArray(int size) {
            return new PlayerSession[size];
        }
    };

    // Getters
    public int getStartPosition() {
        return mStartPositon;
    }



    public String getSessionToken() {
        return mSessionToken;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public Track getTrackAt(int position) {
        if (mTracks == null || mTracks.isEmpty()) {
            return null;
        } else if (position < 0 || position >= mTracks.size()) {
            return null;
        } else {
            return mTracks.get(position);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public int getPlaylistSize() {
        return (mTracks != null) ? mTracks.size() : 0;
    }

    public Track getCurrentTrack() {
        return mTracks.get(mCurrentPosition);
    }

    private int getPreviousPosition() {
        int prev = mCurrentPosition - 1;

        return (prev < 0) ? mTracks.size() - 1 : prev;
    }

    private int getNextPosition() {
        int next = mCurrentPosition + 1;

        return (next >= mTracks.size()) ? 0 : next;
    }

    public Track getPreviousTrack() {
        mCurrentPosition = getPreviousPosition();
        return getTrackAt(mCurrentPosition);
    }

    public Track getNextTrack() {
        mCurrentPosition = getNextPosition();
        return getTrackAt(mCurrentPosition);
    }

    //Setters
    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerSession) {
            return mSessionToken.equals(((PlayerSession) o).getSessionToken());
        }

        return super.equals(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        //running out of time, shouldn't use json string
        dest.writeString(new Gson().toJson(mArtists));
        dest.writeString(new Gson().toJson(mTracks));
        dest.writeInt(mStartPositon);
        dest.writeInt(mCurrentPosition);
        dest.writeString(mSessionToken);
    }

    protected PlayerSession(Parcel in) {
        String artistJson = in.readString();
        String tracksJson = in.readString();

        Gson gson = new Gson();

        mArtists = gson.fromJson(artistJson, new TypeToken<List<Artist>>() {
        }.getType());
        mTracks = gson.fromJson(tracksJson, new TypeToken<List<Track>>() {
        }.getType());

        mStartPositon = in.readInt();
        mCurrentPosition = in.readInt();
        mSessionToken = in.readString();

//        mArtist = in.readParcelable(ParcableArtist.class.getClassLoader());
//        this.playlist = in.createTypedArrayList(ParcableTrack.CREATOR);
//        this.startingPosition = in.readInt();
//        this.currentPosition = in.readInt();
//        this.sessionToken = in.readString();
    }

//    public static final Parcelable.Creator<PlayerSession> CREATOR = new Parcelable.Creator<PlayerSession>() {
//        public PlayerSession createFromParcel(Parcel source) {
//            return new PlayerSession(source);
//        }
//
//        public PlayerSession[] newArray(int size) {
//            return new PlayerSession[size];
//        }
//    };
}
