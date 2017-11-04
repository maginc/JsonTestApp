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


}
