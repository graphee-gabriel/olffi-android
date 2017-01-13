package com.olffi.app.olffi.search.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.olffi.app.olffi.R;
import com.olffi.app.olffi.json.SearchResultHit;
import com.olffi.app.olffi.tools.HtmlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class SearchResultAdapter extends BaseAdapter {

    private List<SearchResultHit> data = new ArrayList<>();
    private LayoutInflater layoutInflater;
    //private Context context;

    public SearchResultAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        //this.context = context;
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
            convertView = layoutInflater.inflate(R.layout.layout_list_item_search_result, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchResultHit searchResult = data.get(i);
        holder.textTitle.setText(searchResult.program_name + " (" + searchResult.country_name + ")");
        holder.textSubtitle.setText(searchResult.fb_name);
        holder.textLeft.setText(Html.fromHtml(HtmlHelper.bold("Level: ")
                + "<br/>" + searchResult.level));
        holder.textRight.setText(Html.fromHtml(HtmlHelper.bold("Activity: ")
                + "<br/>" + searchResult.activity));
        //noinspection ConstantConditions
        holder.textDetails.setText(Html.fromHtml(HtmlHelper.bold("Project type: ")
                + "<br/>" + (searchResult.project_type == null ? "" : TextUtils.join(", ", searchResult.project_type))
                + "<br/>"
                + "<br/>" + HtmlHelper.bold("Nature of project: ")
                + "<br/>" + (searchResult.nature_of_project == null ? "" : TextUtils.join(", ", searchResult.nature_of_project))
        ));

        return convertView;
    }

    public void setData(List<SearchResultHit> data) {
        this.data = data;
    }

    private static class ViewHolder {
        View view;
        TextView textTitle;
        TextView textSubtitle;
        TextView textLeft;
        TextView textRight;
        TextView textDetails;

        ViewHolder(View view) {
            this.view = view;
            textTitle = (TextView) view.findViewById(R.id.text_title);
            textSubtitle = (TextView) view.findViewById(R.id.text_subtitle);
            textLeft = (TextView) view.findViewById(R.id.text_left);
            textRight = (TextView) view.findViewById(R.id.text_right);
            textDetails = (TextView) view.findViewById(R.id.text_details);
        }
    }
}
