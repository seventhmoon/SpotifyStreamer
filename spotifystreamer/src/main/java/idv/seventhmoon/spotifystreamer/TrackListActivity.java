package idv.seventhmoon.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.spotify.api.model.Track;

import java.util.List;


public class TrackListActivity extends AppCompatActivity implements TrackListFragment.OnFragmentInteractionListener, PlayerFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);

        if (savedInstanceState == null) {
//            Bundle arguments = new Bundle();

            String artistId = getIntent().getStringExtra(TrackListFragment.ARG_ARTIST_ID);
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
//        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onTrackSelected(List<Track> tracks, int trackNumber) {
//        Toast.makeText(this, getString(R.string.text_not_implemented), Toast.LENGTH_SHORT).show();

        //for phase 2
        PlayerFragment fragment = PlayerFragment.newInstance(tracks, trackNumber);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.commit();
//        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }
}
