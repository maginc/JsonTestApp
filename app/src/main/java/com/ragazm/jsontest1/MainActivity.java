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

import com.ragazm.jsontest1.json.APIClient;
import com.ragazm.jsontest1.json.JSONResponse;
import com.ragazm.jsontest1.json.RequestInterface;
import com.ragazm.jsontest1.recyclerView.DataAdapter;
import com.ragazm.jsontest1.recyclerView.MyClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andris on 002 02.11.17.
 *
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Movie> data;
    private DataAdapter adapter;
   // Confidential information such as private api key cant be shared in public lol :)
    private static final String API_KEY = "dc6b8a0";
    private String keyWord;
    private Context context;
    RequestInterface requestInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //Bind http client with our call interface
        requestInterface = APIClient.getClient().create(RequestInterface.class);



        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.addOnItemTouchListener(
                new MyClickListener(context, recyclerView, new MyClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        String imdbId = data.get(position).getImdbID();

                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("imdbId", imdbId);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


    }
        //Option menu for search field
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

 // Load Json data from server using Retrofit
    private void loadJSON() {
        Call<JSONResponse> call = requestInterface.getSearch(keyWord, API_KEY);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                if (((jsonResponse.response.equals("True")) && (response.isSuccessful()))) {


                        data = jsonResponse.data;
                        adapter = new DataAdapter(data);
                        recyclerView.setAdapter(adapter);
                    }else {
                    Toast.makeText(getApplicationContext(), "Nothing found, try again!", Toast.LENGTH_SHORT).show();
                }
                }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }



}


