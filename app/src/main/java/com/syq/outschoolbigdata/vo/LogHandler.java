package com.syq.outschoolbigdata.vo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.syq.outschoolbigdata.utils.MyLog;

/**
 * Created by yfb on 2018/3/15.
 */

public class LogHandler {
    private static LogHanler mLogHandler;


    public static void addToHandler(LocalLogBean bean){
        if(mLogHandler == null){
            HandlerThread thread = new HandlerThread("SaveLocalLog", Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
            Looper looper = thread.getLooper();
            mLogHandler = new LogHanler(looper);
        }

        Message msg = mLogHandler.obtainMessage();
        msg.obj = bean;
        mLogHandler.sendMessage(msg);
    }

    private final static class LogHanler extends Handler {
        public LogHanler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            LocalLogBean bean = (LocalLogBean)msg.obj;
            MyLog.i("LogHandler",Thread.currentThread().getName());
            MyLog.i("LogHandler",bean.getMessage());
            MyLog.i("LogHandler",bean.getErrorInfo());
        }
    }
}
