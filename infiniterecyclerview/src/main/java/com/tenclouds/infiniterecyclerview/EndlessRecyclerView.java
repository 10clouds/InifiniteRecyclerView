package com.tenclouds.infiniterecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Recycler view to be used with {@link AbstractEndlessAdapter}, automatically loads new items when user scrolls to the end of the list
 */
public class EndlessRecyclerView extends RecyclerView {
    public EndlessRecyclerView(Context context) {
        super(context);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof AbstractEndlessAdapter) {
            super.setAdapter(adapter);
            setScrollListener((AbstractEndlessAdapter) adapter);
        } else {
            throw new IllegalStateException("Adapter ");
        }
    }

    private void setScrollListener(final AbstractEndlessAdapter adapter) {
        if(getLayoutManager() instanceof LinearLayoutManager) {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    int visibleItemCount = EndlessRecyclerView.this.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    boolean loading = adapter.isLoading();

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + 5)) {
                        post(adapter::loadNewItems);
                    }
                }
            });
        } else {
            throw new IllegalStateException("LayoutManager is not instance of LinearLayoutManager. LoadingRecyclerView only supports LinearLayoutManager.");
        }
    }
}
