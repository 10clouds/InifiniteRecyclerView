package com.tenclouds.loadingadaptersample;

import com.tenclouds.loadingadapter.AbstractItemsLoader;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class AllCardsLoader implements AbstractItemsLoader<Card> {
    private static final int PAGE_SIZE = 20;
    private int pageNo = 1;

    @Override
    public List<Card> getNewItems() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("pageSize=" + PAGE_SIZE);
        filter.add("page=" + pageNo);
        List<Card> cardsPage = CardAPI.getAllCards(filter);
        ++pageNo;
        return cardsPage;
    }

}
