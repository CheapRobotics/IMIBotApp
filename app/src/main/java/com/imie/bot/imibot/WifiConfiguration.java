package com.imie.bot.imibot;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class WifiConfiguration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_configuration);

        final Switch switchBtn = findViewById(R.id.hotspotConfig);
        final TextView textViewHotspotState = findViewById(R.id.hotspotState);

        final FragmentManager fragmentManager = this.getFragmentManager();

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (switchBtn.isEnabled()) {
                    textViewHotspotState.setText(getResources().getString(R.string.hotspot_on));
                    CreateHotpost fragment = new CreateHotpost();
                    fragmentTransaction.add(R.id.fragmentLayoutHotspot, fragment);
                } else {
                    textViewHotspotState.setText(getResources().getString(R.string.hotspot_off));
                    ConnectWifi fragment = new ConnectWifi();
                    fragmentTransaction.add(R.id.fragmentLayoutHotspot, fragment);
                }
                fragmentTransaction.commit();


            }
        });

    }
}
