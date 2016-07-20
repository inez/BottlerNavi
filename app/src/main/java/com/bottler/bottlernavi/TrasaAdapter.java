package com.bottler.bottlernavi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TrasaAdapter extends ArrayAdapter<Trasa> {

    public TrasaAdapter(Context context, List<Trasa> trasy) {
        super(context, 0, trasy);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }
        final Trasa trasa = getItem(position);
        TextView txt1 = (TextView) view.findViewById(android.R.id.text1);
        txt1.setText(trasa.getNazwa());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Intent i = new Intent(context, PunktyActivity.class);
                i.putExtra("TRASA_ID", trasa.getId());
                context.startActivity(i);
            }
        });
        return view;
    }
}
