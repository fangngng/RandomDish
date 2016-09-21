package com.example.fangngng.randomdish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by fangngng on 2016/9/21.
 */
public class RecrycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Map<String, Object>> mData;

    public RecrycleAdapter(Context context, List<Map<String, Object>> mData) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(layoutInflater.inflate(R.layout.listitem, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NormalHolder viewHolder = (NormalHolder) holder;
        viewHolder.title.setText(mData.get(position).get("title").toString());
        viewHolder.info.setText(mData.get(position).get("info").toString());
        viewHolder.img.setImageResource((int)mData.get(position).get("img"));


    }

//    private void BindNormalItem()

    public class NormalHolder extends RecyclerView.ViewHolder {
        TextView title, info;
        ImageView img;

        public NormalHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_item);
            info = (TextView) itemView.findViewById(R.id.info_item);
            img = (ImageView) itemView.findViewById(R.id.img_item);
        }
    }
}
