package idv.seventhmoon.spotifystreamer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.api.model.Album;
import com.spotify.api.model.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fung on 05/07/2015.
 */
public class SearchAlbumResultAdapter extends RecyclerView.Adapter<SearchAlbumResultAdapter.ViewHolder> {

    public static final String TAG = SearchAlbumResultAdapter.class.getSimpleName();

    private List<Album> mAlbums;
    private Context mContext;


    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchAlbumResultAdapter(Context context, List<Album> albums) {
        mAlbums = albums;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchAlbumResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_album, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(rootView);
        vh.mTextAlbumName = (TextView) rootView.findViewById(R.id.text_album_name);
        vh.mImageAlbumCover = (ImageView) rootView.findViewById(R.id.image_album_cover);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        Album album = mAlbums.get(position);
        holder.mTextAlbumName.setText(album.getName());

        List<Image> images = album.getImages();
        if (!images.isEmpty()) {
            Image image = ImageUtil.getBestFitImage(images, holder.mImageAlbumCover.getWidth(), holder.mImageAlbumCover.getHeight());
            Log.d(TAG, String.valueOf(holder.mImageAlbumCover.getWidth()) + ", " + String.valueOf(holder.mImageAlbumCover.getHeight()));
            String url = image.getUrl();
            Picasso.with(mContext).load(url).fit().centerCrop().into(holder.mImageAlbumCover);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
//        return mDataset.length;
        return mAlbums.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextAlbumName;
        public ImageView mImageAlbumCover;

        public ViewHolder(View rootView) {
            super(rootView);
//            mTextView = rootView;


        }
    }
}
