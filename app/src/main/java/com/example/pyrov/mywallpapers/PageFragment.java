package com.example.pyrov.mywallpapers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

    private static final String ARG_PAGE = "ARG_PAGE";
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
                .setNegativeButton(getString(R.string.alert_message_ok),
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            alertMessage(getString(R.string.no_internet_connection), getString(R.string.check_connection_settings), R.drawable.ic_signal_wifi_off_blue_900_36dp);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_key), getString(R.string.settings_popular_default));
        String orientation = sharedPreferences.getString(getString(R.string.settings_orientation_key), getString(R.string.settings_orientation_default));
        String safeSearch = sharedPreferences.getString(getString(R.string.settings_safe_search_key), getString(R.string.settings_orientation_default));
        String listSizeWallpapers = sharedPreferences.getString(getString(R.string.settings_list_size_key), getString(R.string.settings_list_size_default));
        int tmpSize;
        try {
            tmpSize = Integer.parseInt(listSizeWallpapers);
        } catch (NumberFormatException e) {
            tmpSize = Integer.parseInt(getString(R.string.settings_list_size_default));
        }

        if (tmpSize < 3) {
            tmpSize = 3;
        } else if (tmpSize > 200) {
            tmpSize = 200;
        }
        listSizeWallpapers = String.valueOf(tmpSize);

        App.getPixabayApi().getData(q, orderBy, orientation, safeSearch, listSizeWallpapers).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                try {
                    list = response.body().getHits();
                } catch (NullPointerException e) {
                    getWallpaperList(mPage);
                }
                adapter.setDataChanged(list);
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
            }
        });
        return list;
    }

    private List<HitsItem> getWallpaperList(int mPage) {
        if (mPage == 2) {
            return getDataWallpapers(getString(R.string.texture_request));
        }
        if (mPage == 3) {
            return getDataWallpapers(getString(R.string.nature_request));
        }
        if (mPage == 4) {
            return getDataWallpapers(getString(R.string.animal_request));
        }
        if (mPage == 5) {
            return getDataWallpapers(getString(R.string.car_request));
        }
        if (mPage == 6) {
            return getDataWallpapers(getString(R.string.city_request));
        }
        if (mPage == 7) {
            return getDataWallpapers(getString(R.string.universe_request));
        }
        return null;
    }
}
