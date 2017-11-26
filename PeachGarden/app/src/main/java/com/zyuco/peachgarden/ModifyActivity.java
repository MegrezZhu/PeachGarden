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
        render(data = (Character) getIntent().getSerializableExtra("character"));
    }

    protected void render(Character data) {
        if (data == null) {
            isEdit = false;
            this.data = new Character();
            this.data.avatar = "1";
            return;
        }
        isEdit = true;

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
        if (data.avatar != null) {
            if (data.avatar.length() == 1) {
                // a stupid preset avatar
                avatar.setImageResource(getResources().getIdentifier("avatar_" + data.avatar, "mipmap", getPackageName()));
            } else {
                new Tools.LoadImagesTask(avatar).execute(data.avatar);
            }
        }

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

        addAvatarSelectionListener();
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    private void addNewCharactor() {
        data._id = -1;
        data.name = name.getText().toString();
        data.pinyin = null;
        data.avatar = null;
        data.abstractDescription = _abstract.getText().toString();
        data.description = description.getText().toString();
        data.gender = male.isSelected() ? 1 : 0;
        data.from = Integer.parseInt(live_from.getText().toString());
        data.to = Integer.parseInt(live_to.getText().toString());
        data.origin = origin.getText().toString();
        data.belongId = -1;
        data.belong = belong.getText().toString();
        List<Character> res = new ArrayList<>();
        res.add(data);
        Intent broadcast = new Intent(MainActivity.NOTIFY_ITEMS_ADDITIONS);
        broadcast.putExtra("characters", (ArrayList<Character>) res);
        sendBroadcast(broadcast);
    }

    private void addAvatarSelectionListener() {
        findViewById(R.id.edit_avatar).setOnClickListener(new View.OnClickListener() {
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
        switch (requestCode) {
            case AvatarSelectActivity.SELECT_AVARTAR:
            default:
        }
        if (intent == null) return;
        Log.i("PeachGarden:", String.format("onActivityResult: avatar %d", intent.getIntExtra("avatar", -1)));
        data.avatar = String.valueOf(intent.getIntExtra("avatar", 1));
        render(data);
    }

    private void editCharactor() {
        data.name = name.getText().toString();
        data.pinyin = null;
        data.abstractDescription = _abstract.getText().toString();
        data.description = description.getText().toString();
        data.gender = male.isSelected() ? 1 : 0;
        if (isNumeric(live_from.getText().toString())) {
            data.from = Integer.parseInt(live_from.getText().toString());
        } else {
            data.from = 0;
        }
        if (isNumeric(live_to.getText().toString())) {
            data.to = Integer.parseInt(live_to.getText().toString());
        } else {
            data.to = 0;
        }
        data.origin = origin.getText().toString();
        data.belongId = -1;
        data.belong = belong.getText().toString();
        List<Character> res = new ArrayList<Character>();
        res.add(data);
        Intent broadcast = new Intent(MainActivity.NOTIFY_ITEMS_MODIFY);
        broadcast.putExtra("characters", (ArrayList<Character>) res);
        sendBroadcast(broadcast);
        Intent mIntent = new Intent();
        mIntent.putExtra("character", data);
        setResult(0, mIntent);
    }
}
