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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.zyuco.peachgarden.library.CommonAdapter;
import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;

public class EncyclopediaActivity extends AppCompatActivity {
    private List<Character> list;
    private List<Character> copy_list = new ArrayList<>();
    private CommonAdapter<Character> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia);

        initList();
        initListener();
        setStatusBarColor();
    }

    private void initListener() {
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Pulse).duration(200).playOn(view);
                Intent intent = new Intent(EncyclopediaActivity.this, AddActivity.class);
                EncyclopediaActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.search_input);
                String key = editText.getText().toString();
                if (!key.equals("")) {
                    editText.setText("");
                    List<Character> new_list = DbReader.getInstance(EncyclopediaActivity.this).getSearchCharacters(key);
                    list.clear();
                    if (new_list != null) {
                        findViewById(R.id.search_nores).setVisibility(View.INVISIBLE);
                        list.addAll(DbReader.getInstance(EncyclopediaActivity.this).getSearchCharacters(key));
                    } else {
                        findViewById(R.id.search_nores).setVisibility(View.VISIBLE);
                    }
                } else {
                    list.clear();
                    findViewById(R.id.search_nores).setVisibility(View.INVISIBLE);
                    list.addAll(copy_list);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(0xFFEDEDED);
    }

    private void initList() {
        list = DbReader.getInstance(this).getAllCharacters();
        copy_list.addAll(list);
        adapter = new CommonAdapter<Character>(this, R.layout.character_item, list) {
            @Override
            public void convert(ViewHolder holder, Character data) {
                boolean unlocked = DbReader.getInstance(EncyclopediaActivity.this).checkIfOwned(data);
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
                boolean hasUnlock = DbReader.getInstance(EncyclopediaActivity.this).checkIfOwned(data);
                if (hasUnlock) {
                    Intent intent = new Intent();
                    intent.setClass(EncyclopediaActivity.this, DetailActivity.class);
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
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }
}
