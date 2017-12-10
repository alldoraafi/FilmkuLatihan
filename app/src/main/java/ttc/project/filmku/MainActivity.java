package ttc.project.filmku;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG_LIFECYCLE = "Lifecycle";
    private RecyclerView rvFilm;
    ArrayList<String> titles = new ArrayList<>(), poster = new ArrayList<>(), movieID = new ArrayList<>(), favoritesID = new ArrayList<>();
    private FilmAdapter adapter;
    private MenuItem menuTitle;
    private SQLiteDatabase mDb;
    private DatabaseHelper dbHelper;
    private String path;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menuTitle = menu.findItem(R.id.menuTitle);
        if (NetworkUtils.isNetworkAvailable(this))menuTitle.setTitle("Popular Movies");
        else menuTitle.setTitle("Favorites");
        return true;
    }

    private void getData(String jsonData){
        try {
            JSONObject dataJson = new JSONObject(jsonData);
            JSONArray titlesJsonArray = dataJson.getJSONArray("results");
            for(int i=0; i<titlesJsonArray.length(); i++){
                JSONObject film_item = titlesJsonArray.getJSONObject(i);
                Uri uri = NetworkUtils.buildImageUrl();
                String title = film_item.getString("title");
                String image = uri.toString()+film_item.getString("poster_path");
                String movie_id = film_item.getString("id");
                titles.add(title);
                poster.add(image);
                movieID.add(movie_id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFavorites(){
        Cursor mCursor = getAllFavorites();
        titles.clear();movieID.clear();poster.clear();favoritesID.clear();
        for(int i = 0; i<mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            int titleColumnIndex = mCursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_TITLE);
            int idColumnIndex = mCursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_ID);
            int posterColumnIndex = mCursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_POSTER);

            titles.add(mCursor.getString(titleColumnIndex));
            movieID.add(mCursor.getString(idColumnIndex));
            poster.add(mCursor.getString(posterColumnIndex));
            favoritesID.add(mCursor.getString(idColumnIndex));
        }
        adapter.swapData(titles, poster, movieID, favoritesID);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!NetworkUtils.isNetworkAvailable(this)){
            Toast.makeText(this, "No Internet, can't get list", Toast.LENGTH_SHORT);
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()){
            case R.id.getPopular:
                menuTitle.setTitle("Popular Movies");
                path = "popular";
                setContent(path);
                break;
            case R.id.getNowPlaying:
                menuTitle.setTitle("Now Playing");
                path = "now_playing";
                setContent(path);
                break;
            case R.id.getTopRated:
                menuTitle.setTitle("Top Rated");
                path = "top_rated";
                setContent(path);
                break;
            case R.id.getUpcoming:
                menuTitle.setTitle("Upcoming Movies");
                path = "upcoming";
                setContent(path);
                break;
            case R.id.getFavorites:
                menuTitle.setTitle("Favorites");
                path = "favorites";
                getFavorites();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setContent(String path){
        Uri uri = NetworkUtils.buildMovieListUrl(path);
        new NetworkTask().execute(uri);
    }

    class NetworkTask extends AsyncTask<Uri, Void, String>{
        @Override
        protected String doInBackground(Uri... uris) {
            URL popularURL = null;
            try {
                popularURL = new URL(uris[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (popularURL!=null) try {
                return NetworkUtils.getResponseFromHttpUrl(popularURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            titles = new ArrayList<>();
            poster = new ArrayList<>();
            movieID = new ArrayList<>();
            getData(s);
            adapter.swapData(titles, poster, movieID, favoritesID);
            adapter.notifyDataSetChanged();
        }
    }

    public Cursor getAllFavorites(){
        return mDb.query(DatabaseContract.FavoritesEntry.TABLE_NAME, null, null, null, null, null, null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG_LIFECYCLE, "OnCreate");

        dbHelper = new DatabaseHelper(this);
        mDb = dbHelper.getReadableDatabase();

        rvFilm = (RecyclerView) findViewById(R.id.rvFilm);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new FilmAdapter(this);
        rvFilm.setLayoutManager(layoutManager);
        rvFilm.setAdapter(adapter);

        getFavorites();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_LIFECYCLE, "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtils.isNetworkAvailable(this)){
            if (path==null)setContent("popular");
            else if (path.equals("favorites")) getFavorites();
            else setContent(path);
        }else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG_LIFECYCLE, "OnResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG_LIFECYCLE, "OnRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_LIFECYCLE, "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LIFECYCLE, "OnDestroy");
    }
}
