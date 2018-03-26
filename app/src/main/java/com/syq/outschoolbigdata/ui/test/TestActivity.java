package com.syq.outschoolbigdata.ui.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.ui.base.BaseActivity;

public class TestActivity extends BaseActivity implements TestContact.ITestView {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private ImageView image;
    private EditText startEdit;
    private EditText endEdit;

    private TestPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        addStatusViewWithColor(this,getResources().getColor(R.color.white));
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        image = (ImageView) findViewById(R.id.image);
        startEdit = (EditText) findViewById(R.id.start_edit);
        endEdit = (EditText) findViewById(R.id.end_edit);
        presenter = new TestPresenter(this,this);
        presenter.init();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btn1Click();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btn2Click();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.btn3Click();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start = Integer.parseInt(startEdit.getText().toString());
                int end = Integer.parseInt(endEdit.getText().toString());
                presenter.btn4Click(start,end);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int start = Integer.parseInt(startEdit.getText().toString());
                int end = Integer.parseInt(endEdit.getText().toString());
                presenter.btn5Click(start,end);
            }
        });
    }

    @Override
    public void showPic(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}
