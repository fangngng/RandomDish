package com.example.fangngng.randomdish;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by fang.fei on 2016/9/28.
 */

public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

    private List<Map<String, Object>> mData;
    private OnItemClickListener mOnItemClickListener;

    public MyRecycleAdapter(List<Map<String, Object>> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.text.setText(mData.get(position).get("title").toString());
        holder.info.setText(mData.get(position).get("info").toString());
        holder.icon.setImageResource((int)mData.get(position).get("img"));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false));
        return holder;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);  //点击
        void onItemLongClick(View view, int position);  //长按
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView icon;
        TextView info;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.title_item);
            info = (TextView) itemView.findViewById(R.id.info_item);
            icon = (ImageView) itemView.findViewById(R.id.img_item);
        }
    }
}
