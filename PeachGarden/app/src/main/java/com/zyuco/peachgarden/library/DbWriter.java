package com.zyuco.peachgarden.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zyuco.peachgarden.model.Character;

/**
 * DbWriter is an interface to manipulate (write-only) Database
 */
public class DbWriter {
    private static DbWriter instance;

    private SQLiteDatabase db;

    private DbWriter(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
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
