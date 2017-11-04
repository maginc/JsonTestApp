package com.ragazm.jsontest1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ragazm.jsontest1.json.APIClient;
import com.ragazm.jsontest1.json.JSONResponse;
import com.ragazm.jsontest1.json.MovieDetails;
import com.ragazm.jsontest1.json.RequestInterface;
import com.ragazm.jsontest1.recyclerView.DataAdapter;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {

    private String title;
    private String imageUrl;
    private String year;
    private String imdbId;
    RequestInterface requestInterface;
    private String plot;
    private static final String API_KEY = "dc6b8a0";
    TextView textPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        requestInterface = APIClient.getClient().create(RequestInterface.class);


        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        imageUrl = extras.getString("imageUrl");
        year = extras.getString("year");
        imdbId = extras.getString("imdbId");


        //load image
        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute(imageUrl);

        TextView textYear = findViewById(R.id.txtYear);
        TextView textView = findViewById(R.id.txtDescription);
        textPlot = findViewById(R.id.txtPlot);
        textView.setText(title);
        textYear.setText(year);

        loadDetailsJSON();

    }

    //Image loading method
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void loadDetailsJSON() {
        Call<MovieDetails> call = requestInterface.getDetails(imdbId, API_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {

                MovieDetails jsonResponse = response.body();

                if (((response.isSuccessful()))) {

                    plot = jsonResponse.getPlot();
                    textPlot.setText(plot);

                }else {
                   textPlot.setText("");
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
