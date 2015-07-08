package idv.seventhmoon.spotifystreamer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.api.model.Image;
import com.spotify.api.model.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fung on 05/07/2015.
 */
public class TopTracksAdapter extends RecyclerView.Adapter<TopTracksAdapter.ViewHolder> {


    private List<Track> mTracks;
    private Context mContext;
    private TrackListFragment.OnFragmentInteractionListener mListener;


    public TopTracksAdapter(Context context, List<Track> tracks, TrackListFragment.OnFragmentInteractionListener listener) {
        mTracks = tracks;
        mContext = context;
        mListener = listener;

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewAlbum;
        public TextView mTextViewTrack;
        public ImageView mImageViewThumbnail;
        public View mRootView;

        public ViewHolder(View rootView) {
            super(rootView);

            mRootView = rootView;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TopTracksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_track, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(rootView);
        vh.mImageViewThumbnail = (ImageView) rootView.findViewById(R.id.image_thumbnail);
        vh.mTextViewAlbum = (TextView) rootView.findViewById(R.id.text_album);
        vh.mTextViewTrack = (TextView) rootView.findViewById(R.id.text_track);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        final Track track = mTracks.get(position);
        holder.mTextViewAlbum.setText(track.getAlbum().getName());
        holder.mTextViewTrack.setText(track.getName());

        List<Image> images = track.getAlbum().getImages();
        if (!images.isEmpty()){
            Picasso.with(mContext).load(images.get(0).getUrl()).fit().centerCrop().into(holder.mImageViewThumbnail);
        }

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTrackSelected(track.getId());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}
