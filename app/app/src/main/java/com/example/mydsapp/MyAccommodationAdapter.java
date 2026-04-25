package com.example.mydsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAccommodationAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<String> items;

    public MyAccommodationAdapter(LayoutInflater inflater, ArrayList<String> items){
        this.inflater = inflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size()/2;
    }

    @Override
    public Object getItem(int i) {
        return items.get(i*2);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_accommodation_list_item, container, false);
        }

        int baseIndex = position * 2;

        ((TextView) convertView.findViewById(R.id.nameTextView))
                .setText(items.get(baseIndex));

        ((TextView) convertView.findViewById(R.id.datesTextView))
                .setText(items.get(baseIndex + 1));

        return convertView;
    }
}