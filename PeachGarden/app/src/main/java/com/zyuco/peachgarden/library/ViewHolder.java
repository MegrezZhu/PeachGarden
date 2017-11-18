package com.zyuco.peachgarden.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> cachedViews;
    private View mConvertView;

    public ViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView = itemView;
        cachedViews = new SparseArray<>();
    }

    public static ViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(context, itemView, parent);
    }

    public <T extends View> T getView(int viewId) {
        View view = cachedViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            cachedViews.put(viewId, view);
        }
        return (T) view;
    }
}
