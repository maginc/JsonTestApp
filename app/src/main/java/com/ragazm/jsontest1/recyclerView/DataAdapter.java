package com.ragazm.jsontest1.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ragazm.jsontest1.Movie;
import com.ragazm.jsontest1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andris on 002 02.11.17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<Movie> movies;
    private boolean isLoadingAdded = false;


    public DataAdapter(List<Movie> movies) {
        this.movies = movies;

    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {

        holder.txtTitle.setText(movies.get(position).getTitle());
        holder.txtYear.setText(movies.get(position).getYear());


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitle;
        private TextView txtYear;




        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtYear = itemView.findViewById(R.id.txtYear);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public void add(Movie r) {
        movies.add(r);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movies.indexOf(r);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

}
