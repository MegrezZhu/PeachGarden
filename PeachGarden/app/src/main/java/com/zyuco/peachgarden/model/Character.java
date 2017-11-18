package com.zyuco.peachgarden.model;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Character implements Serializable, BaseColumns {
    public String avatar;
    public String name;
    public String pinyin;
    @SerializedName("abstract")
    public String abstractDescription;
    public String description;
    public int baiduCount;
    public int gender; // 1 for male
    public int from;
    public int to;
    public String origin;
    public int belongId;
    public String belong;

    public static Character fromCursor(Cursor cursor) {
        // TODO
        return null;
    }
}
