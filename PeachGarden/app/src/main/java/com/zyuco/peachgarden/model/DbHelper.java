package com.zyuco.peachgarden.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.zyuco.peachgarden.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "PeachGarden.DbHelper";

    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "PeachGarden.db";

    // table name
    private static final String TABLE_CHARACTER = "characters";

    // column names
    private static final String CHARACTER_NAME = "name";
    private static final String CHARACTER_PINYIN = "pinyin";
    private static final String CHARACTER_AVATAR = "avatar";
    private static final String CHARACTER_ABSTRACT = "abstract";
    private static final String CHARACTER_DESCRIPTION = "description";
    private static final String CHARACTER_GENDER = "gender";
    private static final String CHARACTER_FROM = "live_from";
    private static final String CHARACTER_TO = "live_to";
    private static final String CHARACTER_ORIGIN = "origin";
    private static final String CHARACTER_BELONG = "belong";
    private static final String CHARACTER_BELONG_ID = "belong_id";

    // create tables
    private static final String CREATE_TABLE_CHARACTERS = "" +
        "CREATE TABLE IF NOT EXISTS " + TABLE_CHARACTER +
        " ( " + CHARACTER_NAME + " TEXT, " + CHARACTER_PINYIN + " TEXT, " + CHARACTER_AVATAR + " TEXT, " + CHARACTER_ABSTRACT + " TEXT, " + CHARACTER_DESCRIPTION + " TEXT, " + CHARACTER_GENDER + " INT, " + CHARACTER_FROM + " INT, " + CHARACTER_TO + " INT, " + CHARACTER_ORIGIN + " TEXT, " + CHARACTER_BELONG + " TEXT, " + CHARACTER_BELONG_ID + " INT );";

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

    public void insertInitialData (SQLiteDatabase db) {
        Character[] data = readInitCharacters();
        db.beginTransaction();
        for (Character ch : data) {
            ContentValues value = convertCharacter(ch);
            db.insert(TABLE_CHARACTER, null, value);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private Character[] readInitCharacters () {
        try (InputStream stream = context.getResources().openRawResource(R.raw.characters)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF8"));
            Gson gson = new Gson();
            return gson.fromJson(reader, Character[].class);
        } catch (IOException e) {
            Log.e(TAG, "readInitCharacters: ", e);
            return new Character[]{};
        }
    }

    ContentValues convertCharacter(Character ch) {
        ContentValues values = new ContentValues();
        values.put(CHARACTER_NAME, ch.name);
        values.put(CHARACTER_PINYIN, ch.pinyin);
        values.put(CHARACTER_AVATAR, ch.avatar);
        values.put(CHARACTER_ABSTRACT, ch.abstractDescription);
        values.put(CHARACTER_DESCRIPTION, ch.description);
        values.put(CHARACTER_GENDER, ch.gender);
        values.put(CHARACTER_FROM, ch.from);
        values.put(CHARACTER_TO, ch.to);
        values.put(CHARACTER_ORIGIN, ch.origin);
        values.put(CHARACTER_BELONG, ch.belong);
        values.put(CHARACTER_BELONG_ID, ch.belongId);

        return values;
    }

}
