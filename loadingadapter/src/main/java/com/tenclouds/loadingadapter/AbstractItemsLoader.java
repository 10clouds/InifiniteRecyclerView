package com.tenclouds.loadingadapter;

import java.util.List;

/**
 * Interface for providing items for {@link AbstractLoadingAdapter}.
 * @param <T> Type of the items returned from this loader
 */
public interface AbstractItemsLoader<T> {
   /**
    * Method for fetching new items for {@link AbstractLoadingAdapter}
    * @return List containing new items
     */
   List<T> getNewItems();
}
