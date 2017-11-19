package com.zyuco.peachgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyuco.peachgarden.library.CommonAdapter;
import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PeachGarden.Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();
        initListeners();
        setStatusBarColor();
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xFFEDEDED);
    }

    private void initListeners() {
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "add button clicked");
                // TODO: goto add page
            }
        });

        final DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        findViewById(R.id.main_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: main page");
                startActivity(MainActivity.class);
            }
        });
        findViewById(R.id.wiki_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: goto wiki page
            }
        });
        findViewById(R.id.unlock_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO goto unlock page
            }
        });
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void initList() {
        // TODO: load characters user have, instead of all
        List<Character> res = DbReader.getInstance(this).getAllOwnedCharacters();

        final CommonAdapter<Character> adapter = new CommonAdapter<Character>(this, R.layout.character_item, res) {
            @Override
            public void convert(ViewHolder holder, Character data) {
                TextView name = holder.getView(R.id.name);
                name.setText(data.name);
                TextView belong = holder.getView(R.id.belong);
                belong.setText(data.belong);
                TextView description = holder.getView(R.id.abstract_description);
                description.setText("\t\t\t\t" + data.abstractDescription);
            }
        };

        adapter.setOnItemClickListemer(new CommonAdapter.OnItemClickListener<Character>() {
            @Override
            public void onClick(int position, Character data) {
                Log.i(TAG, String.format("Item clicked: %s", data.name));
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("character", data);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position, Character data) {
                Log.i(TAG, String.format("Item long-clicked: %s", data.name));
                // TODO: do... whatever
            }
        });


        RecyclerView list = (RecyclerView) findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }
}
