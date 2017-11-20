package com.zyuco.peachgarden;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zyuco.peachgarden.library.Tools;
import com.zyuco.peachgarden.model.Character;

public class DetailActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView name;
    private TextView belong;
    private TextView origin;
    private TextView live;
    private TextView description;
    private TextView _abstract;

    private Character data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setStatusBarColor();
        render();
        setPopMenu();
        addBackClickEventListener();
    }

    protected void render() {
        data = (Character) getIntent().getSerializableExtra("character");
        avatar = findViewById(R.id.detail_avatar);
        name = findViewById(R.id.detail_name);
        belong = findViewById(R.id.detail_belong);
        origin = findViewById(R.id.detail_origin);
        live = findViewById(R.id.detail_live);
        _abstract = findViewById(R.id.detail_abstract);
        description = findViewById(R.id.detail_desription);
        // 名字
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < data.name.length(); i++) {
            if (i != 0) {
                text.append(data.gender == 1 ? '♂' : '♀');
            }
            text.append(data.name.charAt(i));
        }

        name.setText(text.toString());
        text.setLength(0);

        // 归属势力
        text.append("归属势力:").append(data.belong != null ? data.belong : "???");
        belong.setText(text.toString());
        text.setLength(0);

        // 籍贯
        text.append("籍贯:").append(data.origin != null ? data.origin : "???");
        origin.setText(text.toString());
        text.setLength(0);

        // 生卒
        text.append("生卒:").append(data.from == 0 ? "?" : data.from + "年").append('-').append(data.to == 0 ? "?" : data.to + "年");
        live.setText(text.toString());
        text.setLength(0);

        // 人物简介
        _abstract.setText(text.append("\t\t\t\t").append(data.abstractDescription).toString());
        text.setLength(0);

        // 历史记载
        description.setText(text.append("\t\t\t\t").append(data.description).toString());
        text.setLength(0);

        // 头像
        new Tools.LoadImagesTask(avatar).execute(data.avatar);

        // 背景
        ScrollView scrollView = findViewById(R.id.detail_scroll_view);
        scrollView.setBackgroundResource(data.gender == 1 ? R.mipmap.detail_man_bg : R.mipmap.detail_woman_bg);
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xF5F5F5);
    }

    protected void setPopMenu() {
        final ImageButton moreMenu = (ImageButton) findViewById(R.id.more);
        moreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DetailActivity.this, moreMenu);
                popupMenu.getMenuInflater().inflate(R.menu.detail_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.remove) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                            builder.setTitle("注意！").setMessage("确定要删除么?")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Do nothing
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // TODO 删除数据，回到主页面，toast已删除
                                        Intent broadcast = new Intent(MainActivity.NOTIFY_ITEM_DELETION);
                                        broadcast.putExtra("character", data);
                                        DetailActivity.this.sendBroadcast(broadcast);
                                        DetailActivity.this.finish();
                                    }
                                }).create().show();

                        } else if (menuItem.getItemId() == R.id.edit) {
                            Intent intent = new Intent(DetailActivity.this, ModifyActivity.class);
                            intent.putExtra("character",data);
                            DetailActivity.this.startActivity(intent);
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    protected void addBackClickEventListener() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
            }
        });
    }

}
