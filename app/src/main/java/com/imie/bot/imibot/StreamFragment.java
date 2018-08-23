package com.imie.bot.imibot;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;
import com.github.niqdev.mjpeg.*;
import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.imie.bot.imibot.FirstConnectionActivity.ROS_MASTER_URI;

/**
 * Created by nbethuel on 08/02/18.
 */

public class StreamFragment extends Fragment {
    private static ProgressDialog progressDialog;


    private static final int TIMEOUT = 5;

    @BindView(R.id.mjpegViewCustomAppearance)
    MjpegView mjpegView;
    String videoUrl;
    VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind((Activity) this.getContext());
        SharedPreferences settings = this.getContext().getSharedPreferences(FirstConnectionActivity.PREFS_NAME, 0);
        videoUrl = settings.getString(ROS_MASTER_URI, "");
        videoUrl = "http://" + videoUrl + ":8080/stream?topic=/webcam/image_raw";
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream, container, false);


        return view;
    }
    private String getPreference(String key) {
        return PreferenceManager
                .getDefaultSharedPreferences(this.getContext())
                .getString(key, "");
    }

    private DisplayMode calculateDisplayMode() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE ?
                DisplayMode.FULLSCREEN : DisplayMode.BEST_FIT;
    }

    private void loadIpCam() {
        MjpegView mjpegView = (MjpegView) this.getView().findViewById(R.id.mjpegViewCustomAppearance);

        Mjpeg.newInstance()
                .open(videoUrl, TIMEOUT)
                .subscribe(
                        (MjpegInputStream inputStream) -> {
                            mjpegView.setSource(inputStream);
                            mjpegView.setDisplayMode(calculateDisplayMode());
                            mjpegView.setTransparentBackground();
                        },
                        throwable -> {
                            Log.e(getClass().getSimpleName(), "mjpeg error", throwable);
                            Toast.makeText(this.getContext(), "Error : Stream not found.", Toast.LENGTH_LONG).show();
                        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIpCam();
    }

    @Override
    public void onPause() {
        super.onPause();
        MjpegView mjpegView = (MjpegView) this.getView().findViewById(R.id.mjpegViewCustomAppearance);
        mjpegView.stopPlayback();
    }


}
