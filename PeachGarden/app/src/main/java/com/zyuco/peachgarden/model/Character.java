package com.zyuco.peachgarden.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Character implements Serializable, BaseColumns {
    public static final String COL_NAME = "name";
    public static final String COL_PINYIN = "pinyin";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_ABSTRACT = "abstract";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_GENDER = "gender";
    public static final String COL_FROM = "live_from";
    public static final String COL_TO = "live_to";
    public static final String COL_ORIGIN = "origin";
    public static final String COL_BELONG = "belong";
    public static final String COL_BELONG_ID = "belong_id";

    public long _id = -1;
    public String name;
    public String pinyin;
    public String avatar;
    @SerializedName("abstract")
    public String abstractDescription;
    public String description;
//    public int baiduCount;
    public int gender; // 1 for male
    public int from;
    public int to;
    public String origin;
    public int belongId;
    public String belong;

    public static Character fromCursor(Cursor cursor) {
        Character ch = new Character();
        ch._id = cursor.getLong(cursor.getColumnIndex(_ID));
        ch.name = cursor.getString(cursor.getColumnIndex(COL_NAME));
        ch.pinyin = cursor.getString(cursor.getColumnIndex(COL_PINYIN));
        ch.avatar = cursor.getString(cursor.getColumnIndex(COL_AVATAR));
        ch.abstractDescription = cursor.getString(cursor.getColumnIndex(COL_ABSTRACT));
        ch.description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
        ch.gender = cursor.getInt(cursor.getColumnIndex(COL_GENDER));
        ch.from = cursor.getInt(cursor.getColumnIndex(COL_FROM));
        ch.to = cursor.getInt(cursor.getColumnIndex(COL_TO));
        ch.origin = cursor.getString(cursor.getColumnIndex(COL_ORIGIN));
        ch.belongId = cursor.getInt(cursor.getColumnIndex(COL_BELONG_ID));
        ch.belong = cursor.getString(cursor.getColumnIndex(COL_BELONG));

        return ch;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PINYIN, pinyin);
        values.put(COL_AVATAR, avatar);
        values.put(COL_ABSTRACT, abstractDescription);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_GENDER, gender);
        values.put(COL_FROM, from);
        values.put(COL_TO, to);
        values.put(COL_ORIGIN, origin);
        values.put(COL_BELONG, belong);
        values.put(COL_BELONG_ID, belongId);

        return values;
    }
}
