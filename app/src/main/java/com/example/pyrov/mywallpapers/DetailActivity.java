package com.example.pyrov.mywallpapers;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
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

public class DetailActivity extends AppCompatActivity {

    public static final String KEY = "com.example.pyrov.mywallpapers_key";
    @BindView(R.id.single_image)
    ImageView singleImage;
    @BindView(R.id.no_wifi_image)
    ImageView noWifiImage;
    @BindView(R.id.progress_bar_detail)
    ProgressBar progressBarDetail;

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

        Glide.with(this).load(getIntent().getStringExtra(KEY)).thumbnail(0.10f)
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
        intent.putExtra(KEY, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void makeToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (progressBarDetail.getVisibility() == View.INVISIBLE) {
            if (id == R.id.button_set_wallpaper) {
                setWallpaperAlertMessage();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setWallpaperAlertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_message_install_this_wallpaper)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_message_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                setWallpaper();
                            }
                        })
                .setNegativeButton(getString(R.string.alert_message_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setWallpaper() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) singleImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.setBitmap(bitmap);
            wallpaperManager.suggestDesiredDimensions(width, height);
            makeToast(getString(R.string.wallpapers_changed));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
