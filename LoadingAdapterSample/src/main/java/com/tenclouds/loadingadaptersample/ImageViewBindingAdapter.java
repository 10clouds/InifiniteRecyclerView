package com.tenclouds.loadingadaptersample;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViewBindingAdapter {
    @BindingAdapter("imageSrc")
    public static void setImageSource(ImageView imageView, String imageSrc){
        Glide.with(imageView.getContext()).load(imageSrc).into(imageView);
    }
}