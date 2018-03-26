package com.syq.outschoolbigdata.ui.home;


import com.syq.outschoolbigdata.ui.adapter.UserAdapter;

/**
 * Created by yfb on 2018/3/9.
 */

public class HomeContact {
    interface IHomePresenter{

    }
    interface IHomeView{
        public void initRecycler(UserAdapter adapter);
        public void refreshRecycler();
    }
}
