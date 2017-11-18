package com.zyuco.peachgarden.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mData;
    private OnItemClickListener<T> mOnItemClickListener;

    public CommonAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mLayoutId = layoutId;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder, mData.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickListener.onClick(pos, mData.get(pos));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getAdapterPosition();
                    mOnItemClickListener.onLongClick(pos, mData.get(pos));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public abstract void convert(ViewHolder holder, T data);

    public void setOnItemClickListemer(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onClick(int position, T data);

        void onLongClick(int position, T data);
    }
}

