package com.csm.androidlibrary.lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnK4LVideoListener {

    private K4LVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;

    private int intMaxDuration = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trimmerlayout);

        Intent extraIntent = getIntent();
        String path = "";

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(Utils.EXTRA_VIDEO_PATH);
        }

        if (extraIntent.getIntExtra(Utils.MAX_DURATION, 0) != 0) {
            intMaxDuration = extraIntent.getIntExtra(Utils.MAX_DURATION, 0);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(intMaxDuration);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnK4LVideoListener(this);
            //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimmerActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent();
        intent.putExtra(Utils.REPORT, "" + uri);
        intent.putExtra(Utils.REPORT_CODE, Utils.REPORT_CODE_SUCCESS);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void cancelAction() {
        Log.i("log", "the action is cancelled.");
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        finishWithError("the action is cancelled.", Utils.REPORT_CODE_TRIMMING_CANCELLED);
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("log", "error occured during trimming the video.");
                finishWithError("error occured during trimming the video.", Utils.REPORT_CODE_ERROR_DURING_TRIMMING_VIDEO);
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("log", "video is prepared.");
            }
        });
    }

    private void finishWithError(String report, int report_code) {
        Intent intent = new Intent();
        intent.putExtra(Utils.REPORT, report);
        intent.putExtra(Utils.REPORT_CODE, report_code);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
