package com.tenclouds.loadingadapter;

import java.util.List;

public interface AbstractItemsLoader<T> {
   List<T> getNewItems();
}
