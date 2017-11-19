package com.zyuco.peachgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zyuco.peachgarden.library.Tools;

public class UnlockActivity extends AppCompatActivity {
    private int unlockCount;
    private int currentCount;
    private TextView unlockCountView;
    private UnlockActivity context = this;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        initData();
        initListener();
    }

    private void initData() {
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        unlockCount = sharedPref.getInt(getString(R.string.saved_unlock_count), 2);
        // unlockCount = 2;
    }

    private void initListener() {
        unlockCountView = (TextView)findViewById(R.id.tv_unlock_count);
        unlockCountView.setText(Tools.num2String(unlockCount));
        findViewById(R.id.btn_unlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unlockCount - currentCount > 0) {
                    currentCount++;
                    String str = Tools.num2String(unlockCount - currentCount);
                    unlockCountView.setText(str);
                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    unlockCount *= 2;
                    editor.putInt(getString(R.string.saved_unlock_count), unlockCount);
                    editor.apply();
                    startActivity(new Intent(context, UnlockingActivity.class));
                    context.finish();
                    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }
}
