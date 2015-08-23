package idv.seventhmoon.spotifystreamer.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.fragment.FullScreenPlayerFragment;

/**
 * Created by fung on 23/08/2015.
 */

public class FullScreenPlayerActivity extends BaseActivity {
    public static final String PLAYER_SESSION = "us.phyxsi.spotifystreamer.PLAYER_SESSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

//            initializeToolbar(R.menu.menu_player);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(PLAYER_SESSION,
                    getIntent().getExtras().getParcelable(PLAYER_SESSION));

            FullScreenPlayerFragment fragment = new FullScreenPlayerFragment();
            fragment.setArguments(arguments);

            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(android.R.id.content, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

