package com.kursach.WeightControl;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {

    public MySimpleCursorAdapter(Context context, Cursor c, String[] from, int[] to, int flags) {
        super(context, R.layout.one_note, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String date = cursor.getString(cursor.getColumnIndex(Helper.COLUMN_DATE));
        String weight = cursor.getString(cursor.getColumnIndex(Helper.COLUMN_WEIGHT));

        TextView dateText = view.findViewById(R.id.data);
        dateText.setText(date);

        TextView weightText = view.findViewById(R.id.ves);
        weightText.setText(weight);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.one_note, parent, false);
    }

}