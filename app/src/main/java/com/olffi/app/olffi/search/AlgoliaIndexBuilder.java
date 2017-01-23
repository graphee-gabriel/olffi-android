package com.olffi.app.olffi.search;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;

/**
 * Created by gabrielmorin on 06/01/2017.
 */

public class AlgoliaIndexBuilder {

    private final static String
            APPLICATION_ID = "VJSPCUKOBC",
            API_KEY = "68f4f23581880af2fb862ba0201e3c09",
            INDEX_NAME = "Olffi_Programs";

    public final static int
            SETTINGS_HITS_PER_PAGE = 400,
            SETTINGS_CACHE_DURATION = 60000,
            SETTINGS_CACHE_MAX_REQUEST = 50;

    public static Index build() {
        Client client = new Client(APPLICATION_ID, API_KEY);
        Index index = client.initIndex(INDEX_NAME);
        index.enableSearchCache(SETTINGS_CACHE_DURATION, SETTINGS_CACHE_MAX_REQUEST);
        return index;
    }

}
