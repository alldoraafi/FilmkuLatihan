package ttc.project.filmku;

import android.content.Context;
import android.net.ConnectivityManager;
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
    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    public static final String API_KEY = "6b1acd931bdbb181b6f88aae70453648";
    public static final String BASE_TRAILER_URL = "https://www.youtube.com/";

    public static Uri buildMovieListUrl(String path){
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(path)
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }

    public static Uri buildReviewsUrl(String movie_id){
        Uri uri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(movie_id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }

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

    public static Uri buildVideosUrl(String id){
        Uri uri= Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter("api_key", API_KEY)
                .build();
        return uri;
    }

    public static Uri buildTrailerUrl(String key){
        Uri uri= Uri.parse(BASE_TRAILER_URL).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", key)
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}