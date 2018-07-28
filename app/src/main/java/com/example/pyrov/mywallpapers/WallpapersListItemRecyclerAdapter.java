package com.example.pyrov.mywallpapers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pyrov.mywallpapers.model.HitsItem;

import java.util.List;

public class WallpapersListItemRecyclerAdapter extends RecyclerView.Adapter<WallpapersListItemRecyclerAdapter.ViewHolder> {

    private List<HitsItem> wallpapers;
    private Context context;

    WallpapersListItemRecyclerAdapter(Context context, List<HitsItem> wallpapers) {
        this.context = context;
        this.wallpapers = wallpapers;
    }

    public void setDataChanged(List<HitsItem> wallpapers) {
        this.wallpapers = wallpapers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HitsItem wallpaper = wallpapers.get(position);
        holder.bindWallpaperItem(wallpaper);
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(wallpaper.getPreviewURL()).thumbnail(0.10f)
                //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HitsItem wallpaper;
        private View view;
        private ImageView imageView;
        private ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = view.findViewById(R.id.wallpaper_item);
            progressBar = view.findViewById(R.id.progress_bar_item);

            itemView.setOnClickListener(this);
        }

        void bindWallpaperItem(HitsItem wallpaper) {
            this.wallpaper = wallpaper;
        }

        @Override
        public void onClick(View v) {
            DetailActivity.startDetailActivity(context, wallpaper.getFullHDURL());
        }
    }
}
