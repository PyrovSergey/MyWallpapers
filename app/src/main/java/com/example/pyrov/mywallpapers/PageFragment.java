package com.example.pyrov.mywallpapers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pyrov.mywallpapers.model.HitsItem;
import com.example.pyrov.mywallpapers.model.MyResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private void alertMessage(String title, String message, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(icon)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (list.isEmpty()) {
                                    list = getWallpaperList(mPage);
                                }
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            alertMessage("No internet connection", "Check connection settings", R.drawable.ic_signal_wifi_off_blue_900_36dp);
        }
        App.getPixabayApi().getData(q).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                list = response.body().getHits();
                adapter.setDataChanged(list);
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
            }
        });
        return list;
    }


    private List<HitsItem> getWallpaperList(int mPage) {
        if (mPage == 2) {
            return getDataWallpapers("textured");
        }
        if (mPage == 3) {
            return getDataWallpapers("nature");
        }
        if (mPage == 4) {
            return getDataWallpapers("animal");
        }
        if (mPage == 5) {
            return getDataWallpapers("car");
        }
        if (mPage == 6) {
            return getDataWallpapers("city");
        }
        if (mPage == 7) {
            return getDataWallpapers("girls");
        }
        return null;
    }
}
