package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.olffi.app.olffi.data.network.ServiceGenerator;
import com.olffi.app.olffi.json.Country;
import com.olffi.app.olffi.search.CountryAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountrySearchActivity extends SearchBaseActivity {

    private final static String TAG = CountrySearchActivity.class.getSimpleName();

    private static List<Country> data;
    private List<Country> dataFiltered = new ArrayList<>();
    private CountryAdapter countryAdapter;

    @Override
    public void onPreBuildAdapter() {
        showLoading();
        ServiceGenerator.get(this).countryList().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                Log.d(TAG, "onResponse: "+response.code()+" | "+response.message()+" | "+call.request().url());
                setData(response.body());
                hideLoading();
                Log.d(TAG, "onResponse: success count -> "+ (data == null ? "null" : data.size()));
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                hideLoading();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (data != null) {
            filter(getQuery());
            setDataAdapter(dataFiltered);
        }
    }

    @Override
    public BaseAdapter onBuildAdapter() {
        countryAdapter = new CountryAdapter(this);
        return countryAdapter;
    }

    @Override
    public String getSearchHint() {
        return getString(R.string.hint_search_country);
    }

    @Override
    public int getMinSearchChar() {
        return 0;
    }

    @Override
    public boolean hasData() {
        return dataFiltered != null;
    }

    @Override
    public String getRelativeUrlFromPosition(int position) {
        return "/c/"+dataFiltered.get(position).code_iso;
    }

    @Override
    public String getUrlTitleFromPosition(int position) {
        return dataFiltered.get(position).name;
    }

    @Override
    public void onSearchEmpty() {
        filter(null);
    }

    @Override
    public void onSearch(String query) {
        filter(query);
        setDataAdapter(dataFiltered);
    }

    public void filter(String query) {
        dataFiltered.clear();
        if (data != null) {
            boolean isQueryEmpty = TextUtils.isEmpty(query);
            for (Country country : data) {
                String name = country.name;
                if (isQueryEmpty || name != null && name.toLowerCase().contains(query)) {
                    dataFiltered.add(country);
                }
            }
        }
    }

    private void setData(List<Country> data) {
        CountrySearchActivity.data = data;
        filter(getQuery());
        setDataAdapter(dataFiltered);
    }

    private void setDataAdapter(List<Country> data) {
        if (countryAdapter != null) {
            countryAdapter.setData(data);
            countryAdapter.notifyDataSetChanged();
        }
    }
}
