package com.zyuco.peachgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyuco.peachgarden.library.Tools;
import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengjiafeng on 2017/11/19.
 */

public class ModifyActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText name;
    private EditText belong;
    private EditText origin;
    private EditText description;
    private EditText _abstract;
    private EditText live_from;
    private EditText live_to;
    private EditText gender;
    private Character data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        setStatusBarColor();
        render();
        addBackClickEventListener();
        addSaveClickEventListener();
        addAvatarSelectionListener();
    }

    protected void render() {
        data = (Character) getIntent().getSerializableExtra("character");
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
        if (data.avatar.length() == 1) {
            // a stupid preset avatar
            avatar.setImageResource(getResources().getIdentifier("avatar_" + data.avatar, "mipmap", getPackageName()));
        } else {
            new Tools.LoadImagesTask(avatar).execute(data.avatar);
        }

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

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void addAvatarSelectionListener() {
        findViewById(R.id.detail_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyActivity.this, AvatarSelectActivity.class);
                startActivityForResult(intent, AvatarSelectActivity.SELECT_AVARTAR);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null) return;
        Log.i("PeachGarden:", String.format("onActivityResult: avatar %d", intent.getIntExtra("avatar", -1)));
        data.avatar = String.valueOf(intent.getIntExtra("avatar", 1));
        render();
    }

    protected void addSaveClickEventListener() {
        ImageButton save = (ImageButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: modify db and return mainpage
                if (name.getText().toString().trim().equals("") || origin.getText().toString().trim().equals("")
                    || gender.getText().toString().trim().equals("") || belong.getText().toString().trim().equals("")
                    || live_from.getText().toString().trim().equals("") || live_to.getText().toString().trim().equals("")
                    || description.getText().toString().trim().equals("") || _abstract.getText().toString().trim().equals("")) {
                    Toast.makeText(ModifyActivity.this, "所有输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Character ch = new Character();
                    ch._id = data._id;
                    ch.name = name.getText().toString();
                    ch.pinyin = null;
                    ch.avatar = data.avatar;
                    ch.abstractDescription = _abstract.getText().toString();
                    ch.description = description.getText().toString();
                    ch.gender = gender.getText().toString() == "男" ? 1 : 0;
                    if (isNumeric(live_from.getText().toString())) {
                        ch.from = Integer.parseInt(live_from.getText().toString());
                    } else {
                        ch.from = 0;
                    }
                    if (isNumeric(live_to.getText().toString())) {
                        ch.to = Integer.parseInt(live_to.getText().toString());
                    } else {
                        ch.to = 0;
                    }
                    ch.origin = origin.getText().toString();
                    ch.belongId = -1;
                    ch.belong = belong.getText().toString();
                    List<Character> res = new ArrayList<Character>();
                    res.add(ch);
                    Intent broadcast = new Intent(MainActivity.NOTIFY_ITEMS_MODIFY);
                    broadcast.putExtra("characters", (ArrayList<Character>) res);
                    sendBroadcast(broadcast);
                    Intent mIntent = new Intent();
                    mIntent.putExtra("character", ch);
                    setResult(0, mIntent);
                    ModifyActivity.this.finish();
                }
            }
        });
    }
}
