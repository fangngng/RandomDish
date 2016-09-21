package com.example.fangngng.randomdish;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fangngng.randomdish.R;

import java.util.List;
import java.util.Map;

public class EfficientAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Bitmap mIcon1, mIcon2, mIcon3, mIcon4, mIcon5, tempIcon;
    private List<Map<String, Object>> mData;

    public EfficientAdapter(Context context, List<Map<String, Object>> mData) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        this.mData = mData;

        // Icons bound to the rows.
        // mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img1);
        // mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img2);
        // mIcon3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img3);
        // mIcon4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img0);
        // mIcon5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img4);
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return mData.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public Object getItem(int position) {
        return position;
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.title_item);
            holder.info = (TextView) convertView.findViewById(R.id.info_item);
            holder.icon = (ImageView) convertView.findViewById(R.id.img_item);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(mData.get(position).get("title").toString());
        holder.info.setText(mData.get(position).get("info").toString());
        holder.icon.setImageResource((int)mData.get(position).get("img"));

        return convertView;
    }


    static class ViewHolder {
        TextView text;
        ImageView icon;
        TextView info;
        Button btn;
    }
}
