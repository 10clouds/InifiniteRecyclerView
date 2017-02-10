package com.tenclouds.loadingadaptersample;

import com.android.annotations.Nullable;
import com.tenclouds.infiniterecyclerview.ItemsLoader;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class SearchCardsLoader implements ItemsLoader<Card> {
    private static final int PAGE_SIZE = 20;
    private int pageNo = 1;
    private final String searchQuery;
    private ShowErrorInterface showErrorInterface;

    public SearchCardsLoader(@Nullable String searchQuery, ShowErrorInterface showErrorInterface) {
        this.searchQuery = searchQuery;
        this.showErrorInterface = showErrorInterface;
    }

    @Override
    public List<Card> getNewItems() {
        ArrayList<String> filter = new ArrayList<>();
        filter.add("pageSize=" + PAGE_SIZE);
        filter.add("page=" + pageNo);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            filter.add("name=" + searchQuery);
        }

        try {
            List<Card> cardsPage = CardAPI.getAllCards(filter);
            ++pageNo;
            return cardsPage;
        } catch (Exception e){
            e.printStackTrace();
            showErrorInterface.showError(e.getLocalizedMessage());
            return null;
        }
    }

    public interface ShowErrorInterface {
        void showError(String errorText);
    }

}
