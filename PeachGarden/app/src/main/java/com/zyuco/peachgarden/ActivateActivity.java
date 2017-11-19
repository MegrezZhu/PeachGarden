package com.zyuco.peachgarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

public class ActivateActivity extends AppCompatActivity {
    private int activateCount = 100;
    private TextView activateCountView;

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
                String str = number2String(--activateCount);
                activateCountView.setText(str);
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
