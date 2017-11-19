package com.zyuco.peachgarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UnlockActivity extends AppCompatActivity {
    private int activateCount;
    private int currentCount;
    private TextView activateCountView;
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
        // activateCount = sharedPref.getInt(getString(R.string.saved_activate_count), 2);
        activateCount = 2;
    }

    private void initListener() {
        activateCountView = (TextView)findViewById(R.id.tv_activate_count);
        activateCountView.setText(number2String(activateCount));
        findViewById(R.id.btn_activate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateCount - currentCount > 0) {
                    currentCount++;
                    String str = number2String(activateCount - currentCount);
                    activateCountView.setText(str);
                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    activateCount *= 2;
                    editor.putInt(getString(R.string.saved_activate_count), activateCount);
                    editor.apply();
                    startActivity(new Intent(context, UnlockingActivity.class));
                    context.finish();
                    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private String number2String(int num) {
        if (num == 0) return "解锁";
        String[] NUMBER_TABLE = {"零", "壹","贰","叁","肆","伍","陆","柒","捌","玖"};
        String[] UNIT_TABLE = {"", "拾", "佰", "仟", "万"};
        String numberString = new StringBuffer(String.valueOf(num)).reverse().toString().trim();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberString.length(); i++) {
            builder.append(UNIT_TABLE[i]);
            builder.append(NUMBER_TABLE[numberString.charAt(i) - 48]);
        }
        builder.reverse();
        String result = builder.toString();
        result = result.replaceAll("零[拾佰仟]", "零");
        result = result.replaceAll("零万", "万");
        while (result.charAt(result.length() - 1) == '零') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
