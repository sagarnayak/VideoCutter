package com.csm.androidlibrary.lib;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.net.URI;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class RecordAndSave extends Activity {

    Context context;
    Dialog dialogChooserDialog;

    int intMaxDuration = 0;

    RelativeLayout relRecordVideoButton;
    RelativeLayout relGalleryButton;

    int intCalculatingParameter;
    int intCircleDimen;
    int intIconDimen;
    int intmarginDimen;
    int intMarginMiddleDimen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = RecordAndSave.this;

        setLayoutParams();

        performAppropriateAction();
    }

    private void setLayoutParams() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("log", "x y : " + width + "," + height);


        if (width < height) {
            intCalculatingParameter = width;
        } else {
            intCalculatingParameter = height;
        }

        int newcalcdimen = (int) (intCalculatingParameter - (.25 * intCalculatingParameter));

        intCircleDimen = (int) (.50 * newcalcdimen);
        intIconDimen = (int) (.20 * newcalcdimen);
        intmarginDimen = (int) (.06 * newcalcdimen);
        intMarginMiddleDimen = (int) (.03 * newcalcdimen);
    }

    private void performAppropriateAction() {

        Intent intent = getIntent();
        int intOperationType = intent.getIntExtra(Utils.operation_type, 0);
        String path = "";
        path = intent.getStringExtra(Utils.FILE_PATH);

        setMaxDuration();

        switch (intOperationType) {
            case 1://open chooser dialog
                displayChooserDialog();
                break;
            case 2://choose from gallery
                performChooseFromGallery();
                break;
            case 3://record video
                performRecordVideo();
                break;
            case 4://pass path
                if (path != null) {
                    File file = new File(URI.create(intent.getStringExtra(Utils.FILE_PATH)).getPath());
                    if (file.exists() && file.canRead()) {
                        //Do something
                        Log.i("log", "valid path found.");
                        passPath(Uri.parse(intent.getStringExtra(Utils.FILE_PATH)));
                    } else {
                        //close activity with error file path not valid or not accessable
                        Log.i("log", "the file path is not accessable or not valid.");
                        finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "the file path is not accessable or not valid.");
                    }
                } else {
                    //close activity with error no parameter passed
                    Log.i("log", "please pass a valid file path. do not leave the FILE_PATH empty.");
                    finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "please pass a valid file path. do not leave the FILE_PATH empty.");
                }
                break;
            default:
                Log.i("log", "no operation selected.");
                finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "please select a valid operation type");
        }

    }

    public void displayChooserDialog() {
        Log.i("log", "chooser dialog open.");
        dialogChooserDialog = new Dialog(context);
        dialogChooserDialog.setContentView(R.layout.chooserdialog);
        dialogChooserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialogChooserDialog.getWindow().getAttributes();
        layoutParams.width = (int) (intCalculatingParameter - (.25 * intCalculatingParameter));

        dialogChooserDialog.getWindow().setAttributes(layoutParams);
        dialogChooserDialog.show();

        RelativeLayout.LayoutParams layoutPara = (RelativeLayout.LayoutParams) ((ImageView) dialogChooserDialog.findViewById(R.id.imageView)).getLayoutParams();
        layoutPara.height = intCircleDimen;
        layoutPara.width = intCircleDimen;
        layoutPara.setMargins(intmarginDimen, intMarginMiddleDimen, intMarginMiddleDimen, intMarginMiddleDimen);
        ((ImageView) dialogChooserDialog.findViewById(R.id.imageView)).setLayoutParams(layoutPara);

        RelativeLayout.LayoutParams layoutPara2 = (RelativeLayout.LayoutParams) ((ImageView) dialogChooserDialog.findViewById(R.id.imageView2)).getLayoutParams();
        layoutPara2.height = intCircleDimen;
        layoutPara2.width = intCircleDimen;
        layoutPara2.setMargins(intMarginMiddleDimen, intMarginMiddleDimen, intmarginDimen, intMarginMiddleDimen);
        ((ImageView) dialogChooserDialog.findViewById(R.id.imageView2)).setLayoutParams(layoutPara2);

        RelativeLayout.LayoutParams layoutPara3 = (RelativeLayout.LayoutParams) ((ImageView) dialogChooserDialog.findViewById(R.id.imageView3)).getLayoutParams();
        layoutPara3.height = intIconDimen;
        layoutPara3.width = intIconDimen;
        ((ImageView) dialogChooserDialog.findViewById(R.id.imageView3)).setLayoutParams(layoutPara3);
        ((ImageView) dialogChooserDialog.findViewById(R.id.imageView34)).setLayoutParams(layoutPara3);

        dialogChooserDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.i("log", "dialog is cancelled");
                finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "dialog is cancelled");
            }
        });

        relRecordVideoButton = (RelativeLayout) dialogChooserDialog.findViewById(R.id.recordviewcontainer);
        relGalleryButton = (RelativeLayout) dialogChooserDialog.findViewById(R.id.galleryviewcontainer);

        relRecordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (dialogChooserDialog.isShowing()) {
                    dialogChooserDialog.dismiss();
                }
                Log.i("log", "record video choosed.");
                performRecordVideo();
            }
        });

        relGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (dialogChooserDialog.isShowing()) {
                    dialogChooserDialog.dismiss();
                }
                Log.i("log", "choose from gallery choosed choosed.");
                performChooseFromGallery();
            }
        });

    }

    public void performRecordVideo() {
        Log.i("log", "going to record video.");
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoCapture, Utils.REQUEST_VIDEO_TRIMMER);

    }

    public void performChooseFromGallery() {
        Log.i("log", "going to choose from gallery.");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, context.getString(R.string.permission_read_storage_rationale), Utils.REQUEST_STORAGE_READ_ACCESS_PERMISSION);
            Log.i("log", "read external storage permission is not granted.");
        } else {
            Intent intent = new Intent();
            intent.setTypeAndNormalize("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, context.getString(R.string.label_select_video)), Utils.REQUEST_VIDEO_TRIMMER);
        }

    }

    public void passPath(Uri fileuri) {
        Log.i("log", "got a path for the file to process. " + fileuri);
        startTrimActivity(fileuri);
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.permission_title_rationale));
            builder.setMessage(rationale);
            builder.setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton(getString(R.string.label_cancel), null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performChooseFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Utils.REQUEST_VIDEO_TRIMMER) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startTrimActivity(selectedUri);
                } else {
                    Log.i("log", "can not read the file after it is saved to the device memory.");
                    finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "can not read the file after it is saved to device memory");
                }
            } else if (requestCode == Utils.REQUEST_CODE_RECORD_TO_TRIM) {
                //result from trim activity
                new AlertDialog.Builder(context)
                        .setTitle("Trimming Done")
                        .setMessage("The video is done trimming. do you want to see a preview ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getStringExtra(Utils.REPORT)));
                                intent.setDataAndType(Uri.parse(data.getStringExtra(Utils.REPORT)), "video/mp4");
                                startActivity(intent);
                                finishActivityWithReport(Utils.REPORT__TYPE_DONE, data.getStringExtra(Utils.REPORT));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finishActivityWithReport(Utils.REPORT__TYPE_DONE, data.getStringExtra(Utils.REPORT));
                            }
                        })
                        .setIcon(android.R.drawable.ic_media_play)
                        .show();
            }
        } else {
            if (requestCode == Utils.REQUEST_VIDEO_TRIMMER) {
                Log.i("log", "the result is cancelled by android system due to inappropriate selection.");
                new AlertDialog.Builder(context)
                        .setTitle("Wrong Selection !!!")
                        .setMessage("You have done a wrong selection. do you want to do the selection again ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                performAppropriateAction();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, "the result is cancelled by android system due to inappropriate selection.");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else if (requestCode == Utils.REQUEST_CODE_RECORD_TO_TRIM) {
                Log.i("log", "error in trimming video. error is : " + data.getStringExtra(Utils.REPORT));
                finishActivityWithReport(Utils.REPORT_TYPE_CANCELLED, data.getStringExtra(Utils.REPORT));
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        Log.i("log", "the uri to trim is : " + uri);
        Intent intent = new Intent(this, TrimmerActivity.class);
        intent.putExtra(Utils.EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        if (intMaxDuration != 0) {
            intent.putExtra(Utils.MAX_DURATION, intMaxDuration);
        }
        startActivityForResult(intent, Utils.REQUEST_CODE_RECORD_TO_TRIM);
    }

    private void finishActivityWithReport(int reporttype, String report) {
        Intent intent = new Intent();
        intent.putExtra(Utils.REPORT, report);
        if (reporttype == Utils.REPORT_TYPE_CANCELLED) {
            setResult(Activity.RESULT_CANCELED, intent);
        } else if (reporttype == Utils.REPORT__TYPE_DONE) {
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("log", "destroyed");
    }

    public void setMaxDuration() {
        if (getIntent().getIntExtra(Utils.MAX_DURATION, 0) != 0) {
            Log.i("log", "the max duration is et to : " + getIntent().getIntExtra(Utils.MAX_DURATION, 0));
            intMaxDuration = getIntent().getIntExtra(Utils.MAX_DURATION, 0);
        } else {
            Log.i("log", "the max duration is default and the dafault will be 10 seconds. to vlaue was passed to it.");
        }
    }
}
