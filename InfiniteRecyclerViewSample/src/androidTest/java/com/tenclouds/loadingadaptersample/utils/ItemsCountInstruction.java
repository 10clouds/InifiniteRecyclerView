package com.tenclouds.loadingadaptersample.utils;

import android.support.v7.widget.RecyclerView;

import com.azimolabs.conditionwatcher.Instruction;

public class ItemsCountInstruction extends Instruction {
    private final RecyclerView.Adapter adapter;
    private final int childrenCount;

    public ItemsCountInstruction(RecyclerView.Adapter adapter, int childrenCount){
        super();

        this.adapter = adapter;
        this.childrenCount = childrenCount;
    }

    @Override
    public String getDescription() {
        return String.format("Adapter %s should have %d items. Actual count was %d.", adapter.toString(), childrenCount, adapter.getItemCount());
    }

    @Override
    public boolean checkCondition() {
        return adapter.getItemCount() == childrenCount;
    }
}
