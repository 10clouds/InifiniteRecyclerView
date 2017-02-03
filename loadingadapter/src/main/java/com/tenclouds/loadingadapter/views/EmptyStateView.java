package com.tenclouds.loadingadapter.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenclouds.loadingadapter.R;

public class EmptyStateView extends LinearLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public EmptyStateView(Context context) {
        super(context);
        init();
    }

    public EmptyStateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EmptyStateView,
                0, 0);

        try {
            setMessage(a.getString(R.styleable.EmptyStateView_emptyStateText));
            setIcon(a.getDrawable(R.styleable.EmptyStateView_emptyStateIcon));
        } finally {
            a.recycle();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_empty_state, this);

        mImageView = (ImageView) findViewById(R.id.view_empty_state_icon);
        mTextView = (TextView) findViewById(R.id.view_empty_state_message);
    }

    public void setIcon(int iconId) {
        mImageView.setImageResource(iconId);
    }

    public void setIcon(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
    }

    public void setIcon(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    public void setMessage(String message) {
        mTextView.setText(message);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void showAnimated() {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setVisibility(View.GONE);
        animate().setDuration(shortAnimTime).alpha(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setVisibility(View.VISIBLE);
                    }
                });
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void hideAnimated() {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setVisibility(View.VISIBLE);
        animate().setDuration(shortAnimTime).alpha(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setVisibility(View.GONE);
                    }
                });
    }
}
