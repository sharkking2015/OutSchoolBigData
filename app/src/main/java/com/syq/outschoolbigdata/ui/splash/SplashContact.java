package com.syq.outschoolbigdata.ui.splash;

import com.syq.outschoolbigdata.ui.base.IBaseView;

/**
 * Created by yfb on 2018/3/8.
 */

public class SplashContact {
    interface ISplashPresenter{

    }

    interface ISplashView extends IBaseView{
        void updateProgressDialog(int precent,int type);
    }
}
