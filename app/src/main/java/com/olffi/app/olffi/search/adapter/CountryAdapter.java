package com.olffi.app.olffi.search.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olffi.app.olffi.R;
import com.olffi.app.olffi.json.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class CountryAdapter extends DataBaseAdapter<Country> {
    private final static String TAG = CountryAdapter.class.getSimpleName();

    private List<Country> data = new ArrayList<>();

    private LayoutInflater layoutInflater;

    public CountryAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_simple_list_item_1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Country country = (Country) getItem(i);
        holder.text.setText(country.name);

        return convertView;
    }

    @Override
    public void setData(List<Country> data) {
        this.data = data;
    }

    private static class ViewHolder {
        View view;
        TextView text;

        ViewHolder(View view) {
            this.view = view;
            text = (TextView) view.findViewById(android.R.id.text1);
            Context c = view.getContext();
            view.setBackgroundColor(ContextCompat.getColor(c, R.color.colorCardBackground));
        }
    }
}
