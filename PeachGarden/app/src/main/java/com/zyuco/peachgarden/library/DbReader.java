package com.zyuco.peachgarden.library;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zyuco.peachgarden.model.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * DbWriter is an interface to manipulate (read-only) Database
 */
public class DbReader {
    private static DbReader instance;

    // select data
    private static final String SELECT_ALL_CHARACTERS = "SELECT * FROM " + DbHelper.TABLE_CHARACTER;
    private static final String SELECT_ALL_OWNED_CHARACTERS = "" +
        "SELECT * " +
        "FROM " + DbHelper.TABLE_CHARACTER + " as c, " + DbHelper.TABLE_OWN + " as o " +
        "WHERE o.character_id = c._id;";

    private SQLiteDatabase db;

    private DbReader(Context context) {
        db = DbHelper.getInstance(context).getReadableDatabase();
    }

    public List<Character> getAllCharacters() {
        Cursor cursor = db.rawQuery(SELECT_ALL_CHARACTERS, null);
        List<Character> res = getAllCharactersByCursor(cursor);
        cursor.close();
        return res;
    }

    public List<Character> getAllOwnedCharacters() {
        Cursor cursor = db.rawQuery(SELECT_ALL_OWNED_CHARACTERS, null);
        List<Character> res = getAllCharactersByCursor(cursor);
        cursor.close();
        return res;
    }

    public List<Character> getRandomCharacters(int count) {
        String select = "SELECT * " +
                "FROM " + DbHelper.TABLE_CHARACTER + " as c " +
                "WHERE c._id NOT IN ( " +
                "SELECT character_id " +
                "FROM " + DbHelper.TABLE_OWN + " " +
                ")" +
                "ORDER BY RANDOM() LIMIT " + count;
        Cursor cursor = db.rawQuery(select, null);
        List<Character> res = getAllCharactersByCursor(cursor);
        cursor.close();
        return res;
    }

    private List<Character> getAllCharactersByCursor(Cursor cursor) {
        List<Character> result = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (true) {
            result.add(Character.fromCursor(cursor));
            if (!cursor.moveToNext()) break;
        }
        return result;
    }

    public static synchronized DbReader getInstance(Context context) {
        if (instance == null) {
//            context.getApplicationContext().deleteDatabase(DbHelper.DB_NAME); // used to force triggering db creation
            instance = new DbReader(context);
        }
        return instance;
    }
}
