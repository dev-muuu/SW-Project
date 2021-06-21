package com.example.sw_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sw_project.fragment.Fragment_Tab_1;
import com.example.sw_project.fragment.Fragment_Tab_2;
import com.example.sw_project.fragment.Fragment_Tab_3;
import com.example.sw_project.fragment.Fragment_Tab_4;
import com.example.sw_project.fragment.Fragment_Tab_5;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Fragment_Tab_1 fragment1 = new Fragment_Tab_1();
        Fragment_Tab_2 fragment2 = new Fragment_Tab_2();
        Fragment_Tab_3 fragment3 = new Fragment_Tab_3();
        Fragment_Tab_4 fragment4 = new Fragment_Tab_4();
        Fragment_Tab_5 fragment5 = new Fragment_Tab_5();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,fragment2).commitAllowingStateLoss();

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
}