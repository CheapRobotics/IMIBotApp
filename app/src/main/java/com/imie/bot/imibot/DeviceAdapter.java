package com.imie.bot.imibot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceAdapter extends ArrayAdapter<IMIBot> {

    private LayoutInflater inflater;
    private ArrayList<IMIBot> bots;

    public DeviceAdapter(Context context, int resource, ArrayList<IMIBot> bots) {
        super(context, resource, bots);

        this.bots = bots;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public IMIBot getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_devices_padded, parent, false);

        IMIBot device = bots.get(position);

        TextView name = (TextView)row.findViewById(R.id.name_label);
        TextView address = (TextView)row.findViewById(R.id.address_label);

        name.setText(device.getStringAddress());


        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_devices, parent, false);

        IMIBot device = bots.get(position);

        TextView name = (TextView)row.findViewById(R.id.name_label);
        TextView address = (TextView)row.findViewById(R.id.address_label);

        name.setText(device.getStringAddress());

        return row;
    }
}
