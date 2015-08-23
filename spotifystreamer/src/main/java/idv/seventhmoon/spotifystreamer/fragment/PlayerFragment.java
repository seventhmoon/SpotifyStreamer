package idv.seventhmoon.spotifystreamer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spotify.api.model.Album;
import com.spotify.api.model.Artist;
import com.spotify.api.model.Image;
import com.spotify.api.model.Track;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import idv.seventhmoon.spotifystreamer.MainApplication;
import idv.seventhmoon.spotifystreamer.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends DialogFragment {

    public static final String TAG = PlayerFragment.class.getSimpleName();

//    private static final String ARG_TRACK_ID = "trackId";

    private static final String ARG_TRACKS = "tracks";
    private static final String ARG_TRACK_POSITION = "trackPosition";

    //    private String mTrackId;
    private List<Track> mTracks;
    private int mTrackPosition;

    private MainApplication mApplication;

    private TextView mTextViewAlbumName;
    private TextView mTextViewAlbumArtist;
    private TextView mTextViewTrackName;
    private ImageView mImageViewAlbumCover;
    private TextView mTextViewDuration;

    private OnFragmentInteractionListener mListener;

    public PlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tracks        tracks
     * @param trackPosition trackPosition
     * @return A new instance of fragment PlayerFragment.
     */

    public static PlayerFragment newInstance(List<Track> tracks, int trackPosition) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRACKS, new Gson().toJson(tracks));
        args.putInt(ARG_TRACK_POSITION, trackPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (MainApplication) getActivity().getApplication();
        if (getArguments() != null) {
//            mTrackId = getArguments().getString(ARG_TRACK_ID);
            mTrackPosition = getArguments().getInt(ARG_TRACK_POSITION);
            Type listType = new TypeToken<ArrayList<Track>>() {
            }.getType();
            mTracks = new Gson().fromJson(getArguments().getString(ARG_TRACKS), listType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_player0, container, false);

        mTextViewAlbumArtist = (TextView) rootView.findViewById(R.id.text_album_artist);
        mTextViewAlbumName = (TextView) rootView.findViewById(R.id.text_album_name);
        mTextViewDuration = (TextView) rootView.findViewById(R.id.text_total_duration);
        mTextViewTrackName = (TextView) rootView.findViewById(R.id.text_track_name);
        mImageViewAlbumCover = (ImageView) rootView.findViewById(R.id.image_album_cover);


        loadTrack(mTrackPosition);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadTrack(int trackPosition) {

        Track track = mTracks.get(trackPosition);


        Album album = track.getAlbum();
        List<Artist> artists = track.getArtists();

        List<Image> images = album.getImages();
        if (!images.isEmpty()) {
            try {
                Glide.with(getActivity()).load(images.get(0).getUrl()).into(mImageViewAlbumCover);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

        mTextViewAlbumArtist.setText(getArtistsName(artists));
        mTextViewTrackName.setText(track.getName());
        mTextViewAlbumName.setText(album.getName());
        mTextViewDuration.setText(getFormattedDuration(track.getDuration()));


    }

    private String getArtistsName(List<Artist> artists) {
        StringBuilder sb = new StringBuilder();
        for (Artist artist : artists) {
            sb.append(artist.getName() + ", ");
        }
        String s = sb.toString();
        return s.substring(0, s.length() - 2);
    }

    private String getFormattedDuration(long durationInMs) {
        Date date = new Date(durationInMs);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return formatter.format(date);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {


    }

}
