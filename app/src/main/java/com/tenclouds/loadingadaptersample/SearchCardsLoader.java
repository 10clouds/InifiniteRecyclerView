package com.tenclouds.loadingadaptersample;

import com.tenclouds.loadingadapter.AbstractItemsLoader;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class SearchCardsLoader implements AbstractItemsLoader<Card> {
    private int pageNo = 1;
    private final String cardName;

    public SearchCardsLoader(String cardName){
        this.cardName = cardName;
    }

    @Override
    public List<Card> getNewItems() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("pageSize=20");
        filter.add("page=" + pageNo);
        filter.add("name=" + cardName);
        List<Card> cardsPage = CardAPI.getAllCards(filter);
        ++pageNo;
        return cardsPage;
    }
}
