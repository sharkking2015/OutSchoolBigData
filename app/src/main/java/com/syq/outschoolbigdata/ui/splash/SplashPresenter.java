package com.syq.outschoolbigdata.ui.splash;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.syq.outschoolbigdata.MyApplication;
import com.syq.outschoolbigdata.request.HttpMethods;
import com.syq.outschoolbigdata.request.RequestUrls;
import com.syq.outschoolbigdata.ui.base.BasePresenter;
import com.syq.outschoolbigdata.ui.home.HomeActivity;
import com.syq.outschoolbigdata.utils.Constant;
import com.syq.outschoolbigdata.utils.DBUtil;
import com.syq.outschoolbigdata.utils.MyLog;
import com.syq.outschoolbigdata.utils.PhoneSerialInfoHelper;
import com.syq.outschoolbigdata.utils.ThreadPoolUtil;
import com.syq.outschoolbigdata.utils.ZipUtil;
import com.syq.outschoolbigdata.vo.LocalLogBean;
import com.syq.outschoolbigdata.vo.LogHandler;
import com.syq.outschoolbigdata.vo.orm.TimeTags;
import com.syq.outschoolbigdata.vo.orm.UserBean;
import com.syq.outschoolbigdata.vo.orm.TiInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yfb on 2018/3/8.
 */

public class SplashPresenter extends BasePresenter implements SplashContact.ISplashPresenter{
    public static final String TAG = "SplashPresenter";
    public static final int SHOW_DIALOG = 0;
    public static final int DISMISS_DIALOG = 1;
    public static final int UPDATE_DIALOG_PERCENT = 2;

    public  boolean SYNC_STATE = false;     //同步是否完成
    public  boolean ANALYSIS_STATE = false;//解析文档是否下载完成
    public  boolean ANSWER_STATE = false;   //答案文档是否下载完成

    public static final int TYPE_APK = 0;   //下载类型为apk文件
    public static final int TYPE_ANALYSIS = 1;   //下载类型为File文件
    public static final int TYPE_ANSWER = 2;   //下载类型为File文件

    private boolean openWifi = false; //是否已经尝试打开wifi

    SplashContact.ISplashView IView;
    Handler handler = new Handler();
    int currentTime;//循环的秒数
    private TimeTags timeTags; //数据库中记录时间版本号等用的表

    public SplashPresenter(Context context, SplashContact.ISplashView iSplashView) {
        super(context);
        this.IView = iSplashView;
    }

