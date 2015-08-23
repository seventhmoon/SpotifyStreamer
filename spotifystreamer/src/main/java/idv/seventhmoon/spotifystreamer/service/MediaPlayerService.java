package idv.seventhmoon.spotifystreamer.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.spotify.api.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import idv.seventhmoon.spotifystreamer.PlayerSession;
import idv.seventhmoon.spotifystreamer.R;
import idv.seventhmoon.spotifystreamer.receiver.MediaNotificationReceiver;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = MediaPlayerService.class.getSimpleName();
    // The action of the incoming Intent indicating that it contains a command
    // to be executed (see {@link #onStartCommand})
    public static final String ACTION_CMD = "ACTION_CMD";
    // The key in the extras of the incoming Intent indicating the command that
    // should be executed (see {@link #onStartCommand})
    public static final String CMD_NAME = "CMD_NAME";
    private static final long PROGRESS_UPDATE_INTERNAL = 100;

    private final IBinder mMusicBinder = new MusicBinder();
    private MediaPlayer mPlayer;
    private PlayerSession mSession;
    private MediaNotificationReceiver mMediaNotificationReceiver;

    private List<MusicServiceCallback> mCallbacks;

    private OnStateChangeListener mOnStateChangeListener;
    private Handler mHandler = new Handler();
    private boolean watchProgressUpdate = false;
    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            if (mCallbacks != null && mPlayer != null) {
                for (MusicServiceCallback callback : mCallbacks) {
                    try {
                        callback.onProgressChange(mPlayer.getCurrentPosition());
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (watchProgressUpdate) mHandler.postDelayed(this, PROGRESS_UPDATE_INTERNAL);
        }
    };

    public PlayerSession getSession() {
        return mSession;
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void setSession(PlayerSession session) {
        if (mSession != null) {
            stop();
        }

        this.mSession = session;

        if (session == null || session.getPlaylistSize() == 0) {
            return;
        }

        initializePlayer();

        play();

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(getString(R.string.key_notification), true))
            mMediaNotificationReceiver.startNotification();
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public void registerCallback(MusicServiceCallback callback) {
        if (mCallbacks == null) mCallbacks = new ArrayList<>();

        mCallbacks.add(callback);
    }

    public void unregisterCallback(MusicServiceCallback callback) {
        if (mCallbacks == null) return;

        mCallbacks.remove(callback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            String command = intent.getStringExtra(CMD_NAME);

            if (ACTION_CMD.equals(action)) {
                if (mPlayer == null) initializePlayer();
                else mPlayer.reset();

//                setSession((PlayerSession) intent.getParcelableExtra(FullScreenPlayerActivity.PLAYER_SESSION));
            }
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "MusicService onCreate");
        super.onCreate();

        initializePlayer();

        mMediaNotificationReceiver = new MediaNotificationReceiver(this);
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

        super.onDestroy();
    }

    public void initializePlayer() {
        this.mPlayer = new MediaPlayer();

        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public void play() {
        prepareToPlay(mSession.getCurrentTrack());

        changeState(true);
        setWatchProgressUpdate(true);
    }

    public void resume() {
        mPlayer.start();

        changeState(true);
        setWatchProgressUpdate(true);
    }

    public void pause() {
        mPlayer.pause();

        changeState(false);
        setWatchProgressUpdate(false);
    }

    private void stop() {
        mPlayer.stop();

        changeState(false);
        setWatchProgressUpdate(false);
    }

    public void togglePlay() {
        if (mPlayer == null) return;

        if (mPlayer.isPlaying()) pause();
        else resume();
    }

    public void playNext() {
        if (canPlayNext()) {
            Track track = mSession.getNextTrack();

            if (mCallbacks != null) {
                for (MusicServiceCallback callback : mCallbacks) {
                    callback.onTrackChanged(track);
                }
            }

            prepareToPlay(track);
        }
    }

    public void playPrevious() {
        if (canPlayPrev()) {
            Track track = mSession.getPreviousTrack();

            if (mCallbacks != null) {
                for (MusicServiceCallback callback : mCallbacks) {
                    callback.onTrackChanged(track);
                }
            }

            prepareToPlay(track);
        } else
            seekTo(0);
    }

    public void seekTo(int position) {
        if (mPlayer == null) return;

        mPlayer.seekTo(position);
    }

    private void setWatchProgressUpdate(boolean watchProgress) {
        this.watchProgressUpdate = watchProgress;

        if (watchProgress) {
            mUpdateProgressTask.run();
        }
    }

    public boolean canPlayNext() {
        if (mSession == null) return false;

        int playlistSize = mSession.getPlaylistSize();
        if (playlistSize <= 1) return false;

        int nextIndex = mSession.getCurrentPosition() + 1;
        return nextIndex >= 0 && nextIndex < playlistSize;
    }

    public boolean canPlayPrev() {
        if (mSession == null) return false;

        int playlistSize = mSession.getPlaylistSize();
        if (playlistSize <= 1) return false;

        int prevIndex = mSession.getCurrentPosition() - 1;
        return prevIndex >= 0 && prevIndex < playlistSize;
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    private void prepareToPlay(Track track) {
        if (mPlayer == null) initializePlayer();

        mPlayer.reset();

        try {
            mPlayer.setDataSource(track.getPreviewUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        changeState(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayer.reset();

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            if (canPlayNext()) {
                playNext();
            } else {
                if (mCallbacks != null) {
                    for (MusicServiceCallback callback : mCallbacks) {
                        callback.onPlaybackStopped();
                    }
                }

                mPlayer.reset();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (ConcurrentModificationException ex) {
            ex.printStackTrace();
        }
    }

    public class MusicBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    private void changeState(boolean isPlaying) {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onStateChanged(isPlaying);
        }

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(getString(R.string.key_notification), true))
            mMediaNotificationReceiver.updateState();
    }

    public interface OnStateChangeListener {
        void onStateChanged(boolean isPlaying);
    }

    public interface MusicServiceCallback {

        /**
         * Called when the progress of a track has changed
         *
         * @param progress the progress of the track in milliseconds
         */
        void onProgressChange(int progress);

        /**
         * Called when a new track is set
         *
         * @param track the new track to be played
         */
        void onTrackChanged(Track track);

        /**
         * Called when the playlist is completed and playback stopped
         */
        void onPlaybackStopped();
    }
}
