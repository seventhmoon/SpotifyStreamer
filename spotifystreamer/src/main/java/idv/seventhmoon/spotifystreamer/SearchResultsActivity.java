package idv.seventhmoon.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * Created by fung on 04/07/2015.
 */
public class SearchResultsActivity extends AppCompatActivity {

    public static final String TAG = SearchResultsActivity.class.getSimpleName();

    private MainApplication mApplication;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);

        mApplication = (MainApplication) this.getApplication();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

//        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper(mApplication.getRequestQueue());
//        spotifyApiHelper.searchAlbum(query, limit, new Response.Listener<SearchAlbumResponseModel>() {
//            @Override
//            public void onResponse(SearchAlbumResponseModel response) {
//
//                mAdapter = new SearchArtistResultAdapter(getApplicationContext(), response.getAlbums().getAlbums(), mListener);
//                mRecyclerView.setAdapter(mAdapter);
//                Log.d(TAG, response.getAlbums().getAlbums().toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, error.toString());
//            }
//        });

    }
}
