package idv.seventhmoon.spotifystreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.fragment.PlaybackControlsFragment;
import idv.seventhmoon.spotifystreamer.service.MediaPlayerService;

/**
 * Created by fung on 23/08/2015.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private PlaybackControlsFragment mControlsFragment;
    private Intent serviceIntent;
    private int mNumberOfPane;

//    public static boolean mIsLargeLayout;

//    public boolean ismIsLargeLayout() {
//        return mIsLargeLayout;
//    }

    public int getNumberOfPane(){
        return mNumberOfPane;
    }

    public PlaybackControlsFragment getControlsFragment() {
        return mControlsFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumberOfPane = getResources().getInteger(R.integer.number_of_pane);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Activity onStart");

        mControlsFragment = (PlaybackControlsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);
        if (mControlsFragment == null) {
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        }

        hidePlaybackControls();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (serviceIntent == null) {
            serviceIntent = new Intent(this, MediaPlayerService.class);
            bindService(serviceIntent, mControlsFragment.streamingConnection, Context.BIND_AUTO_CREATE);
        } else {
            setSession();
        }

        if (mControlsFragment.shouldShowControls()) {

            Log.wtf(TAG, "shouldShowControls");
            showPlaybackControls();
        }else{
            Log.wtf(TAG, "shouldNotShowControls");
        }

    }

    public void setSession() {
        mControlsFragment.setSession();
    }

    public void showPlaybackControls() {
        Log.d(TAG, "showPlaybackControls");
        if (isConnectedToNetwork()) {
            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(
//                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
//                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                    .show(mControlsFragment)
                    .commit();
        }
    }

    protected void hidePlaybackControls() {
        Log.d(TAG, "hidePlaybackControls");
        getSupportFragmentManager().beginTransaction()
                .hide(mControlsFragment)
                .commit();
    }

//    protected void initializeToolbar(int menu) {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (mToolbar == null) {
//            throw new IllegalStateException("Layout is required to include a Toolbar with id " +
//                    "'toolbar'");
//        }
//        mToolbar.inflateMenu(menu);
//
//        setSupportActionBar(mToolbar);
//
//        boolean isRoot = getFragmentManager().getBackStackEntryCount() == 0;
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
//            getSupportActionBar().setHomeButtonEnabled(!isRoot);
//        }
//    }

    public boolean isConnectedToNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
