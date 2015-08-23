package idv.seventhmoon.spotifystreamer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.spotify.api.model.Track;

import java.util.List;

import idv.seventhmoon.spotifystreamer.PlayerSession;
import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.fragment.FullScreenPlayerFragment;
import idv.seventhmoon.spotifystreamer.fragment.TrackListFragment;


public class TrackListActivity extends BaseActivity implements TrackListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks_list);

        if (savedInstanceState == null) {


            String artistId = getIntent().getStringExtra(TrackListFragment.ARG_ARTIST_ID);
            String artistName = getIntent().getStringExtra(TrackListFragment.ARG_ARTIST);


            TrackListFragment fragment = TrackListFragment.newInstance(artistId, artistName);

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
    public void onTrackSelected(List<Track> tracks, int trackPosition) {


        //for phase 2 test

        FragmentManager fragmentManager = getSupportFragmentManager();


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
}
