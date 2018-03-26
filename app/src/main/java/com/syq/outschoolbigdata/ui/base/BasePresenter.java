package com.syq.outschoolbigdata.ui.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yfb on 2018/3/8.
 */

public abstract class BasePresenter {
    protected Context context;

    public BasePresenter(Context context){
        this.context = context;
    }

    protected abstract void init();
    /**
     * 预先检查网络情况 （有相关权限）
     *
     * @return 真的网络可用 ，假则不可用
     */
    protected boolean preCheckNetwork() {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错
            return networkInfo.isAvailable();
        }
        return false;
    }

    protected int getVersionCode()// 获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
