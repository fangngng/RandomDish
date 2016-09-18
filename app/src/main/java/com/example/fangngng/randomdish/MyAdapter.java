package com.example.fangngng.randomdish;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fangngng.randomdish.Model.DishItem;
import com.example.fangngng.randomdish.R;

import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private final Activity activity;
    private List<Map<String, Object>> mData;
    private DishItem dishItem ;



    public MyAdapter(Activity activity, DishItem dishItem) {
        this.inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.dishItem = dishItem;
        this.mData = dishItem.get();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.listitem, null);
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.title = (TextView) view.findViewById(R.id.title11);
            holder.txt = (TextView) view.findViewById(R.id.info11);
            holder.btn = (Button) view.findViewById(R.id.view_btn11);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.img.setBackgroundResource((Integer) mData.get(i).get("img"));
        holder.title.setText((String) mData.get(i).get("title"));
        holder.txt.setText((String) mData.get(i).get("info"));

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishItem.remove(activity, i);
                NotifyChanged();
            }
        });

        return view;
    }

    private void NotifyChanged() {
        this.notifyDataSetChanged();
    }


    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView txt;
        public Button btn;
    }


}
