package com.syq.outschoolbigdata.vo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.syq.outschoolbigdata.utils.MyLog;

/**
 * Created by yfb on 2018/3/9.
 */

public class LocalLogBean {
    private String message;
    private String errorInfo;


    public LocalLogBean(String message, String errorInfo) {
        this.message = message;
        this.errorInfo = errorInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
