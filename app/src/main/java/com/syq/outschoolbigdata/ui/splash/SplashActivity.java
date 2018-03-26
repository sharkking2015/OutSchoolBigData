package com.syq.outschoolbigdata.ui.splash;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity implements SplashContact.ISplashView{

    SplashPresenter presenter;

    //下载进度条
    ProgressDialog m_pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        addStatusViewWithColor(this,getResources().getColor(android.R.color.transparent));
        initProgressDialog();
        presenter = new SplashPresenter(this,this);
        presenter.init();
    }

    private void initProgressDialog(){
        if (m_pDialog == null) {
            m_pDialog = new ProgressDialog(SplashActivity.this);
        }
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        m_pDialog.setTitle("下载进度");
        m_pDialog.setProgress(0);
        m_pDialog.setIndeterminate(false);
        m_pDialog.setCancelable(false);
        m_pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    @Override
    public void updateProgressDialog(int precent, int type) {
        if(m_pDialog != null){
            if(type == 0){
                m_pDialog.show();
            }else if(type == 1){
                m_pDialog.dismiss();
            }else{
                m_pDialog.setProgress(precent);
            }
        }
    }

    @Override
    public void goActivity(Class cls) {
        startActivity(new Intent(SplashActivity.this,cls));
        SplashActivity.this.finish();
    }
}