    @Override
    protected void init() {
        if(MyLog.DEBUG){
            IView.goActivity(HomeActivity.class);
            return;
        }
        timeTags = DBUtil.getTimeTags();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //记录秒数加1
                currentTime++;

                if(preCheckNetwork()){
                    //有网络连接,首先检测更新
                    checkVersion();
                    return;
                }else{
                    //没有网络，打开wifi
                    if(openWifi){
                        //大于20秒，还没有网络，则跳转到主界面
                        if(currentTime > 20){
                            //20秒还未连上网络，跳转到主界面
                            IView.goActivity(HomeActivity.class);
                        }
                    }else{
                        openWifi();
                        openWifi = true;
                    }
                }
                handler.postDelayed(this,1000);
            }
        },1000);
    }

    /**
     * 检测是否有版本更新
     */
    private void checkVersion(){
        HttpMethods.getInstance(RequestUrls.url_update_version).checkVersion(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                MyLog.i(TAG,value);
                try {
                    JSONObject object = new JSONObject(value);
                    JSONObject dataObject = object.getJSONObject("data");
                    int versionNum = dataObject.getInt("versionNum");
                    int localVersion = getVersionCode();
                    String ftp_Path = dataObject.getString("ftp_Path");

                    if(localVersion < versionNum){
                        //线上版本号大于本地，开始下载
                        MyLog.i(TAG,"start download Apk");
                        downLoadFile(ftp_Path,RequestUrls.getLocalApkPath(),TYPE_APK,versionNum);
                    }else{
                        //暂时没有更新
                        getTiInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHandler.addToHandler(new LocalLogBean("checkVersion",e.getMessage()));
                IView.goActivity(HomeActivity.class);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    interface IDownload {
        @Streaming
        @GET
        Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
    }

    /**
     * 下载文件
     * @param url 线上文件地址
     * @param localPath 下载到的地址
     */
    private void downLoadFile(final String url,final String localPath,final int type,final int version){
        IView.updateProgressDialog(0,SHOW_DIALOG);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RequestUrls.url_new)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        final IDownload downlaod = retrofit.create(IDownload.class);
        Call<ResponseBody> call = downlaod.downloadFileWithDynamicUrlAsync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                //放入线程中执行
                ThreadPoolUtil.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.i(TAG,"downLoadApk:success");
                        boolean writeResult = writeResponseBodyToDisk(response.body(),localPath);
                        if(writeResult){
                           switch (type){
                               case TYPE_APK:

                                   break;
                               case TYPE_ANALYSIS:
                                   //解压，解压完成后判断是否需要跳转
                                   boolean analysisResult = ZipUtil.upZipFile(localPath);
                                   if(analysisResult){
                                       timeTags.setAnalysisVersion(version);
                                       MyApplication.getInstances().getDaoSession().getTimeTagsDao().insertOrReplace(timeTags);
                                       ANALYSIS_STATE = true;
                                       shouldGoHome();
                                   }else{
                                       //解压出错，记录
                                       LogHandler.addToHandler(new LocalLogBean("解析解压报错","error"));
                                       ANALYSIS_STATE = true;
                                       shouldGoHome();
                                   }
                                   break;
                               case TYPE_ANSWER:
                                   boolean answerResult = ZipUtil.upZipFile(localPath);
                                   //解压，解压完成后判断是否需要跳转
                                   if(answerResult){
                                       timeTags.setAnswerVersion(version);
                                       MyApplication.getInstances().getDaoSession().getTimeTagsDao().insertOrReplace(timeTags);
                                       ANSWER_STATE = true;
                                       shouldGoHome();
                                   }else{
                                       //解压出错，记录
                                       LogHandler.addToHandler(new LocalLogBean("答案解压报错","error"));
                                       ANALYSIS_STATE = true;
                                       shouldGoHome();
                                   }
                                   break;
                               default:
                                   break;
                           }
                        }else{

                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogHandler.addToHandler(new LocalLogBean("downLoadFile:"+type+url,t.getMessage()));
            }
        });
    }

    /**
     * 将ResponseBody的内容存成文件
     *
     * @param body
     * @return
     */
    public  boolean writeResponseBodyToDisk(ResponseBody body, String localUrl) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(localUrl);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    int percent = (int) (fileSizeDownloaded * 100 / fileSize);
                    IView.updateProgressDialog(percent,UPDATE_DIALOG_PERCENT);
                    MyLog.i("retrofit_download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                IView.updateProgressDialog(100,DISMISS_DIALOG);
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 获取机构信息
     */
    private void getTiInfo(){
        io.reactivex.Observer<String> observer = new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                MyLog.i(TAG,"getTiInfo:"+value);
                try {
                    JSONObject object = new JSONObject(value);
                    JSONObject tiObject = object.getJSONObject("data");
                    TiInfo tiInfo = MyApplication.getInstances().getDaoSession().getTiInfoDao().queryBuilder()
                            .build().unique();
                    if(tiInfo == null){
                        //数据库里没有机构信息，则保存
                        tiInfo = new TiInfo(0,tiObject.getString("tiId"),tiObject.getString("tiName"),tiObject.getString("tiVersion"));
                    }else{
                        //库里有机构信息
                        if(tiInfo.getTiId().equals(tiObject.getString("tiId"))){
                            //如果机构id相同
                            tiInfo.setTiName(tiObject.getString("tiName"));
                            tiInfo.setTiVersion(tiObject.getString("tiVersion"));
                        }else{
                            //如果机构id不相同
                            tiInfo.setTiId(tiObject.getString("tiId"));
                            tiInfo.setTiName(tiObject.getString("tiName"));
                            tiInfo.setTiVersion(tiObject.getString("tiVersion"));
                            //切换机构，则把之前所有的数据全部清空
                            deleteData();
                        }
                    }
                    //插入或者更新
                    MyApplication.getInstances().getDaoSession().getTiInfoDao().insertOrReplace(tiInfo);
                    //继续调用获取学生信息接口
                    getListFromNet();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogHandler.addToHandler(new LocalLogBean("getTiInfo:"+RequestUrls.url_user+"?mac="+PhoneSerialInfoHelper.getSerial(),e.getMessage()));
                IView.goActivity(HomeActivity.class);
            }

            @Override
            public void onComplete() {

            }
        };
        HttpMethods.getInstance(RequestUrls.url_user).getTiInfo(observer, PhoneSerialInfoHelper.getSerial());
    }

    /**
     * 获取学生列表数据
     */
    private void getListFromNet(){
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                MyLog.i(TAG,value);
                try {
                    JSONObject object = new JSONObject(value);
                    JSONArray array = object.getJSONArray("data");
                    ArrayList<UserBean> list = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject userObj = array.getJSONObject(i);
                        UserBean ub = new UserBean();
                        ub.setAvater(userObj.getString("headimg"));
                        ub.setName(userObj.getString("userName"));
                        ub.setScore(userObj.getString("userScore"));
                        list.add(ub);
                    }
                    //插入数据库
                    MyApplication.getInstances().getDaoSession().getUserBeanDao().deleteAll();
                    MyApplication.getInstances().getDaoSession().getUserBeanDao().insertInTx(list);
                    //检测解析文档是否需要下载
                    checkAnalysis();
                    //继续调用获取考试记录的接口
                    long lastTime = timeTags.getOneDayTime();
                    //时间大于1天则执行全量更新
                    if(System.currentTimeMillis() - lastTime > Constant.ONE_DAY){
                        syncAllInfo();
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.i(TAG,"getListFromNet:"+e.getMessage());
                LogHandler.addToHandler(new LocalLogBean("getListFromNet:"+RequestUrls.url_user+"&mac="+PhoneSerialInfoHelper.getSerial(),e.getMessage()));
                IView.goActivity(HomeActivity.class);
            }

            @Override
            public void onComplete() {

            }
        };
        MyLog.i(TAG,"getUserInfo:http://schbd.zxyq.com.cn/syncMac/syncStudents.do?mac="+PhoneSerialInfoHelper.getSerial());
        HttpMethods.getInstance(RequestUrls.url_user).getUserInfo(observer, PhoneSerialInfoHelper.getSerial());
    }


    private void checkAnalysis(){
        Observable<String> observable = HttpMethods.getInstance(RequestUrls.url_user).getAnalysisVersion();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        try{
                            JSONObject object = new JSONObject(value).getJSONObject("data");
                            int paperAnalysisVersion = object.getInt("paperAnalysisVersion");
                            int paperAnswerVersion = object.getInt("paperAnswerVersion");
                            if(paperAnalysisVersion > timeTags.getAnalysisVersion()){
                                //线上解析文档版本号大于本地的，则下载
                                downLoadFile(RequestUrls.PAPER_FORM_URL,RequestUrls.getLocalAnalysisPath(),TYPE_ANALYSIS,paperAnalysisVersion);
                            }else{
                                //不大于，则直接标记解析文档下载完成
                                ANALYSIS_STATE = true;
                            }

                            if(paperAnswerVersion > timeTags.getAnswerVersion()){
                                downLoadFile(RequestUrls.PAPER_ANSWER_URL,RequestUrls.getLocalAnswerPath(),TYPE_ANSWER,paperAnswerVersion);
                            }else{
                                //线上答案包版本号不大于本地答案包版本号，直接显示答案更新完成，判断是否需要跳转主界面
                                ANSWER_STATE = true;
                                shouldGoHome();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHandler.addToHandler(new LocalLogBean("checkAnalysis:"+RequestUrls.url_user+"?mac="+PhoneSerialInfoHelper.getSerial(),e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void syncAllInfo(){
        Observable<String> observable = HttpMethods.getInstance(RequestUrls.url_user).syncInfo(PhoneSerialInfoHelper.getSerial(),"");
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        MyLog.i(TAG,"syncAllInfo:"+value);
                        SYNC_STATE = true;
                        shouldGoHome();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogHandler.addToHandler(new LocalLogBean("syncAllInfo:"+RequestUrls.url_user+"?mac="+PhoneSerialInfoHelper.getSerial()+"&syncTime="+"",e.getMessage()));
                        SYNC_STATE = true;
                        shouldGoHome();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设备绑定机构变更，删除数据库相关信息
     */
    private void deleteData(){

    }

    /**
     * 判断是否跳到主界面
     * 如果同步完成并且解析下载完成，则跳转
     */
    private void shouldGoHome(){
        MyLog.i(TAG,"shouldGoHome:"+SYNC_STATE+" "+ANALYSIS_STATE+" "+ANSWER_STATE);
        if(SYNC_STATE && ANALYSIS_STATE && ANSWER_STATE){
            IView.goActivity(HomeActivity.class);
        }
    }

    /**
     * 打开wifi
     *
     * @return string型的ip地址
     */
    private String openWifi() {
        String ip = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
}
