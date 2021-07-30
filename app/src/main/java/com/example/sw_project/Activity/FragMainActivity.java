//package com.example.sw_project.Activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager.widget.ViewPager;
//
//import android.os.Bundle;
//
//import com.example.sw_project.R;
//import com.example.sw_project.adapter.FragmentAdapter;
//import com.example.sw_project.fragment.FragmentAll;
//import com.example.sw_project.fragment.FragmentDesign;
//import com.example.sw_project.fragment.FragmentIT;
//import com.example.sw_project.fragment.FragmentMarketing;
//import com.example.sw_project.fragment.FragmentPlan;
//import com.google.android.material.tabs.TabLayout;
//
//public class FragMainActivity extends AppCompatActivity {
//
//    private static final String TAG = "Main_Activity";
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    private FragmentAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.frag_activity_main);
//
//        tabLayout=findViewById(R.id.tabs);
//        viewPager=findViewById(R.id.view_pager);
//        adapter=new FragmentAdapter(getSupportFragmentManager(),1);
//
//        //FragmentAdapter에 컬렉션 담기
//        adapter.addFragment(new FragmentAll());
//        adapter.addFragment(new FragmentPlan());
//        adapter.addFragment(new FragmentMarketing());
//        adapter.addFragment(new FragmentIT());
//        adapter.addFragment(new FragmentDesign());
//
//        //ViewPager Fragment 연결
//        viewPager.setAdapter(adapter);
//
//        //ViewPager과 TabLayout 연결
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.getTabAt(0).setText("전체");
//        tabLayout.getTabAt(1).setText("기획");
//        tabLayout.getTabAt(2).setText("마케팅");
//        tabLayout.getTabAt(3).setText("IT");
//        tabLayout.getTabAt(4).setText("디자인");
//    }
//}
