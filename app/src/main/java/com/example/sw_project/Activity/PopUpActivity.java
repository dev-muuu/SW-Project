package com.example.sw_project.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw_project.ContestInfo;
import com.example.sw_project.R;
import com.example.sw_project.adapter.CalendarListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PopUpActivity extends Activity {

    private static final String TAG = "PopUpActivity";
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        //데이터 가져오기
        Intent intent = getIntent();
//        String value = intent.getStringExtra("contestDate");
        String dateTitle = intent.getStringExtra("dateTextTitle");
        ArrayList<ContestInfo> dateSameList = (ArrayList<ContestInfo>)intent.getSerializableExtra("list");

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        ArrayList<ContestInfo> scrapContestNameList = new ArrayList<>();

        findViewById(R.id.popUpCloseButton).setOnClickListener(onClickListener);
        RecyclerView recycleViewInCalendar = findViewById(R.id.popUpRecycleView);

        TextView contestEndDateCalendarText = findViewById(R.id.popUpDateText);
        contestEndDateCalendarText.setText(dateTitle);

        recycleViewInCalendar.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(PopUpActivity.this);
        recycleViewInCalendar.setLayoutManager(layoutManager);
        mAdapter = new CalendarListAdapter(dateSameList);
        recycleViewInCalendar.setAdapter(mAdapter);

//        db.collection("contests")
//                .whereEqualTo("endDate", value)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                scrapContestNameList.add(document.toObject(ContestInfo.class));
//                            }
//
//                            TextView contestEndDateCalendarText = findViewById(R.id.popUpDateText);
//                            contestEndDateCalendarText.setText(dateTitle);
//                            recycleViewInCalendar.setHasFixedSize(true);
//                            layoutManager = new LinearLayoutManager(PopUpActivity.this);
//                            recycleViewInCalendar.setLayoutManager(layoutManager);
//                            mAdapter = new CalendarListAdapter(scrapContestNameList);
//                            recycleViewInCalendar.setAdapter(mAdapter);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popUpCloseButton:
                    Log.e("click","click");
                    finish();
                    break;
            }

        }
    };

}
