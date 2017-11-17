package com.zyuco.peachgarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starup);

        // 在这里初始化数据库
        // 完成后跳去MainActivity
    }
}
