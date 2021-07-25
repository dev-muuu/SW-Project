package com.example.sw_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sw_project.R;
import com.example.sw_project.fragment.Fragment_Tab_1;
import com.example.sw_project.fragment.Fragment_Tab_2;
import com.example.sw_project.fragment.Fragment_Tab_3;
import com.example.sw_project.fragment.Fragment_Tab_4;
import com.example.sw_project.fragment.Fragment_Tab_5;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private static final String TAG="MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment_Tab_1 fragment1 = new Fragment_Tab_1();
        Fragment_Tab_2 fragment2 = new Fragment_Tab_2();
        Fragment_Tab_3 fragment3 = new Fragment_Tab_3();
        Fragment_Tab_4 fragment4 = new Fragment_Tab_4();
        Fragment_Tab_5 fragment5 = new Fragment_Tab_5();


        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment3).commitAllowingStateLoss();
        findViewById(R.id.bottom_tab_3).performClick();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.bottom_tab_1:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment1).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.bottom_tab_2:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment2).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.bottom_tab_3:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment3).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.bottom_tab_4:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment4).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.bottom_tab_5:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment5).commitAllowingStateLoss();
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, fragment).commit();
        transaction.addToBackStack(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            //fragment4 알림 편집 액티비티 종료 이후 자동 새로고침
            Fragment_Tab_4 mf = (Fragment_Tab_4) getSupportFragmentManager().findFragmentById(R.id.main_layout);
            mf.alarmListDataGet();
        } catch (ClassCastException exception) {
            return;
        }
    }

}