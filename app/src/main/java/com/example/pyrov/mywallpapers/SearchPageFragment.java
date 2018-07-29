package com.example.pyrov.mywallpapers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pyrov.mywallpapers.model.HitsItem;
import com.example.pyrov.mywallpapers.model.MyResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    Unbinder unbinder;
    private int mPage;
    private WallpapersListItemRecyclerAdapter adapter;
    private List<HitsItem> list;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private TextView tvSwipeToRefresh;

    public static SearchPageFragment newSearchInstance(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        SearchPageFragment fragment = new SearchPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_list, null);
        list = new ArrayList<>();
        recycler = view.findViewById(R.id.recycler_search_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_search_list);
        searchView = view.findViewById(R.id.search_bar_search_list);
        tvSwipeToRefresh = view.findViewById(R.id.tv_swipe_to_refresh);
        //tvSwipeToRefresh.setVisibility(View.INVISIBLE);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new WallpapersListItemRecyclerAdapter(App.getContext(), list);
        recycler.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataWallpapers("");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDataWallpapers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getDataWallpapers("");
                return false;
            }
        });

        getDataWallpapers("");

        return view;
    }

    private void getDataWallpapers(String q) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            alertMessage("No internet connection", "Check connection settings", R.drawable.ic_signal_wifi_off_blue_900_36dp);
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

        tvSwipeToRefresh.setVisibility(View.INVISIBLE);
        App.getPixabayApi().getData(q, orderBy, orientation, safeSearch, listSizeWallpapers).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                try {
                    list.clear();
                    list = response.body().getHits();
                } catch (NullPointerException e) {
                    tvSwipeToRefresh.setVisibility(View.VISIBLE);
                }
                adapter.setDataChanged(list);
                swipeRefreshLayout.setRefreshing(false);
                if (list.isEmpty()) {
                    tvSwipeToRefresh.setVisibility(View.VISIBLE);
                } else {
                    tvSwipeToRefresh.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                tvSwipeToRefresh.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                                    getDataWallpapers("");
                                }
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
