package com.ragazm.jsontest1;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
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
 * TODO fix bugs!!!  make nice loading bar
 *
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Movie> data;


    private DataAdapter adapter;
   // Confidential information such as private api key can't be shared in public lol :)
    private static final String API_KEY = "ENTER_YOUR_API_KEY_HERE";
    private String keyWord;
    private String page;
    private String totalResults;
    int pageCount;
    private Context context;
    RequestInterface requestInterface;
    LinearLayoutManager layoutManager;

    private boolean isLoading = false;
    private boolean isLastPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //Bind http client with our call interface
        requestInterface = APIClient.getClient().create(RequestInterface.class);


        pageCount = 0;



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //Default value
        page = "1";

        recyclerView.addOnScrollListener(new PageScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }

                }, 1000);


            }

            @Override
            public int getTotalPageCount() {
                return pageCount;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        recyclerView.addOnItemTouchListener(
                new MyClickListener(context, recyclerView, new MyClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String imdbId = data.get(position).getImdbID();
                        //Send IMDB ID to DetailActivity
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
                //reset when new search performed
                page = "1";
                data = null;

                loadJSON();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

 // Load Json data from server using Retrofit
    private void loadJSON() {

        Call<JSONResponse> call = requestInterface.getSearch(keyWord, API_KEY,page);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {


                JSONResponse jsonResponse = response.body();


                if (((jsonResponse.response.equals("True")) && (response.isSuccessful()))) {
                        //calculate how much pages of search results we got
                        pageCount = pageCount(jsonResponse.totalResults);
                    Log.d("ASDAPage count:", String.valueOf(pageCount));

                    List<Movie> cacheData = jsonResponse.data;
                    Log.d("ASDApage:", page);



                    if(page.equals("1")) {
                        data = jsonResponse.data;
                    }else {
                        data.addAll(cacheData);
                    }
                        adapter = new DataAdapter(data);
                        recyclerView.setAdapter(adapter);
                    page = String.valueOf(Integer.valueOf(page) + 1);

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
    //Counts how many pages needs to be downloaded to view all results
    //OMDB API allows 10 results per page, one page per request
    private Integer pageCount(String totalResults){
        int results;
        int reminder;
        reminder = (Integer.valueOf(totalResults)%10);
        results = (Integer.valueOf(totalResults)/10);
        if (reminder !=0){
            return results+1;
        }else{
            return results;
        }
    }

}


