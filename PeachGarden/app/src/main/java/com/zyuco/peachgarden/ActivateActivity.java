package com.zyuco.peachgarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivateActivity extends AppCompatActivity {
    private int activateCount = 3;
    private TextView activateCountView;
    private ActivateActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        initListener();
    }

    private void initListener() {
        activateCountView = (TextView)findViewById(R.id.tv_activate_count);
        activateCountView.setText(number2String(activateCount));
        findViewById(R.id.btn_activate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activateCount > 1) {
                    String str = number2String(--activateCount);
                    activateCountView.setText(str);
                } else {
                    startActivity(new Intent(context, ActivatedActivity.class));
                    context.finish();
                    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private String number2String(int num) {
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
