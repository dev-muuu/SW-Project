package com.example.sw_project.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw_project.ContestInfo;
import com.example.sw_project.R;
import com.example.sw_project.ScrapInfo;
import com.example.sw_project.adapter.ContestScrapAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.StringTokenizer;

public class ContestScrapActivity extends Fragment {

    View view;

    public static ContestScrapActivity newInstance() {
        return new ContestScrapActivity();
    }

    private Activity activity;
    private MaterialCalendarView calendarView;
    private RecyclerView recycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "ContestScrapActivity";
    private final ArrayList<String> temporaryList = new ArrayList<>();
    private final ArrayList<CalendarDay> scrapContestDateList = new ArrayList<>();

    private ArrayList<ContestInfo> scrapContestList = new ArrayList<>();
//    private ArrayList<ContestInfo> scrapContestNameList = new ArrayList<>();

    // item 터치
    interface ClickListener {
        void onClick(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_my_page_contest_scrap,container,false);


        view.findViewById(R.id.showCalendarButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.showListButton).setOnClickListener(onClickListener);

        calendarView = view.findViewById(R.id.calendarMaterial);
        calendarView.addDecorators(new SundayRed(), new SaturdayBlue(), new OutOfMonth());

        recycleView = view.findViewById(R.id.contestListView);
        calendarView.setVisibility(View.INVISIBLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("scrapContest")
                .whereEqualTo("scrapUserUid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ScrapInfo> scrapArray = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                scrapArray.add(document.toObject(ScrapInfo.class));
                            }

                            // contestId에 해당하는 contest 불러오기
                            for(ScrapInfo findPost : scrapArray){
                                db.collection("contests")
                                        .whereEqualTo("contestId", findPost.getContestId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        scrapContestList.add(document.toObject(ContestInfo.class));
                                                        temporaryList.add(document.getData().get("endDate").toString());
                                                    }
                                                    convertDateType();
                                                    recycleView.setHasFixedSize(true);
                                                    layoutManager = new LinearLayoutManager(activity);
                                                    recycleView.setLayoutManager(layoutManager);
                                                    mAdapter = new ContestScrapAdapter(scrapContestList, getActivity());
                                                    recycleView.setAdapter(mAdapter);
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }

                        }else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int year, month, day;
                year = date.getYear() - 2000;
                month = date.getMonth()+1;
                day = date.getDay();
                String value = String.format("%d-%02d-%02d",year,month,day);
                System.out.println(value);
                String dateTitle = month+"월 "+day+"일";

                mOnPopupClick(value, dateTitle);

            }
        });

        recycleView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recycleView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContestInfo detailContest = scrapContestList.get(position);

                // ...?
                Intent intent = new Intent(getContext(), ContestDetailActivity.class);
                intent.putExtra("contestName",detailContest.getContestName());
                startActivity(intent);
            }
        }));

        return view;
    }

    public void mOnPopupClick(String value, String dateTitle){

        ArrayList<ContestInfo> dateSameList = new ArrayList<>();

        for(ContestInfo contest : scrapContestList){
            if(value.equals(contest.getEndDate()))
                dateSameList.add(contest);
        }

        Intent intent = new Intent(getActivity(), PopUpActivity.class);
        intent.putExtra("contestDate", value);
        intent.putExtra("dateTextTitle", dateTitle);
        intent.putExtra("list",dateSameList);
        startActivity(intent);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.showCalendarButton:
                    calendarView.setVisibility(View.VISIBLE);
                    recycleView.setVisibility(View.INVISIBLE);
                    break;
                case R.id.showListButton:
                    recycleView.setVisibility(View.VISIBLE);
                    calendarView.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };

    private void convertDateType(){
        String[] dateStorage = new String[3];
        for (String string : temporaryList) {
            StringTokenizer skn = new StringTokenizer(string, "-");
            for (int i = 0; skn.hasMoreTokens(); i++) {
                dateStorage[i] = skn.nextToken();
            }
            CalendarDay contestDay = CalendarDay.from(Integer.parseInt("20"+dateStorage[0])
                    , Integer.parseInt(dateStorage[1]) - 1, Integer.parseInt(dateStorage[2]));
            scrapContestDateList.add(contestDay);
        }
        calendarView.addDecorator(new ContestEndLineDayDecorate(scrapContestDateList));
    }

    class SaturdayBlue implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

        public SaturdayBlue(){
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

            return weekDay == calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    class SundayRed implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        public SundayRed(){
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    class OutOfMonth implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
            return monthDay == Calendar.DAY_OF_MONTH;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.GRAY));
        }
    }

    class ContestEndLineDayDecorate implements DayViewDecorator{

        private final ArrayList<CalendarDay> dates;

        public ContestEndLineDayDecorate(Collection<CalendarDay> dates) {
            this.dates = new ArrayList<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(15,Color.RED));
        }
    }


}

