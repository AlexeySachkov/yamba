package com.intel.asachkov.yamba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

/**
 * Created by asachkov on 16.12.2015.
 */
public class YambaStatusAdapter extends ArrayAdapter<YambaStatus> {

    private final Context context;
    private final List<YambaStatus> items;

    public YambaStatusAdapter(Context context, List<YambaStatus> objects) {
        super(context, -1, objects);

        this.context = context;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView username = (TextView) rowView.findViewById(R.id.username);
        TextView message = (TextView) rowView.findViewById(R.id.status);
        username.setText(items.get(position).getUser());
        message.setText(items.get(position).getMessage());

        return rowView;
    }
}
