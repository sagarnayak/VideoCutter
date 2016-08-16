package com.csm.androidlibrary.lib;

/**
 * Created by sagar on 8/12/2016.
 */
public class Utils {

    public static final int REQUEST_CODE_PEFORM_VIDEO_TRIM = 247;
    public static final int REQUEST_CODE_RECORD_TO_TRIM = 248;
    public static final int REQUEST_VIDEO_TRIMMER = 0x01;
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

    public static final String operation_type = "Type";
    public static final int PERFORM_CHOOSE_FROM_GALLERY_OR_RECORD_VIDEO = 1;
    public static final int PERFORM_CHOOSE_FROM_GALLERY = 2;
    public static final int PERFORM_CHOOSE_RECORD_VIDEO = 3;
    public static final int PERFORM_SEND_PATH = 4;
    public static final String FILE_PATH = "path";
    public static final String REPORT = "report";
    public static final String MAX_DURATION = "duration";
    public static final int REPORT__TYPE_DONE = 1;
    public static final int REPORT_TYPE_CANCELLED = 2;

    public static final String REPORT_CODE = "report_code";
    //error code list
    public static final int REPORT_CODE_SUCCESS = 100;
    public static final int REPORT_CODE_FILE_PATH_NOT_VALID = 101;
    public static final int REPORT_CODE_NO_FILE_PATH_FOUND = 102;
    public static final int REPORT_CODE_INVALID_OPERATION_TYPE = 103;
    public static final int REPORT_CODE_DIALOG_CANCELLED = 104;
    public static final int REPORT_CODE_OUTPUT_FILE_CAN_NOT_BE_READ = 105;
    public static final int REPORT_CODE_INAPPROPRIATE_SELECTION = 106;
    public static final int REPORT_CODE_TRIMMING_CANCELLED = 107;
    public static final int REPORT_CODE_ERROR_DURING_TRIMMING_VIDEO = 108;
}
