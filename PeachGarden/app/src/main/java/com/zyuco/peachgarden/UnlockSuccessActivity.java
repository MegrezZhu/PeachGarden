package com.zyuco.peachgarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.zyuco.peachgarden.library.CommonAdapter;
import com.zyuco.peachgarden.library.DbReader;
import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;

import java.util.List;

public class UnlockSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_success);
        initList();
    }

    private void initList() {
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
                Intent intent = new Intent();
                intent.setClass(UnlockSuccessActivity.this, DetailActivity.class);
                intent.putExtra("character", data);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position, Character data) {
                // TODO: do... whatever
            }
        });
        RecyclerView list = (RecyclerView) findViewById(R.id.rv_new_unlock);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }
}
