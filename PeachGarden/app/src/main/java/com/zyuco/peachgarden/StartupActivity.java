package com.zyuco.peachgarden;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.DbWriter;

import java.lang.ref.WeakReference;

public class StartupActivity extends AppCompatActivity {
    private static final String TAG = "PeachGarden.Startup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在这里初始化数据库
        // 完成后跳去MainActivity
        final Handler handler = new CounterHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "db init start");
                DbReader.getInstance(StartupActivity.this); // trigger db initialization
                DbWriter.getInstance(StartupActivity.this);
                Log.i(TAG, "db init done");

                handler.obtainMessage().sendToTarget();
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }
        }, 500);
    }

    private static class CounterHandler extends Handler {
        private WeakReference<StartupActivity> ref;
        int count = 0;

        CounterHandler(StartupActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            count++;
            if (count == 2) { // db initialized, and startup activity has displayed for at least 1 second
                go();
            }
        }

        private void go() {
            StartupActivity context = ref.get();
            if (ref == null) return;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            context.finish();
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
