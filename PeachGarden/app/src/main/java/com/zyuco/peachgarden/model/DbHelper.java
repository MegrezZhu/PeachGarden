package com.zyuco.peachgarden.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.zyuco.peachgarden.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "PeachGarden.DbHelper";

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "PeachGarden.db";

    // table name
    private static final String TABLE_CHARACTER = "characters";

    // create tables
    private static final String CREATE_TABLE_CHARACTERS = "" +
        "CREATE TABLE IF NOT EXISTS " + TABLE_CHARACTER +
        " ( " + Character.COL_NAME + " TEXT, " + Character.COL_PINYIN + " TEXT, " + Character.COL_AVATAR + " TEXT, " + Character.COL_ABSTRACT + " TEXT, " + Character.COL_DESCRIPTION + " TEXT, " + Character.COL_GENDER + " INT, " + Character.COL_FROM + " INT, " + Character.COL_TO + " INT, " + Character.COL_ORIGIN + " TEXT, " + Character.COL_BELONG + " TEXT, " + Character.COL_BELONG_ID + " INT );";

    private Context context;


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
        db.execSQL(CREATE_TABLE_CHARACTERS);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);
        onCreate(db);
    }

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public List<Character> getAllCharacters() {
        String select = "SELECT * FROM " + TABLE_CHARACTER;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        List<Character> result = new ArrayList<>(cursor.getCount());

        cursor.moveToFirst();
        while (true) {
            result.add(Character.fromCursor(cursor));
            if (!cursor.moveToNext()) break;
        }
        cursor.close();

        return result;
    }

    private void insertInitialData(SQLiteDatabase db) {
        Character[] data = readInitCharacters();
        db.beginTransaction();
        for (Character ch : data) {
            db.insert(TABLE_CHARACTER, null, ch.toContentValues());
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
}
