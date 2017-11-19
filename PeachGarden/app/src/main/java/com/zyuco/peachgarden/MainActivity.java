package com.zyuco.peachgarden;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.zyuco.peachgarden.library.DbWriter;
import com.zyuco.peachgarden.library.ViewHolder;
import com.zyuco.peachgarden.model.Character;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PeachGarden.Main";

    public static final String NOTIFY_ITEM_DELETION = "com.zyuco.peachgarden.MainActivity.notifyItemDeletion";
    public static final String NOTIFY_ITEMS_ADDITION = "com.zyuco.peachgarden.MainActivity.notifyItemsAddition";

    private List<Character> list;
    private CommonAdapter<Character> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();
        initListeners();
        setStatusBarColor();
        registBroadcastReceivers();
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
                startActivity(UnlockActivity.class);
            }
        });
    }

    private void registBroadcastReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFY_ITEM_DELETION);
        filter.addAction(NOTIFY_ITEMS_ADDITION);
        Receiver receiver = new Receiver();
        registerReceiver(receiver, filter);
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void initList() {
        list = DbReader.getInstance(this).getAllOwnedCharacters();

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


        RecyclerView list = findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    public class Receiver extends BroadcastReceiver {
        private static final String TAG = "PeachGarden.Main_Recv";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: ");
            switch (intent.getAction()) {
                case NOTIFY_ITEM_DELETION:
                    Character ch = (Character) intent.getSerializableExtra("character");
                    deleteCharacter(ch);
                    break;
                case NOTIFY_ITEMS_ADDITION:
                    List<Character> list = (ArrayList<Character>)intent.getSerializableExtra("characters");
                    addCharacters(list);
                    break;
            }
        }

        private void deleteCharacter(Character ch) {
            final Character character = ch;
            for (Character _ch : list) {
                if (_ch._id == character._id) {
                    list.remove(_ch); // stupid... but enough!
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbWriter.getInstance(MainActivity.this).deleteOwnedCharacter(character);
                }
            }).start();
        }

        private void addCharacters(List<Character> characters) {
            final List<Character> characterList = characters;
            list.addAll(characterList);
            adapter.notifyDataSetChanged();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DbWriter.getInstance(MainActivity.this).addCharacters2Own(characterList);
                }
            }).start();
        }
    }
}
