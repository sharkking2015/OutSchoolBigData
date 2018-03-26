package com.syq.outschoolbigdata.ui.analysis;

import android.os.Bundle;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.ui.base.BaseActivity;

public class AnalysisActivity extends BaseActivity implements AnalysisConcat.IAnalysisView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
    }
}
