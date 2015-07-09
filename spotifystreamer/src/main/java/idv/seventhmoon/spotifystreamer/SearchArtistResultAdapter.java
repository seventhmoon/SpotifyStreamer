package idv.seventhmoon.spotifystreamer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.api.model.Artist;
import com.spotify.api.model.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fung on 05/07/2015.
 */
public class SearchArtistResultAdapter extends RecyclerView.Adapter<SearchArtistResultAdapter.ViewHolder> {


    private List<Artist> mArtists;
    private Context mContext;
    ArtistListFragment.OnFragmentInteractionListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextArtistName;
        public ImageView mImageArtistPhoto;
        public View mRootView;
        public ViewHolder(View rootView) {
            super(rootView);
            mRootView = rootView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchArtistResultAdapter(Context context, List<Artist> artists, ArtistListFragment.OnFragmentInteractionListener listener){
        mArtists = artists;
        mContext = context;
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchArtistResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_artist, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(rootView);
        vh.mTextArtistName = (TextView) rootView.findViewById(R.id.text_artist_name);
        vh.mImageArtistPhoto = (ImageView) rootView.findViewById(R.id.image_artist_photo);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Artist artist = mArtists.get(position);
        holder.mTextArtistName.setText(artist.getName());

        List<Image> images = artist.getImages();
        if (!images.isEmpty()){
            Picasso.with(mContext).load(artist.getImages().get(0).getUrl()).fit().centerCrop().into(holder.mImageArtistPhoto);
        }

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onArtistSelected(artist.getId(), artist.getName());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mArtists.size();
    }
}
