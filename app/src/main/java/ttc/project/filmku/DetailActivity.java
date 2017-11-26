package ttc.project.filmku;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView tvNameDetail;
    private Button btSearch;
    String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("DETAIL");
        tvNameDetail = (TextView) findViewById(R.id.tvNameDetail);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, movieTitle);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getStringExtra("film_title")!=null){
                movieTitle = intent.getStringExtra("film_title");
                tvNameDetail.setText(movieTitle);
            }
        }
    }
}
