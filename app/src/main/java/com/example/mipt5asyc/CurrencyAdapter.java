package com.example.mipt5asyc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends BaseAdapter {

    private final Context context;
    private final List<Currency> originalData;
    private final List<Currency> filteredData;

    public CurrencyAdapter(Context context, List<Currency> data) {
        this.context = context;
        this.originalData = new ArrayList<>(data);
        this.filteredData = new ArrayList<>(data);
        Log.d("CurrencyAdapter", "Adapter initialized with " + data.size() + " items.");
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false);
        }

        Currency currency = filteredData.get(position);
        ((TextView) convertView.findViewById(R.id.currency_code)).setText(currency.getCode());
        ((TextView) convertView.findViewById(R.id.currency_rate)).setText(currency.getRate());

        Log.d("CurrencyAdapter", "Item bound at position " + position + ": " + currency.getCode() + " = " + currency.getRate());

        return convertView;
    }

    public void filter(String query) {
        filteredData.clear();
        if (query.isEmpty()) {
            filteredData.addAll(originalData);
        } else {
            for (Currency currency : originalData) {
                if (currency.getCode().toLowerCase().contains(query.toLowerCase())) {
                    filteredData.add(currency);
                }
            }
        }
        Log.d("CurrencyAdapter", "Filter applied. Items after filtering: " + filteredData.size());
        notifyDataSetChanged();
    }
}
