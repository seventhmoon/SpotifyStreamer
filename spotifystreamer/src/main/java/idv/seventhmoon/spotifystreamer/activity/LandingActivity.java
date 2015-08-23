package idv.seventhmoon.spotifystreamer.activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.spotify.api.model.Track;

import java.util.List;

import idv.seventhmoon.spotifystreamer.MainApplication;
import idv.seventhmoon.spotifystreamer.PlayerSession;
import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.fragment.ArtistListFragment;
import idv.seventhmoon.spotifystreamer.fragment.FullScreenPlayerFragment;
import idv.seventhmoon.spotifystreamer.fragment.PlayerFragment;
import idv.seventhmoon.spotifystreamer.fragment.TrackListFragment;


public class LandingActivity extends BaseActivity
        implements ArtistListFragment.OnFragmentInteractionListener,
        TrackListFragment.OnFragmentInteractionListener,
        PlayerFragment.OnFragmentInteractionListener {

    public static final String TAG = LandingActivity.class.getSimpleName();
    private static final int RESULT_SETTINGS = 1;

    public static final String ARG_TRACKS = "tracks";
    public static final String ARG_TRACK_POSITION = "track_position";
    private static final String ARG_MEDIA_SESSION = "SpotifyStreamer";

    private MediaSession mMediaSession;

    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView mSearchView;

    private MediaBrowser mMediaBrowser;




//    private TextView mTextIntro;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);

//        mTextIntro = (TextView) findViewById(R.id.text_intro);


        mTwoPane = (findViewById(R.id.view_right_pane) != null);

//        if (findViewById(R.id.view_right_pane) != null){
//            mTwoPane = true;
//
//        }else{
//            mTwoPane = false;
//
//        }

        ArtistListFragment fragment = new ArtistListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_left_pane, fragment);
        fragmentTransaction.commit();


        handleIntent(getIntent());

        mMediaSession = new MediaSession(this, TAG);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();

        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            searchForArtist(query, getResources().getInteger(R.integer.default_search_limit));
        }
    }

    private void searchForArtist(String query, int limit) {

//        mTextIntro.setVisibility(View.GONE);

        ArtistListFragment fragment = ArtistListFragment.newInstance(query, limit);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.view_left_pane, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onArtistSelected(String artistId, String artistName) {
//        Toast.makeText(this, artistId, Toast.LENGTH_SHORT).show();

        if (mTwoPane) {

            TrackListFragment fragment = TrackListFragment.newInstance(artistId, artistName);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.view_right_pane, fragment);
            fragmentTransaction.commit();

        } else {

            Intent detailIntent = new Intent(this,
                    TrackListActivity.class);
            detailIntent.putExtra(TrackListFragment.ARG_ARTIST_ID, artistId);
            detailIntent.putExtra(TrackListFragment.ARG_ARTIST, artistName);
            startActivity(detailIntent);
        }


    }

    @Override
    public void onSearchReturnNoResult() {
        mSearchView.setIconified(true);

    }

    @Override
    public void onTrackSelected(List<Track> tracks, int trackPosition) {
//        PlayerFragment fragment = PlayerFragment.newInstance(tracks, trackPosition);
//        fragment.show(getSupportFragmentManager(), TAG);

        FragmentManager fragmentManager = getSupportFragmentManager();
//        Track currnetTrack = tracks.get(trackPosition);

        PlayerSession playerSession = new PlayerSession(tracks, trackPosition);

        if (getNumberOfPane() == 1){
            Intent intent = new Intent(this, FullScreenPlayerActivity.class);
            intent.putExtra(FullScreenPlayerActivity.PLAYER_SESSION, playerSession);

            startActivity(intent);
        }else {
            FullScreenPlayerFragment fragment = new FullScreenPlayerFragment();
            Bundle arguments = new Bundle();

            arguments.putParcelable(FullScreenPlayerActivity.PLAYER_SESSION, playerSession);
            fragment.setArguments(arguments);
            fragment.show(fragmentManager, "PLAYER");
        }
    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent detailIntent = new Intent(this,
                        SettingsActivity.class);

//                startActivity(detailIntent);
                startActivityForResult(detailIntent, RESULT_SETTINGS);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }



//    public void onPauseButtonPressed() {
//
//    }
//
//    public void onNextButtonPressed() {
//
//    }
//
//    public void onPervButtonPressed() {
//
//    }
//
//    public void onPlayButtonPressed() {
//
//    }

//    @SuppressLint("CommitPrefEdits")
//    public void saveTracks(List<Track> tracks) {
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putBoolean("silentMode", mSilentMode);
//        editor.putString(ARG_TRACKS, new Gson().toJson(tracks));
//        // Commit the edits!
//        editor.commit();
//    }
//
//    public void saveTrackPosition(int trackPosition) {
//
//
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putBoolean("silentMode", mSilentMode);
//        editor.putInt(ARG_TRACK_POSITION, trackPosition);
//        // Commit the edits!
//        editor.commit();
//    }

}
