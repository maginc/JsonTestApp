package com.ragazm.jsontest1;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Andris on 005 05.11.17.
 */

public abstract class PageScrollListener extends RecyclerView.OnScrollListener{


    LinearLayoutManager layoutManager;


    public PageScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        Log.d("visible item count:", String.valueOf(visibleItemCount));
        Log.d("totalitemcount:", String.valueOf(totalItemCount));
        Log.d("first visible position:", String.valueOf(firstVisibleItemPosition));

       if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {

                loadMoreItems();
            }

        }


    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}



