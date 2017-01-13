package com.olffi.app.olffi.search.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by gabrielmorin on 13/01/2017.
 */

public abstract class DataBaseAdapter<T> extends BaseAdapter {
    abstract public void setData(List<T> data);
}
