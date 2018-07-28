package com.example.pyrov.mywallpapers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new WallpapersListItemRecyclerAdapter(App.getContext(), list);
        recycler.setAdapter(adapter);
        return view;
    }

    private List<HitsItem> getDataWallpapers(String q) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            //alertMessage("No internet connection", "Check connection settings", R.drawable.ic_signal_wifi_off_blue_900_36dp);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
