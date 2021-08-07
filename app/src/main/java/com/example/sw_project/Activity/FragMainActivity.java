package com.example.sw_project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sw_project.R;
import com.example.sw_project.adapter.FragmentAdapter;
import com.example.sw_project.fragment.FragmentAll;
import com.example.sw_project.fragment.FragmentDesign;
import com.example.sw_project.fragment.FragmentIT;
import com.example.sw_project.fragment.FragmentMarketing;
import com.example.sw_project.fragment.FragmentPlan;
import com.google.android.material.tabs.TabLayout;

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

public class FragMainActivity extends Fragment {

    View view;

    private static final String TAG = "Main_Activity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

    public static FragMainActivity newInstance() {
        return new FragMainActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.frag_activity_main,container,false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        //getFragmentManager을 getSupportFragmentManager대신 사용
        adapter = new FragmentAdapter(getFragmentManager(),1);

        //FragmentAdapter에 컬렉션 담기
        adapter.addFragment(new FragmentAll());
        adapter.addFragment(new FragmentPlan());
        adapter.addFragment(new FragmentMarketing());
        adapter.addFragment(new FragmentIT());
        adapter.addFragment(new FragmentDesign());

        //ViewPager Fragment 연결
        viewPager.setAdapter(adapter);

        //ViewPager과 TabLayout 연결
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("전체");
        tabLayout.getTabAt(1).setText("기획");
        tabLayout.getTabAt(2).setText("마케팅");
        tabLayout.getTabAt(3).setText("IT");
        tabLayout.getTabAt(4).setText("디자인");

        return view;
    }

//    private void setChildFragment(Fragment child) {
//
//        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();
//
//        if (!child.isAdded()) {
//            childFt.replace(R.id.tabs, child);
//            childFt.addToBackStack(null);
//            childFt.commit();
//        }
//    }

}