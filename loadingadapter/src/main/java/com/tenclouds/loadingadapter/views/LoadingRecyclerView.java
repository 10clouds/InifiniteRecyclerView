package com.tenclouds.loadingadapter.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tenclouds.loadingadapter.EndlessRecyclerViewScrollListener;

public class LoadingRecyclerView extends RecyclerView {
    public LoadingRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LoadingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if(getLayoutManager() instanceof LinearLayoutManager) {
            addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {

                }

                @Override
                public void onScrolled(int dx, int dy) {

                }
            });
        } else {
            throw new IllegalStateException("LayoutManager is not instance of LinearLayoutManager. LoadingRecyclerView's only supports LinearLayoutManager.");
        }
    }
}
