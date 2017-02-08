package com.tenclouds.loadingadaptersample;

import com.android.annotations.Nullable;
import com.tenclouds.loadingadapter.AbstractItemsLoader;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class SearchCardsLoader implements AbstractItemsLoader<Card> {
    private static final int PAGE_SIZE = 20;
    private int pageNo = 1;
    private final String searchQuery;

    public SearchCardsLoader(@Nullable String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public List<Card> getNewItems() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("pageSize=" + PAGE_SIZE);
        filter.add("page=" + pageNo);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            filter.add("name=" + searchQuery);
        }

        List<Card> cardsPage = CardAPI.getAllCards(filter);
        ++pageNo;
        return cardsPage;
    }

}
