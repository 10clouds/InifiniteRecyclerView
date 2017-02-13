package com.tenclouds.infiniterecyclerview;

import java.util.List;

/**
 * Interface for providing items for {@link AbstractInfiniteAdapter}.
 * @param <T> Type of the items returned from this loader
 */
public interface ItemsLoader<T> {
   /**
    * Method for fetching new items for {@link AbstractInfiniteAdapter}
    * @return List containing new items
     */
   List<T> getNewItems();
}
