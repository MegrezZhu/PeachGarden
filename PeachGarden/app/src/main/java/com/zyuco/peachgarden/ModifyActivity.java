package com.zyuco.peachgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.zyuco.peachgarden.library.Tools;
import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText name;
    private EditText belong;
    private EditText origin;
    private EditText description;
    private EditText _abstract;
    private EditText live_from;
    private EditText live_to;
    private RadioButton male;
    private RadioButton female;
    private Character data;

    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        setStatusBarColor();
        setEventListener();
        render();
    }

    protected void render() {
        Object obj = getIntent().getSerializableExtra("character");
        ;
        if (obj == null) {
            isEdit = false;
            return;
        }
        isEdit = true;
        data = (Character) obj;

        // 名字
        name.setText(data.name);
        // 归属势力
        belong.setText(data.belong != null ? data.belong : "???");
        // 籍贯
        origin.setText(data.origin != null ? data.origin : "???");
        // 生卒
        live_from.setText(data.from == 0 ? "?" : String.valueOf(data.from));
        live_to.setText(data.to == 0 ? "?" : String.valueOf(data.to));
        //人物简介
        _abstract.setText(data.abstractDescription);
        // 历史记载
        description.setText(data.description);
        // 头像
        new Tools.LoadImagesTask(avatar).execute(data.avatar);
        //性别
        (data.gender == 1 ? male : female).setSelected(true);
        // 背景
        ScrollView scrollView = findViewById(R.id.edit_scroll_view);
        scrollView.setBackgroundResource(data.gender == 1 ? R.mipmap.detail_man_bg : R.mipmap.detail_woman_bg);
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xF5F5F5);
    }

    protected void setEventListener() {
        avatar = findViewById(R.id.edit_avatar);
        name = findViewById(R.id.edit_name);
        belong = findViewById(R.id.edit_belong);
        origin = findViewById(R.id.edit_origin_input);
        live_from = findViewById(R.id.edit_live_from);
        live_to = findViewById(R.id.edit_live_to);
        _abstract = findViewById(R.id.edit_abstract);
        description = findViewById(R.id.edit_history);
        male = findViewById(R.id.edit_gender_male);
        female = findViewById(R.id.edit_gender_female);
        ImageButton save = (ImageButton) findViewById(R.id.edit_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: modify db and return mainpage
                if (name.getText().toString().trim().equals("")
                        || origin.getText().toString().trim().equals("")
                        || belong.getText().toString().trim().equals("")
                        || live_from.getText().toString().trim().equals("")
                        || live_to.getText().toString().trim().equals("")
                        || description.getText().toString().trim().equals("")
                        || _abstract.getText().toString().trim().equals("")) {
                    Toast.makeText(ModifyActivity.this, "所有输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEdit) {
                    editCharactor();
                } else {
                    addNewCharactor();
                }
                ModifyActivity.this.finish();
            }
        });
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    private void addNewCharactor() {
        Character ch = new Character();
        ch._id = -1;
        ch.name = name.getText().toString();
        ch.pinyin = null;
        ch.avatar = null;
        ch.abstractDescription = _abstract.getText().toString();
        ch.description = description.getText().toString();
        ch.gender = male.isSelected() ? 1 : 0;
        ch.from = Integer.parseInt(live_from.getText().toString());
        ch.to = Integer.parseInt(live_to.getText().toString());
        ch.origin = origin.getText().toString();
        ch.belongId = -1;
        ch.belong = belong.getText().toString();
        List<Character> res = new ArrayList<>();
        res.add(ch);
        Intent broadcast = new Intent(MainActivity.NOTIFY_ITEMS_ADDITIONS);
        broadcast.putExtra("characters", (ArrayList<Character>) res);
        sendBroadcast(broadcast);
    }

    private void editCharactor() {
        Character ch = new Character();
        ch._id = data._id;
        ch.name = name.getText().toString();
        ch.pinyin = null;
        ch.avatar = data.avatar;
        ch.abstractDescription = _abstract.getText().toString();
        ch.description = description.getText().toString();
        ch.gender = male.isSelected() ? 1 : 0;
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
    }
}
