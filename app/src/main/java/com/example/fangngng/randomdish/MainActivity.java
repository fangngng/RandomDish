package com.example.fangngng.randomdish;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.fangngng.randomdish.Model.DishItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ListView lv;

    private EditText  input2,info2;
    private Button addDish, random;
    private DishItem dishItem ;
    private Spinner dishSpinner;

    private TextView detailTitle, detailInfo;
    private ImageView detailImg;


    private int imgNum = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("onCreate","onCreate");
        init();

    }


    public void init() {
        addDish = (Button) findViewById(R.id.addDish);
        random = (Button) findViewById(R.id.random);
        random.setFocusable(true);

        final List<Map<String, Object>> mData;

//        dishSpinner = (Spinner) findViewById(R.id.spinner);

        dishItem = new DishItem(MainActivity.this);

        Log.v("init","init");
        lv = (ListView) findViewById(R.id.list1);
        mData = dishItem.get();
        final EfficientAdapter adapter;
        adapter = new EfficientAdapter(this, mData);
        lv.setAdapter(adapter);

        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            LayoutInflater inflater = getLayoutInflater();
            final View dialog = inflater.inflate(R.layout.dialogadd,(ViewGroup)findViewById(R.id.dialog1));
            input2 = (EditText)dialog.findViewById(R.id.input2);
            info2 = (EditText)dialog.findViewById(R.id.info2);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                    .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String imgName = "img" + imgNum%4;
                            int imgID = getResources().getIdentifier(imgName,"drawable","com.example.fangngng.randomdish");
                            dishItem.add(MainActivity.this, input2.getText().toString(), info2.getText().toString(),"home",imgID);
                            imgNum ++;
                            Log.v("imgNum:", String.valueOf(imgNum));
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancle,null);
            builder.setView(dialog);
            builder.show();
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(randomDish( mData));
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dishItem.remove(MainActivity.this, i);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.listitemdetail,(ViewGroup)findViewById(R.id.dialog1));
                detailTitle = (TextView) dialog.findViewById(R.id.detailTitle);
                detailInfo = (TextView) dialog.findViewById(R.id.detailInfo);
                detailImg = (ImageView) dialog.findViewById(R.id.detailImg);
                detailTitle.setText( dishItem.get().get(i).get("title").toString());
                detailInfo.setText( dishItem.get().get(i).get("info").toString());
                detailImg.setBackgroundResource((Integer) dishItem.get().get(i).get("img"));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setNegativeButton(R.string.cancle,null);
                builder.setView(dialog);
                builder.show();
            }
        });
    }

    public void showInfo(String info) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.randomResult)
                .setMessage(info)
                .setPositiveButton(R.string.ensure, null)
                .show();
    }

    private String randomDish(List<Map<String, Object>> mData) {
//        Random random = new Random(System.currentTimeMillis());
//        int a = random.nextInt(mData.size());

        if (mData.size() > 0) {
            int b = Integer.valueOf((int) (Math.random() * mData.size()));

            return dishItem.get().get(b).get("title").toString();
        }
        return "添加个饭店吧。";

    }



    private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Bitmap mIcon1;
        private Bitmap mIcon2;
        private List<Map<String, Object>> mData;

        public EfficientAdapter(Context context, List<Map<String, Object>> mData) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);
            this.mData = mData;

            // Icons bound to the rows.
            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img1);
            mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img2);
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
            holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

            return convertView;
        }

        static class ViewHolder {
            TextView text;
            ImageView icon;
            TextView info;
        }
    }

}
