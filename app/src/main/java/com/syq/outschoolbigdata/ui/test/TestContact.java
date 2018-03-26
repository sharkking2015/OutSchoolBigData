package com.syq.outschoolbigdata.ui.test;

import android.graphics.Bitmap;

/**
 * Created by yfb on 2018/3/19.
 */

public class TestContact {
    interface ITestPresenter{
        void btn1Click();
        void btn2Click();
        void btn3Click();
        void btn4Click(int start,int end);
        void btn5Click(int start,int end);
    }

    interface ITestView{
        void showPic(Bitmap bitmap);
    }
}
