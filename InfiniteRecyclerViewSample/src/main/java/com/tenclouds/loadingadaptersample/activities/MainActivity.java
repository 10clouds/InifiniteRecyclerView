package com.tenclouds.loadingadaptersample.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tenclouds.infiniterecyclerview.InfiniteRecyclerView;
import com.tenclouds.loadingadaptersample.MtgCardsInfiniteAdapter;
import com.tenclouds.loadingadaptersample.R;
import com.tenclouds.loadingadaptersample.SearchCardsLoader;

import io.magicthegathering.javasdk.resource.Card;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, MtgCardsInfiniteAdapter.ItemSelectedListener, SearchCardsLoader.ShowErrorInterface {
    private MtgCardsInfiniteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InfiniteRecyclerView recyclerView = (InfiniteRecyclerView) findViewById(R.id.recycler);
        adapter = new MtgCardsInfiniteAdapter(this, new SearchCardsLoader(null, this), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.replaceItemsLoader(new SearchCardsLoader(query, this));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        adapter.replaceItemsLoader(new SearchCardsLoader("", this));
        return true;
    }

    @Override
    public void onItemSelected(Card card) {
        CardDetailActivity.start(this, card);
    }

    @Override
    public void showError(String errorText) {
        runOnUiThread(() -> Toast.makeText(this, errorText, Toast.LENGTH_LONG).show());
    }

}
