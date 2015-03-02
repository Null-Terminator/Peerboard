package com.nullterminator.peerboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/*
* @author Sanjana
* Created on 22/2/2015
*/
public class GridAdapter extends BaseAdapter{
    private Context context;
    private final String[] gridValues;
    private final String[] gridColors;

    public GridAdapter(Context context, String[] gridValues, String[] gridColors) {
        this.context = context;
        this.gridValues = gridValues;
        this.gridColors = gridColors;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView = new View(context);

            // get layout from homepage_view.xml
            gridView = inflater.inflate(R.layout.homepage_view, null);

            // set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(gridValues[position]);
            textView.setBackgroundColor(0xff000000 + Integer.parseInt(gridColors[position],16));


        return gridView;
    }

    @Override
    public int getCount() {
        return gridValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



}
