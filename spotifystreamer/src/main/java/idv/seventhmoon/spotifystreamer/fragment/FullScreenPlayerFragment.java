package idv.seventhmoon.spotifystreamer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import idv.seventhmoon.spotifystreamer.activity.SettingsActivity;
import idv.seventhmoon.spotifystreamer.service.MediaPlayerService;

import static android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class FullScreenPlayerFragment extends DialogFragment implements
        Palette.PaletteAsyncListener, MediaPlayerService.MusicServiceCallback,
        MediaPlayerService.OnStateChangeListener {

    private static final String TAG = FullScreenPlayerFragment.class.getSimpleName();

    private BaseActivity mActivity;

    // Palette colors
    private int palettePrimaryColor;
    private int paletteAccentColor;
    private int titleTextColor;
    private int bodyTextColor;
    private int primaryColor;
    private int accentColor;

    // Views
    private ImageView mSkipPrev;
    private ImageView mSkipNext;
    private ImageView mPlayPause;
    private TextView mStart;
    private TextView mEnd;
    private SeekBar mSeekbar;
    private TextView mLine1;
    private TextView mLine2;
    private ImageView mAlbumImage;
    private Toolbar mToolbar;
    private LinearLayout mControls;
    private Drawable mPauseDrawable;
    private Drawable mPlayDrawable;

    // Sharing
    private ShareActionProvider mShareActionProvider;

    private PlayerSession mSession;
    private MediaPlayerService mMusicService;
    private Intent mPlayIntent;
    private Track mTrack;
    private boolean serviceBound = false;
    private boolean isPlaying = (mMusicService != null);
    private boolean viewsAreCreated = false;

    public FullScreenPlayerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "FullScreenPlayerFragment onCreate called");
        super.onCreate(savedInstanceState);

        mActivity = (BaseActivity) getActivity();

        primaryColor = palettePrimaryColor = ContextCompat.getColor(getContext(), R.color.primary);
        accentColor = paletteAccentColor = ContextCompat.getColor(getContext(), R.color.accent);
        titleTextColor = ContextCompat.getColor(getContext(), R.color.title_text);
        bodyTextColor = ContextCompat.getColor(getContext(), R.color.body_text);


        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(FullScreenPlayerActivity.PLAYER_SESSION)) {
            this.mSession = arguments.getParcelable(FullScreenPlayerActivity.PLAYER_SESSION);

            assert this.mSession != null;
            this.mTrack = mSession.getCurrentTrack();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // Initialize views
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mAlbumImage = (ImageView) view.findViewById(R.id.player_album_image);
        mStart = (TextView) view.findViewById(R.id.startText);
        mEnd = (TextView) view.findViewById(R.id.endText);
        mLine2 = (TextView) view.findViewById(R.id.player_track_title);
        mLine1 = (TextView) view.findViewById(R.id.player_artist_name);
        mControls = (LinearLayout) view.findViewById(R.id.player_control_layout);
        mSeekbar = (SeekBar) view.findViewById(R.id.player_seekbar);

        mSkipPrev = (ImageView) view.findViewById(R.id.player_control_prev);
        mPlayPause = (ImageView) view.findViewById(R.id.player_control_play_pause);
        mSkipNext = (ImageView) view.findViewById(R.id.player_control_next);

        mPauseDrawable = getActivity().getDrawable(R.drawable.ic_pause_white_48dp);
        mPlayDrawable = getActivity().getDrawable(R.drawable.ic_play_arrow_white_48dp);

        // Music control buttons
        mSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicService != null) mMusicService.playNext();
            }
        });

        mSkipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicService != null) mMusicService.playPrevious();
            }
        });

        mPlayPause.setImageDrawable((isPlaying) ? mPauseDrawable : mPlayDrawable);

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = !isPlaying;

                if (mMusicService != null) {
                    mMusicService.togglePlay();
                    mPlayPause.setImageDrawable((mMusicService.isPlaying()) ? mPauseDrawable : mPlayDrawable);
                }


            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private boolean userInitiated = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekTo(progress, userInitiated);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userInitiated = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userInitiated = false;
            }
        });


        // Toolbar
        if (isAdded()) {
            mToolbar.setTitle(getString(R.string.now_playing));
            mToolbar.setNavigationIcon(R.drawable.abc_ic_clear_mtrl_alpha);

            // Close/back button
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    showControlsFromDialogClose();
                    unbindService();
                }
            });

        }


        if (getActivity() != null && getActivity() instanceof FullScreenPlayerActivity) {
            FullScreenPlayerActivity activity = (FullScreenPlayerActivity) getActivity();
            activity.setSupportActionBar(mToolbar);
        }

        viewsAreCreated = true;

        setViewsInfo(mTrack);

        updateShareIntent();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mToolbar.inflateMenu(R.menu.menu_player);

        // Settings menu
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.menu_settings) {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
                }

                return false;
            }
        });

        // Retrieve the share menu item
        MenuItem shareItem = mToolbar.getMenu().findItem(R.id.menu_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        updateShareIntent();
    }

    private Intent createShareTrackIntent() {
        if (mMusicService == null) return null;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.app_name) + " - " +
                        mMusicService.getSession().getCurrentTrack().getName() + " - " +
                        mMusicService.getSession().getCurrentTrack().getPreviewUrl());
        return shareIntent;
    }

    private void updateShareIntent() {
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrackIntent());
        } else {
            Log.d(TAG, "Share action provider is null?");
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "FullScreenPlayerFragment onDestroyView called");
        viewsAreCreated = false;

        unbindService();

        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);


        if (mActivity.getNumberOfPane() > 1) {
            showControlsFromDialogClose();
        }

    }

    public void unbindService() {
        if (mMusicService != null) {
            mMusicService.unregisterCallback(null);
            getActivity().unbindService(streamingConnection);
            mMusicService = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "FullScreenPlayerFragment onSaveInstanceState called");
        super.onSaveInstanceState(outState);

        outState.putParcelable(FullScreenPlayerActivity.PLAYER_SESSION, mSession);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "FullScreenPlayerFragment onActivityCreated called");
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(FullScreenPlayerActivity.PLAYER_SESSION)) {
            mSession = savedInstanceState.getParcelable(FullScreenPlayerActivity.PLAYER_SESSION);

            if (mSession != null) {
                setViewsInfo(mSession.getCurrentTrack());
            }
        }

        if (mPlayIntent == null) {
            Activity activity = getActivity();
            mPlayIntent = new Intent(activity, MediaPlayerService.class);
            activity.bindService(mPlayIntent, streamingConnection, Activity.BIND_AUTO_CREATE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "FullScreenPlayerFragment onCreateDialog called");
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void setViewsInfo(Track track) {
        if (viewsAreCreated) {
            mLine1.setText(track.getName());
            mLine2.setText(track.getPrintableArtists());
            fetchImageAsync(track);

            int duration = track.getPreviewDuration();
            mSeekbar.setMax(duration);
            mEnd.setText(formatMillis(duration));

            // Update the share intent
            updateShareIntent();
        }
    }

    private void showControlsFromDialogClose() {
        if (mActivity != null) {
            mActivity.setSession();
            mActivity.getControlsFragment().setViewsInfo();
            mActivity.showPlaybackControls();
        }
    }


    // Seekbar functions
    public void seekTo(int position, boolean userInitiated) {
        mStart.setText(formatMillis(position));
        if (userInitiated && mMusicService != null) mMusicService.seekTo(position);
    }

    private void updateProgress() {
        mSeekbar.setProgress(mMusicService.getCurrentPosition());
    }

    private void fetchImageAsync(final Track track) {
        if (mAlbumImage.getWidth() == 0 && mAlbumImage.getHeight() == 0) {
            mAlbumImage.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            loadImage(track);
                            mAlbumImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        } else {
            loadImage(track);
        }
    }

    private void loadImage(Track track) {

        List<Image> images = track.getAlbum().getImages();

        if (!images.isEmpty()) {
            Image image = ImageUtil.getBestFitImage(images, mAlbumImage.getWidth(), mAlbumImage.getHeight());
            Glide.with(this).load(image.getUrl()).centerCrop().into(mAlbumImage);
        }


    }

    // Palette overrides
    @Override
    public void onGenerated(Palette palette) {
        Palette.Swatch primarySwatch = palette.getDarkMutedSwatch();

        palettePrimaryColor = palette.getDarkMutedColor(primaryColor);
        paletteAccentColor = palette.getVibrantColor(accentColor);

        if (palettePrimaryColor == primaryColor) {
            primarySwatch = palette.getMutedSwatch();

            palettePrimaryColor = palette.getMutedColor(primaryColor);
            paletteAccentColor = palette.getLightVibrantColor(accentColor);
        }

        if (primarySwatch != null) {
            titleTextColor = primarySwatch.getTitleTextColor();
            bodyTextColor = primarySwatch.getBodyTextColor();
        }

        mLine1.setBackgroundColor(palettePrimaryColor);
        mLine1.setTextColor(titleTextColor);
        mLine2.setBackgroundColor(palettePrimaryColor);
        mLine2.setTextColor(bodyTextColor);

        mControls.setBackgroundColor(palette.getMutedColor(primaryColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Seekbar color
            mSeekbar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(paletteAccentColor, PorterDuff.Mode.MULTIPLY));
            mSeekbar.getThumb().mutate().setAlpha(0);

            // Status bar color
            if (getActivity() != null && getActivity() instanceof FullScreenPlayerActivity) {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(palette.getMutedColor(primaryColor));
            }
        }
    }


    // MusicService callback overrides
    @Override
    public void onProgressChange(int progress) {
        mSeekbar.setProgress(progress);
    }

    @Override
    public void onTrackChanged(Track track) {
        setViewsInfo(track);
    }

    @Override
    public void onPlaybackStopped() {
        mPlayPause.setImageDrawable(mPlayDrawable);
        isPlaying = false;
    }

    @Override
    public void onStateChanged(boolean isPlaying) {
        if (viewsAreCreated) {

            mPlayPause.setImageDrawable((isPlaying) ? mPauseDrawable : mPlayDrawable);
        }
    }

    private void setOnPlayerStateChanged() {
        if (mMusicService != null) {
            mMusicService.setOnStateChangeListener(this);
        }
    }

    public static String formatMillis(int millisec) {
        int seconds = millisec / 1000;
        int hours = seconds / 3600;
        seconds %= 3600;
        int minutes = seconds / 60;
        seconds %= 60;
        String time;
        if (hours > 0) {
            time = String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            time = String.format("%d:%02d", minutes, seconds);

        }

        return time;
    }

    private ServiceConnection streamingConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "FullScreenPlayerFragment onServiceCreated called");
            MediaPlayerService.MusicBinder binder = (MediaPlayerService.MusicBinder) service;

            mMusicService = binder.getService();

            idv.seventhmoon.spotifystreamer.PlayerSession serviceSession = mMusicService.getSession();
            if (serviceSession != null && serviceSession.equals(mSession)) {
                mSession = serviceSession;
            } else {
                mMusicService.setSession(mSession);
            }

            updateShareIntent();
            mMusicService.registerCallback(FullScreenPlayerFragment.this);
            setOnPlayerStateChanged();
            onStateChanged(mMusicService.isPlaying());

            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}