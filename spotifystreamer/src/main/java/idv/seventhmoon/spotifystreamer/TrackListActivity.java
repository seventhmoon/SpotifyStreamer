package idv.seventhmoon.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class TrackListActivity extends AppCompatActivity implements TrackListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            String artistId =  getIntent().getStringExtra(TrackListFragment.ARG_ARTIST_ID);
            String artistName = getIntent().getStringExtra(TrackListFragment.ARG_ARTIST);
//            arguments.putString(TrackListFragment.ARG_ARTIST_ID,);

            TrackListFragment fragment = TrackListFragment.newInstance(artistId, artistName);
//                    TrackListFragment fragment = new RouteDetailFragment();
//            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_placeholder, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tracks_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrackSelected(String trackId) {
        Toast.makeText(this, trackId, Toast.LENGTH_SHORT).show();
    }
}
