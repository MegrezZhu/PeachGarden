package com.zyuco.peachgarden;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setPopMenu();
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

                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // TODO 删除数据，回到主页面，toast已删除
                                        }
                                    }).create().show();

                        } else if (menuItem.getItemId() == R.id.edit) {
                            // TODO 去修改页
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }
}
