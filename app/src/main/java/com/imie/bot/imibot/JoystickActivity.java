package com.imie.bot.imibot;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickActivity extends Activity {
    private TextView mTextViewAngle;
    private TextView mTextViewStrength;
    private Button quitButton;
    private BTDevice btDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        mTextViewAngle = findViewById(R.id.textView_angle);
        mTextViewStrength = findViewById(R.id.textView_strength);
        quitButton = findViewById(R.id.button_quit);
        JoystickView joystickLeft = findViewById(R.id.joystickView);
        btDevice = BTDevice.getInstance();

        joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mTextViewAngle.setText(angle + "°");
                mTextViewStrength.setText(strength + "%");

                if (btDevice.getMmSocket().isConnected()){
                    int[] values = {angle, strength};
//                    byte[] angleBytes = ByteBuffer.allocate(4).putInt(angle).array();
//                    byte[] strengthBytes = ByteBuffer.allocate(4).putInt(strength).array();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(values.length * 4);
                    IntBuffer intBuffer = byteBuffer.asIntBuffer();
                    intBuffer.put(values);

                    byte[] array = byteBuffer.array();

                    try {
                        btDevice.getMmSocket().getOutputStream().write(array);
                        btDevice.getMmSocket().getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(JoystickActivity.this, "L'IMIBot n'est plus connecté via bluetooth !",  Toast.LENGTH_LONG).show();
                }
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btDevice.closeSocket();
            }
        });
    }
}
