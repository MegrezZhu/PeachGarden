package com.zyuco.peachgarden;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;
import com.zyuco.peachgarden.model.DbHelper;
import com.zyuco.peachgarden.library.CommonAdapter;

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
    }

    private void initList() {
        DbHelper helper = DbHelper.getInstance(this);
        // TODO: load characters user have, instead of all
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Character> res = helper.getAllOwnedCharacters(db);

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
                // TODO: goto detail page
            }

            @Override
            public void onLongClick(int position, Character data) {
                Log.i(TAG, String.format("Item long-clicked: %s", data.name));
                // TODO: do... whatever
            }
        });

        RecyclerView list = findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }
}
