package idv.seventhmoon.spotifystreamer;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;


public class LandingActivity extends AppCompatActivity implements ArtistListFragment.OnFragmentInteractionListener, TrackListFragment.OnFragmentInteractionListener, PlayerFragment.OnFragmentInteractionListener {

    public static final String TAG = LandingActivity.class.getSimpleName();

    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchView mSearchView;
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

        if (mTwoPane){

            TrackListFragment fragment = TrackListFragment.newInstance(artistId, artistName);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.view_right_pane, fragment);
            fragmentTransaction.commit();

        }else{

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
    public void onTrackSelected(String trackId) {
        PlayerFragment fragment = PlayerFragment.newInstance(trackId);
        fragment.show(getSupportFragmentManager(), TAG);
    }
}
