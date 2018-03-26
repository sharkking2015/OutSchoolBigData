package com.syq.outschoolbigdata.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.ui.adapter.UserAdapter;
import com.syq.outschoolbigdata.ui.base.BaseActivity;
import com.syq.outschoolbigdata.ui.test.TestActivity;
import com.syq.outschoolbigdata.utils.MyLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by yfb on 2018/3/9.
 */

public class HomeActivity extends BaseActivity implements HomeContact.IHomeView{
    private HomePresenter presenter;

    private RecyclerView recyclerView;
    private RelativeLayout rightLayout;
    private Button goAnalysisBtn;


    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addStatusViewWithColor(this,getResources().getColor(R.color.black));

        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        goAnalysisBtn = (Button) findViewById(R.id.go_analysis_btn);

        goAnalysisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(HomeActivity.this, TestActivity.class));
            }
        });

        presenter = new HomePresenter(this,this);
        presenter.init();
    }

    @Override
    public void initRecycler(UserAdapter adapter) {
        this.adapter = adapter;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshRecycler() {

    }
}
