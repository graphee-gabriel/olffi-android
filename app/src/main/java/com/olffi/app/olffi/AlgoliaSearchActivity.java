package com.olffi.app.olffi;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.BaseAdapter;

import com.olffi.app.olffi.json.SearchResult;
import com.olffi.app.olffi.search.AlgoliaIndex;
import com.olffi.app.olffi.search.AlgoliaSearchController;
import com.olffi.app.olffi.search.adapter.SearchResultAdapter;

import java.util.ArrayList;

public class AlgoliaSearchActivity extends SearchBaseActivity implements AlgoliaSearchController.SearchResultListener {

    private final static String TAG = AlgoliaSearchActivity.class.getSimpleName();

    private SearchResultAdapter searchResultAdapter;
    private AlgoliaSearchController algoliaSearchController;
    private SearchResult data;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onPreBuildAdapter() {
        algoliaSearchController = new AlgoliaSearchController(AlgoliaIndex.getInstance());
        algoliaSearchController.setListener(this);
    }

    @Override
    public BaseAdapter onBuildAdapter() {
        searchResultAdapter = new SearchResultAdapter(this);
        return searchResultAdapter;
    }

    @Override
    public String getSearchHint() {
        return getString(R.string.action_search);
    }

    @Override
    public int getMinSearchChar() {
        return 1;
    }

    @Override
    public boolean hasData() {
        return data != null;
    }

    @Override
    public String getRelativeUrlFromPosition(int position) {
        return data.hits.get(position).program_url;
    }

    @Override
    public String getUrlTitleFromPosition(int position) {
        return data.hits.get(position).program_name;
    }

    @Override
    public void onSearchEmpty() {
        setAdapterData(null);
        hideLoading();
        setEmptyText(getSearchHint());
        clearHandler();
    }

    @Override
    public void onSearch(String query) {
        setEmptyText(getString(R.string.list_empty_searching_for, query));
        setAdapterData(null);
        showLoading();

        clearHandler();
        runnable = () -> algoliaSearchController.searchAsync(query);
        handler.postDelayed(runnable, 300);
    }

    @Override
    public void onResult(SearchResult data) {
        if (!isQueryNull())
            setEmptyText(getString(R.string.list_empty_no_result, getQuery()));
        this.data = data;
        setAdapterData(data);
        hideLoading();
    }

    @Override
    public void onError(String message) {
        hideLoading();
        Log.e(TAG, "onError: "+message);
    }

    private void setAdapterData(SearchResult data) {
        if (searchResultAdapter != null) {
            searchResultAdapter.setData(
                    data == null || TextUtils.isEmpty(getQuery()) ?
                            new ArrayList<>() :
                            data.hits
            );
            searchResultAdapter.notifyDataSetChanged();
        }
    }

    private void clearHandler() {
        if (handler == null)
            handler = new Handler();
        if (runnable != null)
            handler.removeCallbacks(runnable);
        runnable = null;
    }
}
