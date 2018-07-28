package com.example.pyrov.mywallpapers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pyrov.mywallpapers.model.HitsItem;
import com.example.pyrov.mywallpapers.model.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private WallpapersListItemRecyclerAdapter adapter;
    private List<HitsItem> list;

    public static PageFragment newInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (list.isEmpty()) {
            list = getWallpaperList(mPage);
        }
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.wallpapers_list, container, false);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
        adapter = new WallpapersListItemRecyclerAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    private List<HitsItem> getDataWallpapers(String q) {
        App.getPixabayApi().getData(q).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                list = response.body().getHits();
                adapter.setDataChanged(list);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
        return list;
    }


    private List<HitsItem> getWallpaperList(int mPage) {
        if (mPage == 1) {
            return getDataWallpapers("textured");
        }
        if (mPage == 2) {
            return getDataWallpapers("nature");
        }
        if (mPage == 3) {
            return getDataWallpapers("animal");
        }
        if (mPage == 4) {
            return getDataWallpapers("car");
        }
        if (mPage == 5) {
            return getDataWallpapers("city");
        }
        if (mPage == 6) {
            return getDataWallpapers("");
        }
        return null;
    }
}
