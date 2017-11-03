package com.ragazm.jsontest1;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andris on 002 02.11.17.
 *
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Movie> data;
    private DataAdapter adapter;
    private static final String BASE_URL = "http://www.omdbapi.com";
    private static final String API_KEY = "dc6b8a0";
    private String keyWord;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.addOnItemTouchListener(
                new MyClickListener(context, recyclerView, new MyClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String title = data.get(position).getTitle();
                        String imageUrl = data.get(position).getPoster();
                        String year = data.get(position).getYear();
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("imageUrl", imageUrl);
                        intent.putExtra("year", year);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                keyWord = query;
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                loadJSON();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }


    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON(keyWord, API_KEY);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if ((jsonResponse.getMovies() != null) && (response.isSuccessful())) {

                        data = new ArrayList<>(Arrays.asList(jsonResponse.getMovies()));

                        adapter = new DataAdapter(data);
                        recyclerView.setAdapter(adapter);
                    }else { Toast.makeText(getApplicationContext(), "Nothing found, try again!", Toast.LENGTH_SHORT).show();}
                }


            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }



}


