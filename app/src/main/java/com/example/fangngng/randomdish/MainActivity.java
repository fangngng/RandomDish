package com.example.fangngng.randomdish;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.Toast;

import com.example.fangngng.randomdish.Model.DishItem;

import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

//    private ListView lv;
    private EditText  input2,info2;
    private Button addDish, random;
    private DishItem dishItem ;
    private Spinner dishSpinner;
    private TextView detailTitle, detailInfo;
    private ImageView detailImg;

    private RecyclerView recyclerView;
    private MyRecycleAdapter recrycleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;
    
    private int imgNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("onCreate","onCreate");

        init();
    }

    @Override
    protected void onStart() {
        bindData();
        super.onStart();
    }

    // 从数据库中获取数据，显示到主界面上
    public void bindData() {
        //列表数据
        final List<Map<String, Object>> mData ;
        dishItem = new DishItem(MainActivity.this);
        mData = dishItem.get();

        //设置主界面的刷新控件
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            bindData();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        //设置recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this,
                LinearLayoutManager.VERTICAL, 20, Color.BLACK)); //设置分割线
        recrycleAdapter = new MyRecycleAdapter(mData);
        recrycleAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recrycleAdapter);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {//处理拖拽的事件
//                return false;
//            }
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {//处理侧滑的事件
//                dishItem.remove(MainActivity.this, viewHolder.getAdapterPosition());
//                recrycleAdapter.notifyDataSetChanged();
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        recrycleAdapter.setmOnItemClickListener(new MyRecycleAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(MainActivity.this, "点击："+i,Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.listitemdetail,(ViewGroup)findViewById(R.id.dialog1));
                detailTitle = (TextView) dialog.findViewById(R.id.detailTitle);
                detailInfo = (TextView) dialog.findViewById(R.id.detailInfo);
                detailImg = (ImageView) dialog.findViewById(R.id.detailImg);
//                detailImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                detailTitle.setText( dishItem.get().get(i).get("title").toString());
                detailInfo.setText( dishItem.get().get(i).get("info").toString());
//                detailImg.setBackgroundResource((Integer) dishItem.get().get(i).get("img"));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setNegativeButton(R.string.cancle,null);
                builder.setView(dialog);
                builder.show();
            }

            @Override
            public void onItemLongClick(View view, int i) {
                Toast.makeText(MainActivity.this, "长按："+i, Toast.LENGTH_SHORT).show();
            }
        });

        // 随机按钮
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(randomDish( mData));
            }
        });
    }

    //初始化主要界面，不包括数据
    public void init() {
        addDish = (Button) findViewById(R.id.addDish);
        random = (Button) findViewById(R.id.random);
        random.setFocusable(true);

        Log.v("init","init");

        // 添加按钮的点击事件
        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDish();
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

    //随机一个数据，并返回结果
    private String randomDish(List<Map<String, Object>> mData) {
        if (mData.size() > 0) {
            int b = (int) (Math.random() * mData.size());
            return dishItem.get().get(b).get("title").toString();
        }
        return "添加个饭馆吧。";
    }

    //添加一个新的数据
    private void AddNewDish () {
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialogadd,(ViewGroup)findViewById(R.id.dialog1));
        input2 = (EditText)dialog.findViewById(R.id.input2);
        info2 = (EditText)dialog.findViewById(R.id.info2);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String imgName = "img" + imgNum%4;
                        int imgID = getResources().getIdentifier(imgName,"drawable",
                                "com.example.fangngng.randomdish");
                        dishItem.add(MainActivity.this, input2.getText().toString(),
                                info2.getText().toString(),"home",imgID);
                        imgNum ++;
                        Log.i("imgNum:", String.valueOf(imgNum));
                        recrycleAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton(R.string.cancle,null);
        builder.setView(dialog);
        builder.show();
    }
    
    //刷新组件的handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1 :
                    swipeRefreshLayout.setRefreshing(false);
                    recrycleAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
