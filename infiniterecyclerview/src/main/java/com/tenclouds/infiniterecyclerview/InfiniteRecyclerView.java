package com.tenclouds.infiniterecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Recycler view to be used with {@link AbstractInfiniteAdapter}, automatically loads new items when user scrolls to the end of the list
 */
public class InfiniteRecyclerView extends RecyclerView {
    public InfiniteRecyclerView(Context context) {
        super(context);
    }

    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfiniteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof AbstractInfiniteAdapter) {
            super.setAdapter(adapter);
            setScrollListener((AbstractInfiniteAdapter) adapter);
        } else {
            throw new IllegalStateException("Adapter ");
        }
    }

    private void setScrollListener(final AbstractInfiniteAdapter adapter) {
        if(getLayoutManager() instanceof LinearLayoutManager) {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    int visibleItemCount = InfiniteRecyclerView.this.getChildCount();
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
