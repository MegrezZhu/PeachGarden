package com.zyuco.peachgarden.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.zyuco.peachgarden.R;
import com.zyuco.peachgarden.model.Character;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class DbHelper extends SQLiteOpenHelper {
    private static DbHelper instance;

    private static final String TAG = "PeachGarden.DbHelper";

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "PeachGarden.db";

    // table name
    static final String TABLE_CHARACTER = "characters";
    static final String TABLE_OWN = "own";

    // create tables
    private static final String CREATE_TABLE_CHARACTERS = "" +
        "CREATE TABLE IF NOT EXISTS " + TABLE_CHARACTER +
        " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + Character.COL_NAME + " TEXT NOT NULL, " + Character.COL_PINYIN + " TEXT, " + Character.COL_AVATAR + " TEXT, " + Character.COL_ABSTRACT + " TEXT, " + Character.COL_DESCRIPTION + " TEXT, " + Character.COL_GENDER + " INT, " + Character.COL_FROM + " INT, " + Character.COL_TO + " INT, " + Character.COL_ORIGIN + " TEXT, " + Character.COL_BELONG + " TEXT, " + Character.COL_BELONG_ID + " INT );";

    private static final String CREATE_TABLE_OWN = "" +
        "CREATE TABLE IF NOT EXISTS " + TABLE_OWN +
        " ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "   character_id INTEGER," +
        "   FOREIGN KEY(character_id) REFERENCES " + TABLE_CHARACTER + "(" + Character._ID + ")" +
        ");";

    private static final int INIT_OWN_SIZE = 10;

    private Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "db created");
        db.execSQL(CREATE_TABLE_CHARACTERS);
        db.execSQL(CREATE_TABLE_OWN);

        Character[] data = readInitCharacters();
        insertInitialData(db, data);
        generateInitOwn(db, data, INIT_OWN_SIZE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWN);
        onCreate(db);
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    private void insertInitialData(SQLiteDatabase db, Character[] data) {
        db.beginTransaction();
        for (Character ch : data) {
            ch._id = db.insert(TABLE_CHARACTER, null, ch.toContentValues());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void generateInitOwn(SQLiteDatabase db, Character[] data, int count) {
        List<Character> list = Arrays.asList(data);

        int toGen = Math.min(count, data.length);
        Collections.shuffle(list);
        List<Character> gen = list.subList(0, toGen);

        db.beginTransaction();
        for (Character ch : gen) {
            ContentValues values = new ContentValues();
            values.put("character_id", ch._id);
            db.insert(TABLE_OWN, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private Character[] readInitCharacters() {
        try (InputStream stream = context.getResources().openRawResource(R.raw.characters)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF8"));
            Gson gson = new Gson();
            return gson.fromJson(reader, Character[].class);
        } catch (IOException e) {
            Log.e(TAG, "readInitCharacters: ", e);
            return new Character[]{};
        }
    }

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }
}
