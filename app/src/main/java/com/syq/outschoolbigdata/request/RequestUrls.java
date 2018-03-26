package com.syq.outschoolbigdata.request;

import android.os.Environment;

import java.io.File;

/**
 * Created by yfb on 2018/3/9.
 */

public class RequestUrls {
    public static final String BASE_URL = "http://cv.zxyq.com.cn/api/";
    public static final String SHOT_MESSAGE_URL = "http://comm.zxyq.com.cn:7080/ext/IsendSms/";
    public static final String VIDEO_SIGN_URL = "http://api.96296edu.com:8097/mobservice/";
    public static final String UPLOAD_URL = "http://pic.zxyq.com.cn/yepic/info/";
    //上传视频图片返回时所需要拼接上的地址前缀
    public static final String PIC_URL = "http://pic.zxyq.com.cn/newimg/ClassVideo/VideoPic/";
    public static final String AVATER_URL = "http://pic.zxyq.com.cn/yepic/info/";
    public static final String AVATER_ADD_URL = "http://pic.zxyq.com.cn/newimg/ClassVideo/Header/";

    public static final String url_new = "http://schbd.zxyq.com.cn/tcbdPaper/";
    public static final String url_user = "http://schbd.zxyq.com.cn/syncMac/";
    public static final String url_update_version = "http://schbd.zxyq.com.cn/tcbdVersion/";


    //解析文档路径
    public static final String PAPER_FORM_URL = "http://file2.cxjyzy.com:8099/trainingCenterBigData/paperAnalysis.zip";
    public static final String PAPER_ANSWER_URL = "http://file2.cxjyzy.com:8099/trainingCenterBigData/paperAnswer.zip";

    /**
     * apk本地下载路径
     * @return
     */
    public static String getLocalApkPath(){
        String dir = Environment.getExternalStorageDirectory()+ File.separator+"OutSchoolBigData"+File.separator+"APK";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdirs();
        }
        return dir+File.separator+"OutSchoolBigData.apk";
    }

    /**
     * 解析文档本地下载路径
     * @return
     */
    public static String getLocalAnalysisPath(){
        String dir = Environment.getExternalStorageDirectory()+ File.separator+"OutSchoolBigData"+File.separator+"analysis";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdirs();
        }
        return dir+File.separator+"analysis.zip";
    }

    /**
     * 答案文档本地下载路径
     * @return
     */
    public static String getLocalAnswerPath(){
        String dir = Environment.getExternalStorageDirectory()+ File.separator+"OutSchoolBigData"+File.separator+"answer";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdirs();
        }
        return dir+File.separator+"answer.zip";
    }

}
