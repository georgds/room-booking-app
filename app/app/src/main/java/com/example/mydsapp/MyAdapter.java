package com.example.mydsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<String> items;
    private Context context;
    private static final String TAG = "MyAdapter";

    public MyAdapter(Context context, LayoutInflater inflater, ArrayList<String> items){
        this.context = context;
        this.inflater = inflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size()/7;
    }

    @Override
    public Object getItem(int i) {
        return items.get(i+7);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_list_item, container, false);
        }

        int baseIndex = position * 7;

        ((TextView) convertView.findViewById(R.id.rnameTextView))
                .setText(items.get(baseIndex));

        ((TextView) convertView.findViewById(R.id.rareaTextView))
                .setText(items.get(baseIndex+1));

        ((TextView) convertView.findViewById(R.id.rpeopleTextView))
                .setText(items.get(baseIndex+2));

        ((TextView) convertView.findViewById(R.id.rpriceTextView))
                .setText(items.get(baseIndex+3));

        ((TextView) convertView.findViewById(R.id.rstarsTextView))
                .setText(items.get(baseIndex+4));

        ((TextView) convertView.findViewById(R.id.rreviewsTextView))
                .setText(items.get(baseIndex+5));

        ImageView imageView = convertView.findViewById(R.id.image);

        String imagePath = items.get(baseIndex + 6);

        try {
            InputStream is = context.getApplicationContext().getResources().getAssets().open("rimages/" + imagePath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}