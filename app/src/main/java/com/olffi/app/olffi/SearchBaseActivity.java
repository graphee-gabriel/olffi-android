package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.olffi.app.olffi.controllers.ToolbarController;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;
import com.olffi.app.olffi.webapp.UrlBuilder;

public abstract class SearchBaseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private SearchView searchView;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private ListView listView;
    private BaseAdapter adapter;
    private String query;
    private ToolbarController toolbarController;
    private String emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbarController = new ToolbarController(this);
        toolbarController.setShowBackButton(true);
        onPreBuildAdapter();
        adapter = onBuildAdapter();
        listView = (ListView) findViewById(R.id.listView);
        emptyTextView = (TextView) findViewById(R.id.emptyElement);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
        listView.setEmptyView(emptyTextView);
        adapter.notifyDataSetChanged();
        setEmptyText(getSearchHint());
    }

    abstract public void onPreBuildAdapter();
    abstract public BaseAdapter onBuildAdapter();
    abstract public String getSearchHint();
    abstract public int getMinSearchChar();
    abstract public boolean hasData();
    abstract public String getRelativeUrlFromPosition(int position);
    abstract public String getUrlTitleFromPosition(int position);
    abstract public void onSearchEmpty();
    abstract public void onSearch(String query);

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (searchView.getQuery().length() > 0)
            searchView.setQuery("", true);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getSearchHint());
        searchView.onActionViewExpanded();
        onQueryTextChange(null);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        this.query = query;
        if (isQueryNull()) {
            onSearchEmpty();
        } else {
            onSearch(query);
        }
        //adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (hasData()) {
            String url = UrlBuilder.getUrlFromRelativeUrl(new UserPreferences(this), getRelativeUrlFromPosition(position));
            App.startWebAppFromUrl(this, url, getUrlTitleFromPosition(position));
        }
    }

    public String getQuery() {
        return query;
    }

    public boolean isQueryNull() {
        return query == null || query.length() <= getMinSearchChar();
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        if (emptyTextView != null)
        emptyTextView.setText(emptyText);
    }
}
