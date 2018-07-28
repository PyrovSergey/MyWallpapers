package com.example.pyrov.mywallpapers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pyrov.mywallpapers.model.HitsItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.single_image)
    ImageView singleImage;
    @BindView(R.id.progress_bar_detail)
    ProgressBar progressBarDetail;
    private HitsItem hitsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Glide.with(this).load(getIntent().getStringExtra("key")).thumbnail(0.10f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBarDetail.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(singleImage);
    }

    public static void startDetailActivity(Context context,
                                           String url) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("key", url);
        context.startActivity(intent);
    }
}
