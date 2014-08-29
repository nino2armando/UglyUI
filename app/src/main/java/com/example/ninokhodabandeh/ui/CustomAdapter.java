package com.example.ninokhodabandeh.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ninokhodabandeh.ui.Services.ApiServices;

import java.util.ArrayList;

/**
 * Created by nino.khodabandeh on 8/28/2014.
 */
public class CustomAdapter extends ArrayAdapter<ApiServices.ApiResultModel> {

    private final Context _context;
    private final ArrayList<ApiServices.ApiResultModel> _itemsArrayList;

    public CustomAdapter(Context context, ArrayList<ApiServices.ApiResultModel> itemsArrayList) {
        super(context, R.layout.row,  itemsArrayList);
        _context = context;
        _itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row, parent, false);
        rowView.setId(_itemsArrayList.get(position).getId());

        TextView labelView = (TextView) rowView.findViewById(R.id.modelDistance);
        TextView valueView = (TextView) rowView.findViewById(R.id.modelContent);

        labelView.setText(_itemsArrayList.get(position).getDistance());
        valueView.setText(_itemsArrayList.get(position).getContent());

        return rowView;
    }
}
