package com.syq.outschoolbigdata.ui.analysis;

import android.content.Context;

import com.syq.outschoolbigdata.ui.base.BasePresenter;

/**
 * Created by yfb on 2018/3/14.
 */

public class AnalysisPresenter extends BasePresenter implements AnalysisConcat.IAnalysisPresenter {
    private AnalysisConcat.IAnalysisView IView;

    public AnalysisPresenter(Context context,AnalysisConcat.IAnalysisView IView) {
        super(context);
        this.IView = IView;
    }

    @Override
    protected void init() {

    }
}
