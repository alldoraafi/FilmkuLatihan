package ttc.project.filmku;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private double rating;
    private String movie_id, title, overview, language, release, image, homepage, tagline, poster, movie_key;
    private ArrayList<String> genres=new ArrayList<>(), reviews=new ArrayList<>(), reviewers=new ArrayList<>();
    private TextView tvOverview, tvLanguage, tvGenres, tvRelease, tvTagline;
    private ImageView ivBack;
    private RatingBar rbRating;
    private Button btHomepage, btTrailer;
    private RecyclerView rvReviews;
    private ReviewAdapter adapter;
    private MaterialFavoriteButton fav;
    private View rootView;

    private SQLiteDatabase mDb;
    private DatabaseHelper dbHelper;

    private void getData(String jsonData){
        try {
            JSONObject film_item = new JSONObject(jsonData);
            Uri uri = NetworkUtils.buildImageUrl();
            overview = film_item.getString("overview");
            image = uri.toString()+film_item.getString("backdrop_path");
            language= film_item.getString("original_language");
            release = film_item.getString("release_date");
            homepage = film_item.getString("homepage");
            tagline = film_item.getString("tagline");
            rating = film_item.getDouble("vote_average")/2;
            poster = uri.toString()+film_item.getString("poster_path");

            JSONArray genresJsonArray = film_item.getJSONArray("genres");
            for (int i=0; i<genresJsonArray.length(); i++){
                genres.add(genresJsonArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getReviews(String jsonData){
        try {
            JSONObject dataJson = new JSONObject(jsonData);
            JSONArray reviewsJsonArray = dataJson.getJSONArray("results");
            for (int i=0; i<reviewsJsonArray.length(); i++){
                JSONObject review_item = reviewsJsonArray.getJSONObject(i);
                String review = review_item.getString("content");
                String reviewer = review_item.getString("author");
                reviews.add(review);
                reviewers.add(reviewer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFavorites(){
        Cursor mCursor = getAllFavorites();
        Log.d("MovieID", movie_id);
        for (int i=0; i<mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            int idColumnIndex = mCursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_ID);
            Log.d("FAV", mCursor.getString(idColumnIndex));
            if (mCursor.getString(idColumnIndex).equals(movie_id)){
                Log.d("FAV", "found match!");
                fav.setFavorite(true);
            }
        }
    }

    private String getVideos(String jsonData){
        try {
            JSONObject dataJson = new JSONObject(jsonData);
            JSONArray videosJsonArray = dataJson.getJSONArray("results");
            JSONObject video_item = videosJsonArray.getJSONObject(0);
                String video_key = video_item.getString("key");
                return video_key;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Cursor getAllFavorites(){
        Log.d("getFav", "run");
        return mDb.query(DatabaseContract.FavoritesEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d("Created", "OnCreate");
        title = getIntent().getStringExtra("film_title");
        movie_id = getIntent().getStringExtra("movie_id");

        getSupportActionBar().setTitle(title);

        dbHelper = new DatabaseHelper(this);
        mDb = dbHelper.getWritableDatabase();

        rootView = findViewById(R.id.rootView);

        tvOverview = findViewById(R.id.tvOverview);
        tvOverview.setMovementMethod(new ScrollingMovementMethod());
        tvGenres = findViewById(R.id.tvGenres);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvRelease = findViewById(R.id.tvRelease);
        tvTagline = findViewById(R.id.tvTagline);
        ivBack = findViewById(R.id.ivBack);
        rbRating  =findViewById(R.id.rbRating);

        btHomepage = findViewById(R.id.btHomepage);
        btHomepage.setOnClickListener(this);
        btTrailer = findViewById((R.id.btTrailer));
        btTrailer.setOnClickListener(this);

        fav = findViewById(R.id.fav);
        fav.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseContract.FavoritesEntry.COLUMN_TITLE, title);
                    contentValues.put(DatabaseContract.FavoritesEntry.COLUMN_ID, movie_id);
                    contentValues.put(DatabaseContract.FavoritesEntry.COLUMN_POSTER, poster);
                    mDb.insert(DatabaseContract.FavoritesEntry.TABLE_NAME, null, contentValues);
                    Snackbar snackbar = Snackbar.make(rootView, "Added to Favorites", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else {
                    mDb.delete(DatabaseContract.FavoritesEntry.TABLE_NAME, DatabaseContract.FavoritesEntry.COLUMN_ID+"=?", new String[] {movie_id});
                    Snackbar snackbar = Snackbar.make(rootView, "Removed from Favorites", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        rvReviews = findViewById(R.id.rvReviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ReviewAdapter(this);
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setAdapter(adapter);

        getFavorites();
        Uri uri = NetworkUtils.buildMovieUrl(movie_id);
        new NetworkTask(this, 1).execute(uri);
        uri = NetworkUtils.buildReviewsUrl(movie_id);
        new NetworkTask(this, 2).execute(uri);
        uri = NetworkUtils.buildVideosUrl(movie_id);
        new NetworkTask(this, 3).execute(uri);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if (view.getId()==R.id.btHomepage) {
            intent.setData(Uri.parse(homepage));
        }else {
            intent.setData(Uri.parse(NetworkUtils.buildTrailerUrl(movie_key).toString()));
        }
        startActivity(intent);
    }


    class NetworkTask extends AsyncTask<Uri, Void, String> {
        private Context context;
        private int i;

        public NetworkTask(Context context, int i) {
            this.context = context;
            this.i =i;
        }

        @Override
        protected String doInBackground(Uri... uris) {
            URL url = null;
            try {
                url = new URL(uris[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url!=null) try {
                return NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (i){
                case 1:
                    getData(s);
                    tvOverview.setText(overview);
                    tvLanguage.setText(language);
                    tvRelease.setText(release);
                    String genre = "";
                    for (String i:genres){
                        if (genre=="")genre+=i;
                        else genre+=", "+i;
                    }
                    tvGenres.setText(genre);
                    if (!tagline.isEmpty())
                        tvTagline.setText("\""+tagline+"\"");
                    else tvTagline.setVisibility(View.GONE);

                    rbRating.setRating((float)rating);
                    Picasso.with(context).load(image).placeholder(R.drawable.loading).into(ivBack);
                    break;
                case 2:
                    getReviews(s);
                    adapter.swapData(reviews, reviewers);
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    movie_key = getVideos(s);
            }
        }
    }
}
