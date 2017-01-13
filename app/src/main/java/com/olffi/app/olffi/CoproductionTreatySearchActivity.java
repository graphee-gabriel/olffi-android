package com.olffi.app.olffi;

import android.widget.BaseAdapter;

import com.olffi.app.olffi.data.network.ServiceGenerator;
import com.olffi.app.olffi.json.CoproductionTreaty;
import com.olffi.app.olffi.search.adapter.CoproductionTreatyAdapter;
import com.olffi.app.olffi.search.adapter.DataBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CoproductionTreatySearchActivity extends LocalSearchActivity<CoproductionTreaty> {

    private final static String TAG = CoproductionTreatySearchActivity.class.getSimpleName();

    private static List<CoproductionTreaty> data;
    private List<CoproductionTreaty> dataFiltered = new ArrayList<>();
    private CoproductionTreatyAdapter adapter;

    @Override
    void setData(List<CoproductionTreaty> data) {
        CoproductionTreatySearchActivity.data = data;
    }

    @Override
    DataBaseAdapter getAdapter() {
        return adapter;
    }

    @Override
    List<CoproductionTreaty> getData() {
        return data;
    }

    @Override
    List<CoproductionTreaty> getDataFiltered() {
        return dataFiltered;
    }

    @Override
    String getStringToSearch(CoproductionTreaty treaty) {
        return treaty.countries_list;
    }

    @Override
    Call<List<CoproductionTreaty>> getApiCall() {
        return ServiceGenerator.get(this).coproductionTreatyList();
    }

    @Override
    public BaseAdapter onBuildAdapter() {
        adapter = new CoproductionTreatyAdapter(this);
        return adapter;
    }

    @Override
    public String getSearchHint() {
        return getString(R.string.hint_search_country);
    }

    @Override
    public String getRelativeUrlFromPosition(int position) {
        return dataFiltered.get(position).url;
    }

    @Override
    public String getUrlTitleFromPosition(int position) {
        return dataFiltered.get(position).countries_list;
    }
}
