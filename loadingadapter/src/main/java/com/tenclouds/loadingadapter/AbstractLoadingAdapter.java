package com.tenclouds.loadingadapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_LOADER = -1;
    private static final int VIEWTYPE_EMPTY_VIEW = -2;
    private boolean isLoading;
    private ArrayList<T> items;
    private LayoutInflater inflater;
    private AbstractItemsLoader<T> itemsLoader;
    private Context context;
    private int emptyStateView;
    private boolean autoLoadingEnabled = true;

    public AbstractLoadingAdapter(Context context, @LayoutRes int emptyStateView, @NonNull AbstractItemsLoader<T> itemsLoader) {
        this.context = context;
        this.emptyStateView = emptyStateView;
        this.itemsLoader = itemsLoader;

        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    public void setItemsLoader(AbstractItemsLoader<T> itemsLoader) {
        items.clear();
        this.itemsLoader = itemsLoader;
        notifyDataSetChanged();
        loadNewItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {
            View view = inflater.inflate(R.layout.loader_item_layout, viewGroup, false);
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
        if (!(viewHolder instanceof AbstractLoadingAdapter.LoaderViewHolder) && !(viewHolder instanceof AbstractLoadingAdapter.EmptyStateViewHolder)) {
            bindYourViewHolder(viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (items == null || items.size() == 0) {
            return 1;
        }

        if (isLoading) {
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
        if (isLoading && (items.isEmpty() || position == getItemCount() - 1)) {
            return VIEWTYPE_LOADER;
        } else if (items.size() == 0) {
            return VIEWTYPE_EMPTY_VIEW;
        } else {
            return getYourItemViewType(position);
        }
    }

    public void add(List<T> newItems) {
        if (newItems != null && newItems.size() > 0) {
            int oldItemsSize = items.size();
            items.addAll(newItems);
            notifyItemRangeInserted(oldItemsSize, newItems.size());
        }
    }

    public abstract long getYourItemId(int position);

    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent, int viewType);

    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract int getYourItemViewType(int position);

    protected LayoutInflater getInflater() {
        return inflater;
    }

    void loadNewItems() {
        if (autoLoadingEnabled) {
            setLoading(true);
            AsyncTask.execute(() -> {
                try {
                    final List<T> newItems = itemsLoader.getNewItems();
                    ((Activity) context).runOnUiThread(() -> {
                        setLoading(false);
                        if (newItems != null)
                            add(newItems);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(() -> setLoading(false));
                }
            });
        }
    }

    boolean isLoading() {
        return isLoading;
    }

    @SuppressWarnings("WeakerAccess")
    public T getItem(int position) {
        return items.get(position);
    }

    private void setLoading(boolean status) {
        if (status != this.isLoading) {
            isLoading = status;
            notifyItemChanged(items.size());
        }
    }

    private class LoaderViewHolder extends RecyclerView.ViewHolder {
        LoaderViewHolder(View view) {
            super(view);
        }
    }

    private class EmptyStateViewHolder extends RecyclerView.ViewHolder {
        EmptyStateViewHolder(View view) {
            super(view);
        }
    }
}

