package com.tenclouds.infiniterecyclerview;

import java.util.List;

/**
 * Interface for providing items for {@link AbstractEndlessAdapter}.
 * @param <T> Type of the items returned from this loader
 */
public interface ItemsLoader<T> {
   /**
    * Method for fetching new items for {@link AbstractEndlessAdapter}
    * @return List containing new items
     */
   List<T> getNewItems();
}
