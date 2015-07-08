package idv.seventhmoon.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.spotify.api.SpotifyApiHelper;
import com.spotify.api.model.Artist;
import com.spotify.api.model.SearchArtistResponseModel;

import java.util.List;

//receive keyword from activity, call API, display result
//when cell clicked, pass the albumId to search tracks
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistListFragment extends Fragment {

    public static final String TAG = ArtistListFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SEARCH_KEYWORD = "searchKeyword";
    private static final String ARG_SEARCH_LIMIT = "searchLimit";

    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mTextViewSearching;

    private String mSearchKeyword;
    private int mSearchLimit;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param searchKeyword keyword for searching album
     * @param searchLimit number of returned result
     * @return A new instance of fragment ArtistListFragment.
     */

    public static ArtistListFragment newInstance(String searchKeyword, int searchLimit) {
        ArtistListFragment fragment = new ArtistListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_KEYWORD, searchKeyword);
        args.putInt(ARG_SEARCH_LIMIT, searchLimit);
        fragment.setArguments(args);
        return fragment;
    }

    public ArtistListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchKeyword = getArguments().getString(ARG_SEARCH_KEYWORD);
            mSearchLimit = getArguments().getInt(ARG_SEARCH_LIMIT);
        }
        mApplication = (MainApplication) getActivity().getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist_list, container, false);
        mTextViewSearching = (TextView) rootView.findViewById(R.id.text_searching);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mApplication);
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchForArtist(mSearchKeyword, mSearchLimit);

        return rootView;
    }

    public void onArtistSelected(String artistId){
        if (mListener != null) {
            mListener.onArtistSelected(artistId);
        }
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

        public void onArtistSelected(String artistId);

    }

    private void displayResult(){
        mTextViewSearching.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void searchForArtist(String query, int limit){

        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper(mApplication.getRequestQueue());
        spotifyApiHelper.searchArtist(query, limit, new Response.Listener<SearchArtistResponseModel>() {
            @Override
            public void onResponse(SearchArtistResponseModel response) {

                List<Artist> artists = response.getArtists().getArtists();
                if (artists.isEmpty()) {
                    //show no result page

                } else {

                    mAdapter = new SearchArtistResultAdapter(mApplication, artists, mListener);
                                  Log.d(TAG,artists.toString());
                    mRecyclerView.setAdapter(mAdapter);

                    displayResult();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });

    }

}
