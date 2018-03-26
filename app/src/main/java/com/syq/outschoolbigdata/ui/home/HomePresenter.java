package com.syq.outschoolbigdata.ui.home;

import android.content.Context;

import com.syq.outschoolbigdata.MyApplication;
import com.syq.outschoolbigdata.db.UserBeanDao;
import com.syq.outschoolbigdata.request.HttpMethods;
import com.syq.outschoolbigdata.request.RequestUrls;
import com.syq.outschoolbigdata.ui.adapter.UserAdapter;
import com.syq.outschoolbigdata.ui.base.BasePresenter;
import com.syq.outschoolbigdata.utils.MyLog;
import com.syq.outschoolbigdata.utils.PhoneSerialInfoHelper;
import com.syq.outschoolbigdata.vo.orm.UserBean;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by yfb on 2018/3/9.
 */

public class HomePresenter extends BasePresenter implements HomeContact.IHomePresenter {
    public static final String TAG = "HomePresenter";

    HomeContact.IHomeView IView;
    private ArrayList<UserBean> list = new ArrayList<>();
    private UserAdapter adapter;


    public HomePresenter(Context context, HomeContact.IHomeView IView) {
        super(context);
        this.IView = IView;
    }

    @Override
    protected void init() {
//        getListFromNet();
        getListFromLocal();
    }

    private void getListFromLocal(){
        List<UserBean> queryList = MyApplication.getInstances().getDaoSession().getUserBeanDao().queryBuilder()
                .build().list();
        list.addAll(queryList);
        adapter = new UserAdapter(context,list);
        IView.initRecycler(adapter);
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
                    for(int i=0;i<array.length();i++){
                        JSONObject userObj = array.getJSONObject(i);
                        UserBean ub = new UserBean();
                        ub.setAvater(userObj.getString("headimg"));
                        ub.setName(userObj.getString("userName"));
                        ub.setScore(userObj.getString("userScore"));
                        list.add(ub);
                    }
                    adapter = new UserAdapter(context,list);
                    IView.initRecycler(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyLog.i(TAG,"getListFromNet:"+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        MyLog.i(TAG,"getUserInfo:http://schbd.zxyq.com.cn/syncMac/syncStudents.do?mac="+PhoneSerialInfoHelper.getSerial());
        HttpMethods.getInstance(RequestUrls.url_user).getUserInfo(observer, PhoneSerialInfoHelper.getSerial());
    }
}
