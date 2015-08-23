package idv.seventhmoon.spotifystreamer.fragment;

import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spotify.api.model.Image;
import com.spotify.api.model.Track;

import java.util.List;

import idv.seventhmoon.spotifystreamer.ImageUtil;
import idv.seventhmoon.spotifystreamer.PlayerSession;
import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.activity.BaseActivity;
import idv.seventhmoon.spotifystreamer.activity.FullScreenPlayerActivity;
import idv.seventhmoon.spotifystreamer.service.MediaPlayerService;

/**
 * Created by fung on 23/08/2015.
 */
public class PlaybackControlsFragment extends Fragment implements
        MediaPlayerService.MusicServiceCallback, MediaPlayerService.OnStateChangeListener {

    private static final String TAG = PlaybackControlsFragment.class.getSimpleName();

    private BaseActivity mActvity;

    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mExtraInfo;
    private ImageView mAlbumArt;
    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;
    private String mArtUrl;

    private PlayerSession mSession;
    private MediaPlayerService mMusicService;
    private boolean serviceBound;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActvity = (BaseActivity) getActivity();
        setViewsInfo();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);

        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicService != null) mMusicService.togglePlay();

                assert mMusicService != null;
                mPlayPause.setImageDrawable((mMusicService.isPlaying()) ? mPauseDrawable : mPlayDrawable);
            }
        });

        mPauseDrawable = getActivity().getDrawable(R.drawable.ic_media_pause);
        mPlayDrawable = getActivity().getDrawable(R.drawable.ic_media_play);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSubtitle = (TextView) rootView.findViewById(R.id.artist);
        mExtraInfo = (TextView) rootView.findViewById(R.id.extra_info);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager =
                        ((FragmentActivity) getActivity()).getSupportFragmentManager();

//                if (MainActivity.mIsLargeLayout) {
                if (mActvity.getNumberOfPane() > 1) {
                    FullScreenPlayerFragment fragment = new FullScreenPlayerFragment();
                    Bundle arguments = new Bundle();

                    arguments.putParcelable(FullScreenPlayerActivity.PLAYER_SESSION, mSession);
                    fragment.setArguments(arguments);
                    fragment.show(fragmentManager, "PLAYER");
                } else {
                    Intent intent = new Intent(getActivity(), FullScreenPlayerActivity.class);
                    intent.putExtra(FullScreenPlayerActivity.PLAYER_SESSION, mSession);

                    startActivity(intent);
                }
            }
        });

//        setViewsInfo();

        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "fragment.onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        setViewsInfo();
    }

    @Override
    public void onDestroy() {
        unbindService();
        super.onDestroy();
    }

    public void unbindService() {
        if (mMusicService != null) {
            mMusicService.unregisterCallback(null);
            getActivity().unbindService(streamingConnection);
            mMusicService = null;
        }
    }

    public void setViewsInfo() {
        if (mSession != null) {

            setViewsInfo(mSession.getCurrentTrack());
        }
    }

    private void setViewsInfo(Track track) {
        if (mSession != null) {

            mTitle.setText(track.getName());
            mSubtitle.setText(track.getPrintableArtists());
            Track currentTrack = mSession.getCurrentTrack();
            List<Image> images = currentTrack.getAlbum().getImages();

            if (!images.isEmpty() & isAdded()) {
                Image image = ImageUtil.getBestFitImage(images, mAlbumArt.getWidth(), mAlbumArt.getHeight());

                Glide.with(this).load(image.getUrl())
                        .into(mAlbumArt);
            }
        }
    }

    // MusicService callback overrides
    @Override
    public void onProgressChange(int progress) {
    }

    @Override
    public void onTrackChanged(Track track) {
        setViewsInfo(track);
    }

    @Override
    public void onPlaybackStopped() {
        mPlayPause.setImageDrawable(mPlayDrawable);
    }

    @Override
    public void onStateChanged(boolean isPlaying) {
//        BaseActivity activity = (BaseActivity) getActivity();

        mPlayPause.setImageDrawable((isPlaying) ? mPauseDrawable : mPlayDrawable);
    }

    public boolean shouldShowControls() {

        Log.d(TAG, "mMusicService is " + mMusicService);
        Log.d(TAG, "mSession is " + mSession);
//        Log.d(TAG, "mSession.getCurrentTrack() is " + mSession.getCurrentTrack());

        return !(mMusicService == null || mSession == null || mSession.getCurrentTrack() == null);
    }

    public void setSession() {
        if (mMusicService != null) {
            mSession = mMusicService.getSession();

            mMusicService.setOnStateChangeListener(this);

            onStateChanged(mMusicService.isPlaying());
        }
    }

    public ServiceConnection streamingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MusicBinder binder = (MediaPlayerService.MusicBinder) service;

            mMusicService = binder.getService();

            setSession();
            mMusicService.registerCallback(PlaybackControlsFragment.this);

            setViewsInfo();

            // Show controls if something is playing or paused
            if (shouldShowControls()) ((BaseActivity) getActivity()).showPlaybackControls();

            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}
