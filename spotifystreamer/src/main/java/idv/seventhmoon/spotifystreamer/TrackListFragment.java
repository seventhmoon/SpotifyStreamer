package idv.seventhmoon.spotifystreamer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spotify.api.SpotifyApiHelper;
import com.spotify.api.model.GetArtistsTopTracksResponseModel;
import com.spotify.api.model.Track;

import java.util.List;

//receive keyword from activity, call API, display result
//when cell clicked, pass the albumId to search tracks

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackListFragment extends Fragment {

    public static final String TAG = TrackListFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_ARTIST_ID = "artistId";
    public static final String ARG_ARTIST = "artist";

    private String mCountryCode;
    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

//    private TextView mTextViewSearching;
//    private TextView mTextViewNoResult;
    private TextView mTextViewMessage;
    private String mArtistId;
    private String mArtist;


    private OnFragmentInteractionListener mListener;

    public TrackListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param artistId  keyword for searching album

     * @return A new instance of fragment ArtistListFragment.
     */

    public static TrackListFragment newInstance(String artistId, String artistName) {
        TrackListFragment fragment = new TrackListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistId);
        args.putString(ARG_ARTIST, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            mArtist = getArguments().getString(ARG_ARTIST);

        }
        mApplication = (MainApplication) getActivity().getApplication();
        mCountryCode = mApplication.getCountryCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_track_list, container, false);
//        mTextViewSearching = (TextView) rootView.findViewById(R.id.text_searching);
//        mTextViewNoResult =  (TextView) rootView.findViewById(R.id.text_no_result);
        mTextViewMessage = (TextView) rootView.findViewById(R.id.text_message);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mApplication);
        mRecyclerView.setLayoutManager(mLayoutManager);

        displaySearching();
        searchForTrack(mArtistId);

        return rootView;
    }

//    public void onTrackSelected(String trackId){
//        if (mListener != null) {
//            mListener.onTrackSelected(trackId);
//        }
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void displaySearching() {
        mTextViewMessage.setText(R.string.text_searching);
        mTextViewMessage.setVisibility(View.VISIBLE);
//        mTextViewSearching.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
//        mTextViewNoResult.setVisibility(View.GONE);
    }

    private void displayResult(){
        mTextViewMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
//        mTextViewNoResult.setVisibility(View.GONE);
    }

    private void displayNoResult(){
//        mTextViewSearching.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mTextViewMessage.setText(R.string.text_no_result);
        mTextViewMessage.setVisibility(View.VISIBLE);
    }

    private void setActionBarTitle(String title, String subTitle){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subTitle);
        }


    }

    private void searchForTrack(String artistId){

        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper(mApplication.getRequestQueue());
        spotifyApiHelper.searchArtistsTopTracks(artistId, mCountryCode, new Response.Listener<GetArtistsTopTracksResponseModel>() {
            @Override
            public void onResponse(GetArtistsTopTracksResponseModel response) {


                setActionBarTitle(getString(R.string.text_top_10_tracks), mArtist);
                List<Track> tracks = response.getTracks();
                if (tracks.isEmpty()) {
                    //show no result page
                    displayNoResult();
                } else {

                    mAdapter = new TopTracksAdapter(mApplication, tracks, mListener);
//                    Log.d(TAG, tracks.toString());
                    mRecyclerView.setAdapter(mAdapter);

                    displayResult();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                if (error.networkResponse.statusCode == 400){
                    Toast.makeText(getActivity(), getString(R.string.text_network_bad_request), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), getString(R.string.text_network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        void onTrackSelected(List<Track> tracks, int trackNumber);


    }

}
