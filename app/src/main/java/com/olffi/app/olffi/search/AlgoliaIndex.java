package com.olffi.app.olffi.search;

import com.algolia.search.saas.Index;

/**
 * Created by gabrielmorin on 23/01/2017.
 */

public class AlgoliaIndex {

    private static Index index = null;

    public static Index getInstance() {
        if (index == null)
            index = AlgoliaIndexBuilder.build();

        return index;
    }
}
