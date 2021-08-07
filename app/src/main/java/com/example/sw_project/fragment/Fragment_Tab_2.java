package com.example.sw_project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sw_project.Activity.ContestScrapActivity;
import com.example.sw_project.Activity.FragMainActivity;
import com.example.sw_project.Activity.MainActivity;
import com.example.sw_project.Activity.SSWUMainActivity;
import com.example.sw_project.R;
import com.example.sw_project.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

//public class Fragment_Tab_2 extends Fragment {
//
//    View view;
//
//    private static final String TAG = "Main_Activity";
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    private FragmentAdapter adapter;
//
//    public static Fragment_Tab_2 newInstance() {
//        return new Fragment_Tab_2();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        view = (ViewGroup) inflater.inflate(R.layout.frag_activity_main,container,false);
//
//        tabLayout = view.findViewById(R.id.tabs);
//        viewPager = view.findViewById(R.id.view_pager);
//        //getFragmentManager을 getSupportFragmentManager대신 사용
//        adapter = new FragmentAdapter(getFragmentManager(),1);
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
//
//        return view;
//    }
//
////    private void setChildFragment(Fragment child) {
////
////        FragmentTransaction childFt = getChildFragmentManager().beginTransaction();
////
////        if (!child.isAdded()) {
////            childFt.replace(R.id.tabs, child);
////            childFt.addToBackStack(null);
////            childFt.commit();
////        }
////    }
//
//}

public class Fragment_Tab_2 extends Fragment {

    View view;

    private static final String TAG = "Fragment_2";

    public static Fragment_Tab_2 newInstance() {
        return new Fragment_Tab_2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_2,container,false);

        view.findViewById(R.id.inContestButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.outContestButton).setOnClickListener(onClickListener);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //버튼 클릭시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.inContestButton:
                    ((MainActivity)getActivity()).replaceFragment(SSWUMainActivity.newInstance(), "inContest");
                    break;
                case R.id.outContestButton:
                    ((MainActivity)getActivity()).replaceFragment(FragMainActivity.newInstance(), "outContest");
                    break;
            }
        }
    };
}