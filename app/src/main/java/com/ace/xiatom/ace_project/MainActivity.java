package com.ace.xiatom.ace_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserApplication userApplication;
    private final int loginRequestCode = 1;
    private String loginUser = null;
    View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_bar);

        userApplication = (UserApplication)this.getApplication();
        String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
        userApplication.setLocationManager((LocationManager)getSystemService(serviceString));
        userApplication.setContext(this);
        userApplication.setIp("10.240.252.96");
        userApplication.setHer("ace");
        LinearLayout l = findViewById(R.id.contentLayout);
        getLayoutInflater().inflate(R.layout.content_main,l);
        //设置顶部actionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //侧边栏
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //toolbar上有点出抽屉图标
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏的抽屉
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //进入注册界面
        headerView=navigationView.inflateHeaderView(R.layout.side_bar_head);
        ImageView userPhoto = headerView.findViewById(R.id.photo);
        userPhoto.setOnClickListener(login);


        //底部菜单选择
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(MainActivity.this,LoginActivity.class),loginRequestCode);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case loginRequestCode:
                if(resultCode==RESULT_OK){
                    ImageView userPhoto = headerView.findViewById(R.id.photo);
                    TextView userName = headerView.findViewById(R.id.username);
                    loginUser = userApplication.getName();
                    int photoRes = data.getIntExtra("photo",R.mipmap.ic_launcher_round);
                    userPhoto.setImageResource(photoRes);
                    userName.setText(loginUser);
                    Log.i("msg",userName.getText().toString());
                }
        }
    }

    //底部菜单监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.bottom_chat:
                    //未登录
                    if(userApplication.getConnection()==null){
                        Toast.makeText(MainActivity.this,"您尚未登陆",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Log.i("chat:","h12");
                    Intent chatIntent = new Intent(MainActivity.this,ChatActivity.class);
                    startActivity(chatIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_bar_items view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
//            LinearLayout l = findViewById(R.id.contentLayout);
//            l.removeAllViews();
//            getLayoutInflater().inflate(R.layout.content_main,l);

            startActivity(new Intent(MainActivity.this,PhotoActivity.class));
            Toast.makeText(this, "gallery", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this, "sildeshow", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,CalendarActivity.class));

        }  else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this,GPSActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
