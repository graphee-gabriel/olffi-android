package com.olffi.app.olffi;

import android.text.TextUtils;
import android.util.Log;

import com.olffi.app.olffi.search.adapter.DataBaseAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gabrielmorin on 13/01/2017.
 */

public abstract class LocalSearchActivity<T> extends SearchBaseActivity {

    private final static String TAG = LocalSearchActivity.class.getSimpleName();

    abstract void setData(List<T> data);
    abstract DataBaseAdapter getAdapter();
    abstract List<T> getData();
    abstract List<T> getDataFiltered();
    abstract String getStringToSearch(T object);
    abstract Call<List<T>> getApiCall();

    @Override
    public void onPreBuildAdapter() {
        showLoading();
        getApiCall().enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {

                Log.d(TAG, "onResponse: "+response.code()+" | "+response.message()+" | "+call.request().url());
                setDataAndUpdateUI(response.body());
                hideLoading();
                Log.d(TAG, "onResponse: success count -> "+ (getData() == null ? "null" : getData().size()));
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                hideLoading();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public int getMinSearchChar() {
        return 0;
    }

    @Override
    public boolean hasData() {
        return getDataFiltered() != null;
    }

    @Override
    public void onSearchEmpty() {
        filter(null);
        setDataAdapter(getDataFiltered());
        setEmptyText(getSearchHint());
    }

    @Override
    public void onSearch(String query) {
        filter(query);
        setDataAdapter(getDataFiltered());
        setEmptyText(getString(R.string.list_empty_no_result, query));
    }

    public void filter(String query) {
        getDataFiltered().clear();
        if (getData() != null) {
            String[] queries = TextUtils.isEmpty(query) ? null : query.toLowerCase().trim().split("\\s+");
            for (T object : getData()) {
                String name = getStringToSearch(object);
                boolean shouldAddObject = true;
                if (queries != null && name != null)
                    for (String q : queries)
                        if (!name.toLowerCase().contains(q))
                            shouldAddObject = false;

                if (shouldAddObject)
                    getDataFiltered().add(object);
            }
        }
    }

    private void setDataAndUpdateUI(List<T> data) {
        setData(data);
        filter(getQuery());
        setDataAdapter(getDataFiltered());
    }

    private void setDataAdapter(List<T> data) {
        DataBaseAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }
}
