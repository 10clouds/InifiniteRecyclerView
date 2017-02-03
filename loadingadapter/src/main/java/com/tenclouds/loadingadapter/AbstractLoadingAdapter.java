package com.tenclouds.loadingadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_LOADER = -1;
    private static final int VIEWTYPE_EMPTY_VIEW = -2;
    protected boolean showLoader;
    protected ArrayList<T> items;
    protected LayoutInflater inflater;
    protected Context context;
    private int emptyStateView;

    public AbstractLoadingAdapter(Context context, @LayoutRes int emptyStateView) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
        this.emptyStateView = emptyStateView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {

            // Your Loader XML view here
            View view = inflater.inflate(R.layout.loader_item_layout, viewGroup, false);

            // Your LoaderViewHolder class
            return new LoaderViewHolder(view);

        } else if (viewType == VIEWTYPE_EMPTY_VIEW) {
            View view = inflater.inflate(emptyStateView, viewGroup, false);
            return new EmptyStateViewHolder(view);
        } else {
            return getYourItemViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AbstractLoadingAdapter.LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) viewHolder;
            if (showLoader) {
                loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressBar.setVisibility(View.GONE);
            }

            return;
        } else if (viewHolder instanceof AbstractLoadingAdapter.EmptyStateViewHolder) {

        } else {
            bindYourViewHolder(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (items == null || items.size() == 0) {
            return 1;
        }
        if (showLoader) {
            return items.size() + 1;
        } else {
            return items.size();
        }
    }

    @Override
    public long getItemId(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            // id of loader is considered as -1 here
            return 0;
        }
        return getYourItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoader && (items.isEmpty() || position == getItemCount() - 1)) {
            return VIEWTYPE_LOADER;
        } else if (items.size() == 0) {
            return VIEWTYPE_EMPTY_VIEW;
        } else {
            return getYourItemViewType(position);
        }
    }

    public void add(List<T> newItems) {
        int oldItemsSize = items.size();
        items.addAll(newItems);
        notifyItemRangeChanged(oldItemsSize, oldItemsSize + newItems.size());
    }

    public void showLoading(boolean status) {
        showLoader = status;
        notifyDataSetChanged();
    }

    public void setEmptyStateMessage(String message) {
        notifyDataSetChanged();
    }

    public void setEmptyStateIcon(int iconId) {
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public abstract long getYourItemId(int position);

    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent, int viewType);

    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract int getYourItemViewType(int position);

    private class LoaderViewHolder extends RecyclerView.ViewHolder {
        public View mProgressBar;

        public LoaderViewHolder(View view) {
            super(view);
            mProgressBar = view;
        }
    }

    private class EmptyStateViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public EmptyStateViewHolder(View view) {
            super(view);
            mView = view;
        }
    }
}

