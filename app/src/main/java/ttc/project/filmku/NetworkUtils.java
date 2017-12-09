package ttc.project.filmku;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Fikry-PC on 11/23/2017.
 */

public class NetworkUtils {

    //    TODO (11) buat variable TMDB base url sebagai base URL
    //    TODO (12) buat variable TMDB api key untuk menyimpan api key mu
    //    TODO (13) buat method untuk membangun URL request popular movies
    //    TODO (14) buat URI sesuai dokumentasi TMDB
    //    TODO (15) buat URL dari URI yang telah dibuat
    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    public static final String API_KEY = "6b1acd931bdbb181b6f88aae70453648";

    public static Uri buildMovieListUrl(String path){
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(path)
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }

    /*public static Uri buildDiscoverMovieUrl(){
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }*/

    public static Uri buildImageUrl(){
        Uri uri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .build();
        return uri;
    }

    public static Uri buildMovieUrl(String movie_id){
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movie_id)
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
