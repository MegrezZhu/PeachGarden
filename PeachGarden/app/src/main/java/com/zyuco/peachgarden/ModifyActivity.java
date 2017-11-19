package com.zyuco.peachgarden;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zyuco.peachgarden.library.Tools;
import com.zyuco.peachgarden.model.Character;

/**
 * Created by zhengjiafeng on 2017/11/19.
 */

public class ModifyActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText name;
    private EditText belong;
    private EditText origin;
    private EditText live;
    private EditText description;
    private EditText _abstract;
    private EditText live_from;
    private EditText live_to;
    private EditText gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        setStatusBarColor();
        render();
        addBackClickEventListener();
    }

    protected void render() {
        Character data = (Character) getIntent().getSerializableExtra("character");
        avatar = findViewById(R.id.detail_avatar);
        name = findViewById(R.id.detail_name);
        belong = findViewById(R.id.detail_belong_input);
        origin = findViewById(R.id.detail_origin_input);
        live_from = findViewById(R.id.from);
        live_to = findViewById(R.id.to);
        _abstract = findViewById(R.id.detail_abstract);
        description = findViewById(R.id.detail_desription);
        gender = findViewById(R.id.gender_input);
        // 名字
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < data.name.length(); i++) {
            text.append(data.name.charAt(i));
        }

        name.setText(text.toString());
        text.setLength(0);

        // 归属势力
        text.append(data.belong != null ? data.belong : "???");
        belong.setText(text.toString());
        text.setLength(0);

        // 籍贯
        text.append(data.origin != null ? data.origin : "???");
        origin.setText(text.toString());
        text.setLength(0);

        // 生卒
        text.append(data.from == 0 ? "?" : data.from);
        live_from.setText(text.toString());
        text.setLength(0);
        text.append(data.to == 0 ? "?" : data.to);
        live_to.setText(text.toString());
        text.setLength(0);

        //人物简介
        _abstract.setText(text.append("\t\t\t\t").append(data.abstractDescription).toString());
        text.setLength(0);

        // 历史记载
        description.setText(text.append("\t\t\t\t").append(data.description).toString());
        text.setLength(0);

        // 头像
        new Tools.LoadImagesTask(avatar).execute(data.avatar);

        //性别
        text.append(data.gender == 1 ? "男" : "女");
        gender.setText(text.toString());
        text.setLength(0);

        // 背景
        ScrollView scrollView = findViewById(R.id.detail_scroll_view);
        scrollView.setBackgroundResource(data.gender == 1 ? R.mipmap.detail_man_bg : R.mipmap.detail_woman_bg);
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
        ImageButton save = (ImageButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: modify db and return mainpage
            }
        });
    }
}
