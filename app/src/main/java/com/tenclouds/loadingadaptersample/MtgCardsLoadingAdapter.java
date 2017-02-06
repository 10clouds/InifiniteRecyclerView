package com.tenclouds.loadingadaptersample;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tenclouds.loadingadapter.AbstractItemsLoader;
import com.tenclouds.loadingadapter.views.AbstractLoadingAdapter;
import com.tenclouds.loadingadaptersample.databinding.ViewCardBinding;

import io.magicthegathering.javasdk.resource.Card;


public class MtgCardsLoadingAdapter extends AbstractLoadingAdapter<Card> {

    public MtgCardsLoadingAdapter(Context context, AbstractItemsLoader<Card> itemsLoader) {
        super(context, R.layout.view_empty, itemsLoader);
    }

    @Override
    public long getYourItemId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent, int viewType) {
        ViewCardBinding binding = DataBindingUtil.inflate(getInflater(), R.layout.view_card, parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        Card item = getItem(position);
        ((CardViewHolder)holder).setCard(item);
    }

    @Override
    public int getYourItemViewType(int position) {
        return 0;
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {
        private ViewCardBinding binding;

        public CardViewHolder(ViewCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setCard(Card card){
            binding.setCard(card);
        }
    }
}
