package com.zyuco.peachgarden;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * Created by zhengjiafeng on 2017/11/19.
 */

public class ModifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        setStatusBarColor();
        addBackClickEventListener();
    }
    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xF5F5F5);
    }
    protected void addBackClickEventListener() {
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyActivity.this.finish();
            }
        });
    }
}
