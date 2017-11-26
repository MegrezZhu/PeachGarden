package com.zyuco.peachgarden;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zyuco.peachgarden.library.CommonAdapter;
import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private String type;
    private List<Character> list = new ArrayList<>();
    private CommonAdapter<Character> adapter;
    private EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        input = findViewById(R.id.search_input);

        setStatusBarColor();
        initView();
        initList();
        initListener();
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xFFEDEDED);
    }

    private void initView() {
        TextView title = findViewById(R.id.search_title);
        TextView nores = findViewById(R.id.search_nores);

        nores.setText(type.equals("own") ? R.string.search_own_nores : R.string.search_all_nores);
        title.setText(type.equals("own") ? R.string.search_own : R.string.search_all);
    }

    private void initListener() {
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = input.getText().toString();
                if (key.equals("")) {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Character> new_list;
                if (type.equals("all")) {
                    new_list = DbReader.getInstance(SearchActivity.this).getSearchCharacters(key);
                } else {
                    new_list = DbReader.getInstance(SearchActivity.this).getSearchOwnCharacters(key);
                }
                list.clear();
                if (new_list != null) {
                    findViewById(R.id.search_nores).setVisibility(View.INVISIBLE);
                    list.addAll(new_list);
                } else {
                    findViewById(R.id.search_nores).setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initList() {
        if (type.equals("own")) {
            initOwnList();
        } else {
            initWikiList();
        }
    }


    private void initOwnList() {
        adapter = new CommonAdapter<Character>(this, R.layout.character_item, list) {
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
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, DetailActivity.class);
                intent.putExtra("character", data);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position, Character data) {
                // TODO: do... whatever
            }
        });


        RecyclerView list = findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        list.setAdapter(adapter);
    }


    private void initWikiList() {
        adapter = new CommonAdapter<Character>(this, R.layout.character_item, list) {
            @Override
            public void convert(ViewHolder holder, Character data) {
                boolean unlocked = DbReader.getInstance(SearchActivity.this).checkIfOwned(data);
                TextView name = holder.getView(R.id.name);
                name.setText(data.name);
                TextView belong = holder.getView(R.id.belong);
                belong.setText(data.belong);
                TextView description = holder.getView(R.id.abstract_description);
                description.setText("\t\t\t\t" + data.abstractDescription);
                ConstraintLayout layout = holder.getView(R.id.list_item_wrapper);

                holder.getView(R.id.locked_mask).setVisibility(unlocked ? View.INVISIBLE : View.VISIBLE);
                holder.getView(R.id.locked_mask_text).setVisibility(unlocked ? View.INVISIBLE : View.VISIBLE);
                layout.setClickable(unlocked);
            }
        };

        adapter.setOnItemClickListemer(new CommonAdapter.OnItemClickListener<Character>() {
            @Override
            public void onClick(int position, Character data) {
                boolean hasUnlock = DbReader.getInstance(SearchActivity.this).checkIfOwned(data);
                if (hasUnlock) {
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, DetailActivity.class);
                    intent.putExtra("character", data);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(int position, Character data) {
                // TODO: do... whatever
            }
        });


        RecyclerView list = findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        list.setAdapter(adapter);
    }
}
