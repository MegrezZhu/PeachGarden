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
import android.widget.TextView;
import android.widget.Toast;

import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.Tools;
import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiafeng on 2017/11/20.
 */

public class AddActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText name;
    private EditText belong;
    private EditText origin;
    private EditText description;
    private EditText _abstract;
    private EditText live_from;
    private EditText live_to;
    private EditText gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        setStatusBarColor();
        render();
        addBackClickEventListener();
        addSaveClickEventListener();
    }
    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xF5F5F5);
    }
    protected void render(){
        avatar = findViewById(R.id.detail_avatar);
        name = (EditText)findViewById(R.id.detail_name);
        belong = findViewById(R.id.detail_belong_input);
        origin = findViewById(R.id.detail_origin_input);
        live_from = findViewById(R.id.from);
        live_to = findViewById(R.id.to);
        _abstract = findViewById(R.id.detail_abstract);
        description = findViewById(R.id.detail_desription);
        gender = findViewById(R.id.gender_input);
    }
    protected void addBackClickEventListener() {
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });
    }
    protected void addSaveClickEventListener(){
        ImageButton save = (ImageButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: modify db and return mainpage
                if (name.getText().toString().trim().equals("") || origin.getText().toString().trim().equals("")
                        ||gender.getText().toString().trim().equals("")||belong.getText().toString().trim().equals("")
                        ||live_from.getText().toString().trim().equals("")||live_to.getText().toString().trim().equals("")
                        ||description.getText().toString().trim().equals("")||_abstract.getText().toString().trim().equals("")){
                    Toast.makeText(AddActivity.this, "所有输入不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    Character ch = new Character();
                    ch._id = -1;
                    ch.name = name.getText().toString();
                    ch.pinyin = null;
                    ch.avatar = null;
                    ch.abstractDescription = _abstract.getText().toString();
                    ch.description = description.getText().toString();
                    ch.gender = gender.getText().toString() == "男" ? 1 : 0;
                    ch.from = Integer.parseInt(live_from.getText().toString());
                    ch.to = Integer.parseInt(live_to.getText().toString());
                    ch.origin = origin.getText().toString();
                    ch.belongId = -1;
                    ch.belong = belong.getText().toString();
                    List<Character> res = new ArrayList<Character>();
                    res.add(ch);
                    Intent broadcast = new Intent(MainActivity.NOTIFY_ITEMS_ADDITIONS);
                    broadcast.putExtra("characters", (ArrayList<Character>) res);
                    sendBroadcast(broadcast);
                    AddActivity.this.finish();
                }
            }
        });
    }
}
