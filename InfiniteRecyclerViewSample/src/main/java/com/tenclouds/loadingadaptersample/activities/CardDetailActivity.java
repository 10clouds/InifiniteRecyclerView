package com.tenclouds.loadingadaptersample.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.tenclouds.loadingadaptersample.R;
import com.tenclouds.loadingadaptersample.databinding.ActivityCardDetailBinding;

import io.magicthegathering.javasdk.resource.Card;

public class CardDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CARD = "card";

    public static void start(Context context, @NonNull Card card) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra(EXTRA_CARD, card);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCardDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_card_detail);
        Card card = (Card) getIntent().getSerializableExtra(EXTRA_CARD);
        binding.setCard(card);

        getSupportActionBar().setTitle(card.getName());
    }
}
