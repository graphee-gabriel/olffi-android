package com.olffi.app.olffi.search.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olffi.app.olffi.R;
import com.olffi.app.olffi.json.CoproductionTreaty;
import com.olffi.app.olffi.tools.HtmlHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class CoproductionTreatyAdapter extends DataBaseAdapter<CoproductionTreaty> {
    private final static String TAG = CoproductionTreatyAdapter.class.getSimpleName();

    private List<CoproductionTreaty> data = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public CoproductionTreatyAdapter(Context context) {
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
            convertView = layoutInflater.inflate(R.layout.layout_list_item_coproduction_treaty, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CoproductionTreaty treaty = (CoproductionTreaty) getItem(i);
        holder.text.setText(treaty.countries_list + " (" + treaty.sign_date + ")");
        //noinspection ConstantConditions
        holder.text2.setText(Html.fromHtml(HtmlHelper.bold("Project type: ")
                + "<br/>" + treaty.project_type
                + "<br/>"
                + "<br/>" + HtmlHelper.bold("In force: ")
                + "<br/>" + treaty.in_force
        ));

        return convertView;
    }

    @Override
    public void setData(List<CoproductionTreaty> data) {
        this.data = data;
    }


    private static class ViewHolder {
        View view;
        TextView text;
        TextView text2;

        ViewHolder(View view) {
            this.view = view;
            text = (TextView) view.findViewById(R.id.text);
            text2 = (TextView) view.findViewById(R.id.text2);
        }
    }
}
