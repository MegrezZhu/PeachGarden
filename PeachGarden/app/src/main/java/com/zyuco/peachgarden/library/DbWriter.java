package com.zyuco.peachgarden.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zyuco.peachgarden.model.Character;

import java.util.List;

import static com.zyuco.peachgarden.library.DbHelper.TABLE_CHARACTER;

/**
 * DbWriter is an interface to manipulate (write-only) Database
 */
public class DbWriter {
    private static DbWriter instance;

    private SQLiteDatabase db;

    private DbWriter(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public void addCharacters2Own(List<Character> list) {
        db.beginTransaction();
        for (Character ch : list) {
            ContentValues values = new ContentValues();
            values.put("character_id", ch._id);
            db.insert(DbHelper.TABLE_OWN, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void addCharacters(List<Character> list) {
        db.beginTransaction();
        for (Character ch : list) {
            ch._id = db.insert(TABLE_CHARACTER, null, ch.toContentValues());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteOwnedCharacter(Character ch) {
        db.delete(DbHelper.TABLE_OWN, Character._ID + " = ?", new String[]{String.valueOf(ch._id)});
    }

    public static synchronized DbWriter getInstance(Context context) {
        if (instance == null) {
            instance = new DbWriter(context);
        }
        return instance;
    }
}
