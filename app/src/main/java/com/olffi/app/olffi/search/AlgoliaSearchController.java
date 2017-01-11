package com.olffi.app.olffi.search;

import android.os.AsyncTask;
import android.util.Log;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.olffi.app.olffi.json.SearchResult;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class AlgoliaSearchController {

    private String query;
    private Index index;
    private SearchResultListener listener;
    private ParseSearchResultAsyncTask parseSearchResultAsyncTask;

    private CompletionHandler completionHandler = new CompletionHandler() {
        @Override
        public void requestCompleted(JSONObject content, AlgoliaException error) {
            if (parseSearchResultAsyncTask != null) {
                parseSearchResultAsyncTask.cancel(true);
            }

            parseSearchResultAsyncTask = new ParseSearchResultAsyncTask();
            parseSearchResultAsyncTask.execute(content);
        }
    };

    public AlgoliaSearchController(Index index) {
        this.index = index;
    }

    public void searchAsync(String query) {
        this.query = query;
        index.searchAsync(
                new Query(query)
                        .setHitsPerPage(AlgoliaIndexBuilder.SETTINGS_HITS_PER_PAGE),
                completionHandler);
    }

    public void setListener(SearchResultListener listener) {
        this.listener = listener;
    }

    private class ParseSearchResultAsyncTask extends AsyncTask<JSONObject, Integer, String> {
        SearchResult searchResult = null;
        protected String doInBackground(JSONObject... contents) {
            searchResult = null;
            JSONObject content = contents[0];
            if (content != null) {
                final String json = content.toString();
                final Moshi moshi = new Moshi.Builder().build();
                final JsonAdapter<SearchResult> jsonAdapter = moshi.adapter(SearchResult.class);
                try {
                    searchResult = jsonAdapter.fromJson(json);
                    return null;
                } catch (IOException e) {
                    Log.e(AlgoliaSearchController.class.getSimpleName(), "Can't turn json into string", e);
                    if (listener != null)
                        listener.onError(e.getMessage());
                    return e.getMessage();
                }
                //Log.i("RESULT:", content.toString());
            }

            return "No internet connection";
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String error) {
            if (listener != null && !isCancelled()) {
                if (error == null) {
                    if (query != null && query.equals(searchResult.query))
                        listener.onResult(searchResult);
                } else {
                    listener.onError(error);
                }
            }
            if (parseSearchResultAsyncTask == this)
                parseSearchResultAsyncTask = null;
        }
    }

    public interface SearchResultListener {
        void onResult(SearchResult data);
        void onError(String message);
    }
}
