package ttc.project.filmku;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private String overview, language, release, image, homepage, tagline;
    private ArrayList<String> genres;
    private TextView tvFilmName, tvOverview, tvLanguage, tvGenres, tvRelease, tvRating, tvTagline;
    private ImageView ivBack;
    private Button btHomepage;

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle(getIntent().getStringExtra("film_title"));
        Log.d("title",getIntent().getStringExtra("film_title"));

        tvOverview = findViewById(R.id.tvOverview);
        tvOverview.setMovementMethod(new ScrollingMovementMethod());
        tvLanguage = findViewById(R.id.tvLanguage);
        tvRelease = findViewById(R.id.tvRelease);
        tvTagline = findViewById(R.id.tvTagline);
        ivBack = findViewById(R.id.ivBack);

        btHomepage = findViewById(R.id.btHomepage);
        btHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(homepage));
                startActivity(intent);
            }
        });

        Uri uri = NetworkUtils.buildMovieUrl(getIntent().getStringExtra("movie_id"));
        Log.d("id",getIntent().getStringExtra("movie_id"));
        new NetworkTask(this).execute(uri);
    }

    class NetworkTask extends AsyncTask<Uri, Void, String> {
        private Context context;

        public NetworkTask(Context context) {
            this.context = context;
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
            getData(s);
            tvOverview.setText(overview);
            tvLanguage.setText(language);
            tvRelease.setText(release);
            if (!tagline.isEmpty())
                tvTagline.setText("\""+tagline+"\"");
            else tvTagline.setVisibility(View.GONE);
            Picasso.with(context).load(image).placeholder(R.drawable.loading).into(ivBack);
        }
    }
}
