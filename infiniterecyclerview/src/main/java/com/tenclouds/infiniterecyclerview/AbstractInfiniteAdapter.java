package com.tenclouds.infiniterecyclerview;

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

/**
 * RecyclerView adapter for use with {@link com.tenclouds.infiniterecyclerview.InfiniteRecyclerView}. It has to be extended to be used in a project.
 * @param <T> Type of the item that this instance of adapter can hold.
 */
public abstract class AbstractInfiniteAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_LOADER = -1;
    private static final int VIEWTYPE_EMPTY_VIEW = -2;
    private boolean isLoading;
    private ArrayList<T> items;
    private LayoutInflater inflater;
    private ItemsLoader<T> itemsLoader;
    private Context context;
    private int emptyStateView;
    private boolean autoLoadingEnabled = true;

    /**
     * @param context Adapter's context
     * @param emptyStateView Id of a layout to be shown when this adapter doesn't hold any items
     * @param itemsLoader Implementation of {@link ItemsLoader} used for loading new items into the adapter
     */
    public AbstractInfiniteAdapter(Context context, @LayoutRes int emptyStateView, @NonNull ItemsLoader<T> itemsLoader) {
        this.context = context;
        this.emptyStateView = emptyStateView;
        this.itemsLoader = itemsLoader;

        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    /**
     * Method for setting new items loader, will remove all the old items from the adapter and start loading the new items.
     * @param itemsLoader loader used to load new items
     */
    public void replaceItemsLoader(ItemsLoader<T> itemsLoader) {
        items.clear();
        this.itemsLoader = itemsLoader;
        notifyDataSetChanged();
        loadNewItems();
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {
            View view = inflater.inflate(R.layout.loader_item_layout, viewGroup, false);
            return new LoaderViewHolder(view);
        } else if (viewType == VIEWTYPE_EMPTY_VIEW) {
            View view = inflater.inflate(emptyStateView, viewGroup, false);
            return new EmptyStateViewHolder(view);
        } else {
            return createRecyclerItemViewHolder(viewGroup, viewType);
        }
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!(viewHolder instanceof AbstractInfiniteAdapter.LoaderViewHolder) && !(viewHolder instanceof AbstractInfiniteAdapter.EmptyStateViewHolder)) {
            bindRecyclerViewHolder(viewHolder, position);
        }
    }

    @Override
    final public int getItemCount() {
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
    final public long getItemId(int position) {
        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            // id of loader is considered as -1 here
            return 0;
        }
        return getRecyclerItemId(position);
    }

    @Override
    final public int getItemViewType(int position) {
        if (isLoading && (items.isEmpty() || position == getItemCount() - 1)) {
            return VIEWTYPE_LOADER;
        } else if (items.size() == 0) {
            return VIEWTYPE_EMPTY_VIEW;
        } else {
            return getRecyclerItemViewType(position);
        }
    }

    /**
     * Return the stable ID for the item at position.
     * @param position Position of the item
     * @return Id of a view shown in recycler view.
     */
    protected abstract long getRecyclerItemId(int position);

    /**
     * Creates view holder for views shown in recycler view
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View, returned by {@link #getRecyclerItemViewType}
     * @return Custom recycler view, extending RecyclerView.ViewHolder
     */
    protected abstract RecyclerView.ViewHolder createRecyclerItemViewHolder(ViewGroup parent, int viewType);

    /**
     *
     * @param holder
     * @param position
     */
    protected abstract void bindRecyclerViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Return item type of recyclerview view. Can be used to return different view types for different types of items.
     * @param position position to query
     * @return Item view type, -1 and -2 are reserved for loading view and empty list view
     */
    protected abstract int getRecyclerItemViewType(int position);

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
                            addNewItems(newItems);
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

    private void addNewItems(List<T> newItems) {
        if (newItems != null && newItems.size() > 0) {
            int oldItemsSize = items.size();
            items.addAll(newItems);
            notifyItemRangeInserted(oldItemsSize, newItems.size());
        }
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

