package com.olffi.app.olffi;

import android.widget.BaseAdapter;

import com.olffi.app.olffi.data.network.ServiceGenerator;
import com.olffi.app.olffi.json.Country;
import com.olffi.app.olffi.search.adapter.CountryAdapter;
import com.olffi.app.olffi.search.adapter.DataBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CountrySearchActivity extends LocalSearchActivity<Country> {

    private final static String TAG = CountrySearchActivity.class.getSimpleName();

    private static List<Country> data;
    private List<Country> dataFiltered = new ArrayList<>();
    private CountryAdapter adapter;

    @Override
    void setData(List<Country> data) {
        CountrySearchActivity.data = data;
    }

    @Override
    DataBaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    List<Country> getData() {
        return data;
    }

    @Override
    List<Country> getDataFiltered() {
        return dataFiltered;
    }

    @Override
    String getStringToSearch(Country country) {
        return country.name;
    }

    @Override
    Call<List<Country>> getApiCall() {
        return ServiceGenerator.get(this).countryList();
    }

    @Override
    public BaseAdapter onBuildAdapter() {
        adapter = new CountryAdapter(this);
        return adapter;
    }

    @Override
    public String getSearchHint() {
        return getString(R.string.hint_search_country);
    }

    @Override
    public String getRelativeUrlFromPosition(int position) {
        return "/c/"+dataFiltered.get(position).code_iso;
    }

    @Override
    public String getUrlTitleFromPosition(int position) {
        return dataFiltered.get(position).name;
    }
}
