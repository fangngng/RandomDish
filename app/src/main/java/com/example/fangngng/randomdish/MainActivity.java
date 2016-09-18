package com.example.fangngng.randomdish;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private List<Map<String, Object>> mData;
    MyAdapter adapter;

    private EditText  input2,info2;
    private Button addDish, random;
    private DishItem dishItem ;
    private Spinner dishSpinner;

    private TextView detailTitle, detailInfo;
    private ImageView detailImg;

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

        dishSpinner = (Spinner) findViewById(R.id.spinner);

        dishItem = new DishItem(MainActivity.this);

        Log.v("init","init");
        lv = (ListView) findViewById(R.id.list1);
        mData = dishItem.get();
        adapter = new MyAdapter(this,dishItem);
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
                            dishItem.add(MainActivity.this, input2.getText().toString(), info2.getText().toString(),"home");
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
                showInfo(randomDish());
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

    private String randomDish() {
//        Random random = new Random(System.currentTimeMillis());
//        int a = random.nextInt(mData.size());

        if (mData.size() > 0) {
            int b = Integer.valueOf((int) (Math.random() * mData.size()));

            return dishItem.get().get(b).get("title").toString();
        }
        return "添加个饭店吧。";

    }


}
