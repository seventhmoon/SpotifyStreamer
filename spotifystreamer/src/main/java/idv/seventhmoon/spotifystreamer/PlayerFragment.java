package idv.seventhmoon.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spotify.api.SpotifyApiHelper;
import com.spotify.api.model.Album;
import com.spotify.api.model.Artist;
import com.spotify.api.model.GetTrackResponseModel;
import com.spotify.api.model.Image;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends DialogFragment{

    public static final String TAG = PlayerFragment.class.getSimpleName();

    private static final String ARG_TRACK_ID = "trackId";
    private String mTrackId;
    private MainApplication mApplication;

    private TextView mTextViewAlbumName;
    private TextView mTextViewAlbumArtist;
    private TextView mTextViewTrackName;
    private ImageView mImageViewAlbumCover;
    private TextView mTextViewDuration;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trackId trackId.
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String trackId) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRACK_ID, trackId);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (MainApplication) getActivity().getApplication();
        if (getArguments() != null) {
            mTrackId = getArguments().getString(ARG_TRACK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_player, container, false);

        mTextViewAlbumArtist = (TextView) rootView.findViewById(R.id.text_album_artist);
        mTextViewAlbumName = (TextView) rootView.findViewById(R.id.text_album_name);
        mTextViewDuration = (TextView) rootView.findViewById(R.id.text_total_duration);
        mTextViewTrackName = (TextView) rootView.findViewById(R.id.text_track_name);
        mImageViewAlbumCover = (ImageView) rootView.findViewById(R.id.image_album_cover);


        loadTrack(mTrackId);

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

    private void loadTrack(String trackId){


        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper(mApplication.getRequestQueue());
        spotifyApiHelper.requestGetTrack(trackId, new Response.Listener<GetTrackResponseModel>() {
            @Override
            public void onResponse(GetTrackResponseModel response) {

                Album album = response.getAlbum();
                List<Artist> artists = response.getArtists();

                List<Image> images = album.getImages();
                if (!images.isEmpty()){
                    Picasso.with(getActivity()).load(images.get(0).getUrl()).into(mImageViewAlbumCover);
                }

                mTextViewAlbumArtist.setText(getArtistsName(artists));
                mTextViewTrackName.setText(response.getName());
                mTextViewAlbumName.setText(album.getName());
                mTextViewDuration.setText(getFormattedDuration(response.getDuration()));

//                List<Artist> artists = response.getArtists().getArtists();
//                setActionBarTitle(getString(R.string.text_top_10_tracks), mArtist);
//                List<Track> tracks = response.getTracks();
//                if (tracks.isEmpty()) {
                    //show no result page
//                    displayNoResult();
//                } else {

//                    mAdapter = new TopTracksAdapter(mApplication, tracks, mListener);
//                    Log.d(TAG, tracks.toString());
//                    mRecyclerView.setAdapter(mAdapter);

//                    displayResult();

//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

    }


    private String getArtistsName(List<Artist> artists){
        StringBuffer sb = new StringBuffer();
        for (Artist artist : artists){
            sb.append(artist.getName() + ", ");
        }
        String s = sb.toString();
        return s.substring(0, s.length()-2);
    }

    private String getFormattedDuration(long durationInMs){
        Date date = new Date(durationInMs);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }

}
