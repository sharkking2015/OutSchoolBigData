package com.syq.outschoolbigdata.ui.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.syq.outschoolbigdata.MyApplication;
import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.core.ImageDealer;
import com.syq.outschoolbigdata.ui.base.BasePresenter;
import com.syq.outschoolbigdata.utils.MyLog;
import com.syq.outschoolbigdata.utils.ThreadPoolUtil;

import java.io.File;

/**
 * Created by yfb on 2018/3/19.
 */

public class TestPresenter extends BasePresenter implements TestContact.ITestPresenter{
    public static final String TAG = "TestPresenter";

    private TestContact.ITestView IView;


    public TestPresenter(Context context,TestContact.ITestView IView) {
        super(context);
        this.IView = IView;
    }

    @Override
    protected void init() {

    }

    @Override
    public void btn1Click() {
        ImageDealer dealer = new ImageDealer(context);
        IView.showPic(dealer.dealBitmap());
    }

    @Override
    public void btn2Click() {
        ImageDealer dealer = new ImageDealer(context);
        IView.showPic(dealer.Canndy(R.drawable.test_pic7));
    }

    @Override
    public void btn3Click() {
        ImageDealer dealer = new ImageDealer(context);
        IView.showPic(dealer.adaptiveThreshold(R.drawable.test_pic8));
    }

    @Override
    public void btn4Click(final int start,final int end) {
        ThreadPoolUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String dir = Environment.getExternalStorageDirectory() + "/downloadPics4/";
                MyLog.i("urlslength",dir);
                File f = new File(dir);
                String[] urls = f.list();
                MyLog.i("urlslength",urls.length+"");
                String temp;
                for (int i = 0; i < urls.length - 1; i++) {
                    for (int j = 0; j < urls.length - 1 - i; j++) {
                        if (urls[j].compareTo(urls[j + 1]) > 0) {
                            temp = urls[j];
                            urls[j] = urls[j + 1];
                            urls[j + 1] = temp;
                        }
                    }
                }

                for (int i = start; i < end; i++) {
                    String path = dir + urls[i];
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    ImageDealer dealer = new ImageDealer(context);
                    int size = dealer.adaptiveThreshold(bmp);
                    MyLog.i("anchorSize",size+"");
                }
            }
        });
    }

    @Override
    public void btn5Click(final int start, final int end) {
        ThreadPoolUtil.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String dir = Environment.getExternalStorageDirectory() + "/downloadPics4/";
                MyLog.i("urlslength",dir);
                File f = new File(dir);
                String[] urls = f.list();
                MyLog.i("urlslength",urls.length+"");
                String temp;
                for (int i = 0; i < urls.length - 1; i++) {
                    for (int j = 0; j < urls.length - 1 - i; j++) {
                        if (urls[j].compareTo(urls[j + 1]) > 0) {
                            temp = urls[j];
                            urls[j] = urls[j + 1];
                            urls[j + 1] = temp;
                        }
                    }
                }

                for (int i = start; i < end; i++) {
                    String path = dir + urls[i];
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    ImageDealer dealer = new ImageDealer(context);
                    int size = dealer.adaptiveThreshold(bmp);
                    MyLog.i("anchorSize",size+"");
                }
            }
        });
    }
}
