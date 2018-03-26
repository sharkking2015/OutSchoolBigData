package com.syq.outschoolbigdata.request;


import android.os.Environment;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.functions.Func1;


/**
 * Created by yfb on 2016/7/8.
 */
public class HttpMethods {

    public static final String BASE_URL = "http://cv.zxyq.com.cn/api/";
//    public static final String SHOT_MESSAGE_URL = "http://comm.zxyq.com.cn:7080/ext/IsendSms/";
//    public static final String VIDEO_SIGN_URL = "http://api.96296edu.com:8097/mobservice/";
//    public static final String UPLOAD_URL = "http://pic.zxyq.com.cn/yepic/info/";
//    //上传视频图片返回时所需要拼接上的地址前缀
//    public static final String PIC_URL = "http://pic.zxyq.com.cn/newimg/ClassVideo/VideoPic/";
//    public static final String AVATER_URL = "http://pic.zxyq.com.cn/yepic/info/";
//    public static final String AVATER_ADD_URL = "http://pic.zxyq.com.cn/newimg/ClassVideo/Header/";
//
//    public static final String url_update_version = "http://schbd.zxyq.com.cn/tcbdVersion/";



    private static final int CONNECT_TIMEOUT = 5;
    private static final int READ_TIMEOUT = 100;
    private static final int WRITE_TIMEOUT = 60;

    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    //构造方法私有
    private HttpMethods(String url, int mode) {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.retryOnConnectionFailure(true);
        if(mode == 1){
            retrofit = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
        }else{
            retrofit = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        public T call(HttpResult<T> httpResult) {
            return httpResult.getData();
        }
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    //获取有参单例
    public static HttpMethods getInstance(String url){
        return new HttpMethods(url,2);
    }

    //获取有参单例
    public static HttpMethods getInstance(String url, int mode){
        return new HttpMethods(url,mode);
    }

    interface IUpdateVersion{
        @GET("queryMaxVersion.action")
        Observable<String> checkVersion();
    }

    public void checkVersion(Observer observer){
        IUpdateVersion updateVersion = retrofit.create(IUpdateVersion.class);
        updateVersion.checkVersion()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    interface IUserInfo{
        @GET("syncStudents.do")
        Observable<String> getUserInfo(@Query("mac")String mac);
    }

    public void getUserInfo(Observer observer,String mac){
        IUserInfo userInfo = retrofit.create(IUserInfo.class);
        userInfo.getUserInfo(mac)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    interface ITiInfo{
        @GET("getGroupVersion.do")
        Observable<String> getTiInfo(@Query("mac") String mac);
    }

    public void getTiInfo(Observer observer,String mac){
        ITiInfo tiInfo = retrofit.create(ITiInfo.class);
        tiInfo.getTiInfo(mac)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    interface IAnalysisVersion{
        @GET("syncPaperVersion.do")
        Observable<String> getAnalysisVersion();
    }

    public Observable<String> getAnalysisVersion(){
        IAnalysisVersion analysisVersion = retrofit.create(IAnalysisVersion.class);
        return analysisVersion.getAnalysisVersion();
    }

    interface ISyncInfo{
        @GET("queryUserPaperForSynchrAll.action")
        Observable<String> syncInfo(@Query("mac")String mac,@Query("synTime")String synTime);
    }

    public Observable<String> syncInfo(String mac,String syncTime){
        ISyncInfo syncInfo = retrofit.create(ISyncInfo.class);
        return syncInfo.syncInfo(mac,syncTime);
    }

    interface ISend{
        @GET("saveMacLog.do")
        Observable<String> sendMessage(@Query("macLog") String macLog);
    }

    public Observable<String> sendMessage(Observer subscriber , String macLog){
        ISend ps = retrofit.create(ISend.class);
         return ps.sendMessage(macLog)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }



    interface IUnUpload{
        @GET("saveUploadFailRecord.action")
        Observable<String> sendUnUpload(@Query("uploadFailJson") String uploadFailJson);
    }

    public void sendUnUpload(Observer subscriber,String uploadFailJson){
        IUnUpload unUpload = retrofit.create(IUnUpload.class);
        unUpload.sendUnUpload(uploadFailJson)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface IDeleteRecord{
        @GET("deleteAnswerMainByPaperIdAndUserId.action")
        Observable<String> deleteRecord(@Query("paperId") String paperId, @Query("userid") String userid);
    }

    public void deleteRecord(Observer subscriber, String paperId, String userid){
        IDeleteRecord deleteRecord = retrofit.create(IDeleteRecord.class);
        deleteRecord.deleteRecord(paperId,userid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    interface IMacStatus{
        @GET("saveMacCorrectStatus.do")
        Observable<String> macStatus(@Query("macCorrectStatus") String macCorrectStatus);
    }



    public void
    sendMacStatus(Observer subscriber,String macCorrectStatus){
        IMacStatus macStatus = retrofit.create(IMacStatus.class);
        macStatus.macStatus(macCorrectStatus)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    interface IUploadPic{
        @Multipart
        @POST("uploadUserPaperOrigin.action")
        Observable<HttpResult<Object>> UploadAvater(@Part MultipartBody.Part photo, @Part("paperId") RequestBody paperId,
                                                    @Part("tiid") RequestBody tiid, @Part("xjNum") RequestBody xjNum, @Part("uuid") RequestBody uuid,
                                                    @Part("pageNum") RequestBody pageNum, @Part("scanTime") RequestBody scanTime);
    }

    public void uploadPic(Observer subscriber,MultipartBody.Part photo,RequestBody paperId,RequestBody tiid,RequestBody xjNum,RequestBody uuid,RequestBody pageNum,RequestBody scanTime){
        IUploadPic pic = retrofit.create(IUploadPic.class);
        pic.UploadAvater(photo,paperId,tiid,xjNum,uuid,pageNum,scanTime)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(subscriber);
    }

    interface IUploadSmallPic{
        @Multipart
        @POST("uploadUserPaper.action")
        Observable<HttpResult<Object>> uploadSmallPic(@Part MultipartBody.Part photo, @Part("paperId") RequestBody paperId,
                                                      @Part("tiid") RequestBody tiid, @Part("xjNum") RequestBody xjNum);
    }

    public void UploadSmallPic(Observer subscriber,MultipartBody.Part photo,RequestBody paperId,RequestBody tiid,RequestBody xjNum){
        IUploadSmallPic uploadSmallPic = retrofit.create(IUploadSmallPic.class);
        uploadSmallPic.uploadSmallPic(photo,paperId,tiid,xjNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface IUploadData{
        @Multipart
        @POST("syncDataBase.do")
        Observable<HttpResult<Object>> uploadDataBase(@Part MultipartBody.Part dataBase, @Part("macId") RequestBody macId, @Part("tiid") RequestBody institueId);
    }

    /**
     * 上传机器数据库的接口
     * @param subscriber
     * @param dataBase
     * @param macId
     * @param institueId
     */
    public void UploadDataBase(Observer subscriber,MultipartBody.Part dataBase,RequestBody macId,RequestBody institueId){
        IUploadData uploadData = retrofit.create(IUploadData.class);
        uploadData.uploadDataBase(dataBase,macId,institueId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    interface IUploadLog{
        @Multipart
        @POST("syncLogs.do")
        Observable<HttpResult<Object>> uploadLog(@Part MultipartBody.Part dataBase, @Part("macId") RequestBody macId, @Part("tiid") RequestBody institueId);
    }

    /**
     * 上传机器日志的接口
     * @param subscriber
     * @param log
     * @param macId
     * @param institueId
     */
    public void UploadLog(Observer subscriber,MultipartBody.Part log,RequestBody macId,RequestBody institueId){
        IUploadLog uploadData = retrofit.create(IUploadLog.class);
        uploadData.uploadLog(log,macId,institueId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }


    interface IUploadPaperPages{
        @Multipart
        @POST("syncErrorImge.do")
        Observable<HttpResult<Object>> uploadPages(@Part MultipartBody.Part dataBase, @Part("macId") RequestBody macId, @Part("tiid") RequestBody institueId
                , @Part("stuId") RequestBody stuId, @Part("guid") RequestBody guid, @Part("paperId") RequestBody paperId);
    }

    /**
     * 用户手动上传某一份记录的全部试卷
     * @param subscriber
     * @param log
     * @param macId
     * @param institueId
     */
    public void UploadPaperPages(Observer subscriber, MultipartBody.Part log, RequestBody macId, RequestBody institueId, RequestBody stuId, RequestBody guid, RequestBody paperId){
        IUploadPaperPages uploadData = retrofit.create(IUploadPaperPages.class);
        uploadData.uploadPages(log,macId,institueId,stuId,guid,paperId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }
}
