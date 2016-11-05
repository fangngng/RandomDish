package com.example.fangngng.randomdish;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import com.example.fangngng.randomdish.Model.DishItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //    private ListView lv;
    private EditText  input2, info2;

    private Button addDish, random;
    private DishItem dishItem ;
    private Spinner dishSpinner;
    private TextView detailTitle, detailInfo;
    private ImageView detailImg;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    private RecyclerView recyclerView;
    private MyRecycleAdapter recrycleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;
    private HashMap<String, Object> tempDate;
    private Spinner mainSpinner;
    private String dishType;
    
    private int imgNum = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("天降美食");

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        init();

        //列表数据
        final List<Map<String, Object>> mData ;
        mData = dishItem.get();


        //随机
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(randomDish( mData));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.i("onCreate","onCreate");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "net work is available", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "net work is not available", Toast.LENGTH_SHORT).show();
        }   }
    }


    @Override
    protected void onStart() {
        bindData();
        super.onStart();
    }

    // 从数据库中获取数据，显示到主界面上
    public void bindData() {


        final List<String> mType;
        mType = dishItem.getType();
        if(mType.isEmpty()) {
            mType.add("默认");
        }
        dishType = mType.get(0);

        //列表数据
        final List<Map<String, Object>> mData ;
        mData = dishItem.get();

        //设置recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this,
                LinearLayoutManager.VERTICAL, 20, Color.BLACK)); //设置分割线
        recrycleAdapter = new MyRecycleAdapter(mData);
        recrycleAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recrycleAdapter);

        //滑动操作
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {//处理拖拽的事件
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {//处理侧滑的事件
                int pos = viewHolder.getAdapterPosition();
                tempDate.put("title", mData.get(pos).get("title").toString());
                tempDate.put("info", mData.get(pos).get("info").toString());
//                tempDate.put("type", mData.get(pos).get("type").toString());
                tempDate.put("img", mData.get(pos).get("img"));

                //删除
                dishItem.remove(MainActivity.this, pos);
                recrycleAdapter.notifyDataSetChanged();
                //撤销
                Snackbar.make(recyclerView, "确认删除？或者撤销吧", Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Undo(tempDate.get("title").toString(), tempDate.get("info").toString(), (int)tempDate.get("img") );
                                recrycleAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //点击操作
        recrycleAdapter.setmOnItemClickListener(new MyRecycleAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int i) {
//                Toast.makeText(MainActivity.this, "点击："+i,Toast.LENGTH_SHORT).show();
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


        Spinner mainSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainSpinner.setAdapter(adapter);
        mainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dishType = mType.get(position);
                dishItem.getDate(dishType);
                recrycleAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "点击的是:" + mType.get(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                            dishItem.getDate(dishType);
                            recrycleAdapter.notifyDataSetChanged();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });


//        // 随机按钮
//        random.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showInfo(randomDish( mData));
//            }
//        });
    }

    //初始化主要界面和参数，不包括数据
    public void init() {
        dishItem = new DishItem(MainActivity.this);
        tempDate = new HashMap<>();

        Log.i("init","init");

    }

    public void showInfo(String info) {
        Log.i("info",info);
//        Toast.makeText(MainActivity.this, "info:" + info, Toast.LENGTH_LONG).show();
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        if(!info.equals("")) {
            final View dialog = inflater.inflate(R.layout.result_random_main,(ViewGroup)findViewById(R.id.resultDialog));
            TextView resultTitle = (TextView) dialog.findViewById(R.id.resultTitle);
            TextView resultItemName = (TextView) dialog.findViewById(R.id.resultItemName);
            TextView resultInfo = (TextView) dialog.findViewById(R.id.resultInfo);
            ImageView resultImg = (ImageView) dialog.findViewById(R.id.resultImage);
            resultTitle.setText("那就吃这个：");
            resultItemName.setText(info);
            builder.setNegativeButton(R.string.cancle,null);
            builder.setView(dialog);
            builder.show();
        }else {
            final View dialog = inflater.inflate(R.layout.result2_random_main,(ViewGroup)findViewById(R.id.result2Dialog));
            TextView resultTitle = (TextView) dialog.findViewById(R.id.result2Info);
            resultTitle.setText("先添加一些用餐地点吧！");
            builder.setNegativeButton(R.string.cancle,null);
            builder.setView(dialog);
            builder.show();
        }

//        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String imgName = "img" + imgNum%4;
//                int imgID = getResources().getIdentifier(imgName,"drawable",
//                        "com.example.fangngng.randomdish");
//                dishItem.add(MainActivity.this, input2.getText().toString(),
//                        info2.getText().toString(), dishType, imgID);
//                imgNum ++;
//                Log.i("imgNum:", String.valueOf(imgNum));
//                recrycleAdapter.notifyDataSetChanged();
//            }
//        });

    }

    //随机一个数据，并返回结果
    private String randomDish(List<Map<String, Object>> mData) {
        if (mData.size() > 0) {
            int b = (int) (Math.random() * mData.size());
            return dishItem.get().get(b).get("title").toString();
        }
        return "";
    }

    //添加一个新的数据
    private void AddNewDish () {
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.add_dialog_main,(ViewGroup)findViewById(R.id.dialog1));
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
                                info2.getText().toString(), dishType, imgID);
                        imgNum ++;
                        Log.i("imgNum:", String.valueOf(imgNum));
                        recrycleAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton(R.string.cancle,null);
        builder.setView(dialog);
        builder.show();

    }

    //撤销数据删除
    private void Undo(String title, String info, int imgID) {
        dishItem.add(MainActivity.this, title, info,"home",imgID);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem addItem = menu.findItem(R.id.action_add);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AddNewDish();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        List<Map<String, Object>> mData ;
        mData = dishItem.get();

        if (id == R.id.nav_add) {   //添加
            AddNewDish();
//            Intent intent = new Intent(MainActivity.this, MessageMainActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_refresh) {    //刷新
            dishItem.getDate(dishType);
            recrycleAdapter.notifyDataSetChanged();
        } else if (id == R.id.nav_random) {
            showInfo(randomDish( mData));
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, MessageMainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
            sendBroadcast(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
