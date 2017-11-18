package com.zyuco.peachgarden;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyuco.peachgarden.model.Character;
import com.zyuco.peachgarden.model.DbHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PeachGarden.Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper helper = new DbHelper(this);
        List<Character> res = helper.getAllCharacters();

        Log.i(TAG, String.format("get %d items", res.size()));

        final CommonAdapter<Character> adapter = new CommonAdapter<Character>(this, R.layout.character_item, res) {
            @Override
            public void convert(ViewHolder holder, Character data) {
                TextView name = holder.getView(R.id.name);
                name.setText(data.name);
            }
        };

        RecyclerView list = (RecyclerView)findViewById(R.id.character_list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }
}

abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.get(mContext, parent, mLayoutId);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(ViewHolder holder, T data);

    public void setOnItemClickListemer(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
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
