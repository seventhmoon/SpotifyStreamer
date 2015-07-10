package idv.seventhmoon.spotifystreamer;

import com.spotify.api.model.Image;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by fung.lam on 10/07/2015.
 */
public class ImageUtil {

    public static Image getBestFitImage(List<Image> images, int width, int height) {
        TreeSet<Image> sortedImages = new TreeSet<>(images);

        Image bestImage = null;

        for (Image image : sortedImages) {
            bestImage = image;
            if (image.getHeight() >= height && image.getWidth() >= width) {
                break;
            }
        }
        return bestImage;
    }
}
