package com.imie.bot.imibot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickActivity extends Activity {
    private TextView mTextViewAngle;
    private TextView mTextViewStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        mTextViewAngle = findViewById(R.id.textView_angle);
        mTextViewStrength = findViewById(R.id.textView_strength);

        JoystickView joystickLeft = findViewById(R.id.joystickView);
        joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mTextViewAngle.setText(angle + "°");
                mTextViewStrength.setText(strength + "%");
            }
        });
    }
}
