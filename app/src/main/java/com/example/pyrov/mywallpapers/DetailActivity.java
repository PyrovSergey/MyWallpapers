package com.example.pyrov.mywallpapers;

import android.app.SearchManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.single_image)
    ImageView singleImage;
    @BindView(R.id.no_wifi_image)
    ImageView noWifiImage;
    @BindView(R.id.progress_bar_detail)
    ProgressBar progressBarDetail;

    private WallpaperManager wallpaperManager;
    private Bitmap bitmap1, bitmap2;
    private DisplayMetrics displayMetrics;
    private int width, height;
    private BitmapDrawable bitmapDrawable;

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            progressBarDetail.setVisibility(View.INVISIBLE);
            noWifiImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

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

    public static void startDetailActivity(Context context, String url) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("key", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void GetScreenWidthHeight() {

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

    }

    private void SetBitmapSize() {
        bitmap2 = Bitmap.createScaledBitmap(bitmap1, width, height, true);
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (progressBarDetail.getVisibility() == View.INVISIBLE) {
            int id = item.getItemId();
            if (id == R.id.button_set_wallpaper) {
                bitmapDrawable = (BitmapDrawable) singleImage.getDrawable();
                bitmap1 = bitmapDrawable.getBitmap();
                GetScreenWidthHeight();
                SetBitmapSize();
                wallpaperManager = WallpaperManager.getInstance(DetailActivity.this);

                try {
                    wallpaperManager.setBitmap(bitmap2);
                    wallpaperManager.suggestDesiredDimensions(width, height);
                    makeToast("Wallpapers changed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
