package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemListaAdapter extends ArrayAdapter<ItemLista> {
    private final LayoutInflater inflater;

    public ItemListaAdapter(Context context, List<ItemLista> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemLista item = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        if (item != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(item.getTexto());
            textView.setTextColor(item.getCor());
        }

        return convertView;
    }

}
