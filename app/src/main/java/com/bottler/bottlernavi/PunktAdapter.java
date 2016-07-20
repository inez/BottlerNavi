package com.bottler.bottlernavi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PunktAdapter extends ArrayAdapter<Punkt> {

    public PunktAdapter(Context context, List<Punkt> punkty) {
        super(context, 0, punkty);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }
        final Punkt punkt = getItem(position);
        TextView txt1 = (TextView) view.findViewById(android.R.id.text1);
        txt1.setText(punkt.getNazwa());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + punkt.getSzerokosc() + "," + punkt.getDlugosc());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        return view;
    }
}
