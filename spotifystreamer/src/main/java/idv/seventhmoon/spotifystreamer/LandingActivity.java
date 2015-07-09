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
import android.view.View;
import android.widget.TextView;


public class LandingActivity extends AppCompatActivity implements ArtistListFragment.OnFragmentInteractionListener {

    public static final String TAG = LandingActivity.class.getSimpleName();

    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mTextIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);

        mTextIntro = (TextView) findViewById(R.id.text_intro);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
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

    private void searchForArtist(String query, int limit){

        mTextIntro.setVisibility(View.GONE);

        ArtistListFragment fragment = ArtistListFragment.newInstance(query, limit);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onArtistSelected(String artistId, String artistName) {
//        Toast.makeText(this, artistId, Toast.LENGTH_SHORT).show();

        Intent detailIntent = new Intent(this,
                TrackListActivity.class);
        detailIntent.putExtra(TrackListFragment.ARG_ARTIST_ID, artistId);
        detailIntent.putExtra(TrackListFragment.ARG_ARTIST, artistName);
        startActivity(detailIntent);

    }
}
