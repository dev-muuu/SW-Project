package com.example.sw_project.fragment;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw_project.Activity.AlarmSettingActivity;
import com.example.sw_project.Activity.ContestDetailActivity;
import com.example.sw_project.Activity.ContestScrapActivity;
import com.example.sw_project.Activity.MainActivity;
import com.example.sw_project.AlarmInfo;
import com.example.sw_project.ContestInfo;
import com.example.sw_project.R;
import com.example.sw_project.adapter.AlarmListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;

public class Fragment_Tab_4 extends Fragment {
    View view;

    private RecyclerView recycleView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView newAlarmText;
    private static final String TAG = "NoticeActivity";
    private FirebaseUser user;
    public FirebaseFirestore db;
    public ArrayList<AlarmInfo> alarmList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.activity_notice_page,container,false);

        recycleView = view.findViewById(R.id.alarmRecyclerView);
        view.findViewById(R.id.alarmSetButton).setOnClickListener(onClickListener);
        newAlarmText = view.findViewById(R.id.newAlarmText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        alarmListDataGet();

        recycleView.addOnItemTouchListener(new Fragment_Tab_4.RecyclerTouchListener(getContext(), recycleView, new Fragment_Tab_4.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                AlarmInfo alarmDetail = alarmList.get(position);
                String documentId = alarmDetail.getAlarmDocument();

                if(!alarmDetail.getIsRead()){
                    DocumentReference washingtonRef = db.collection("alarms").document(documentId);
                    washingtonRef
                            .update("isRead", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                Intent intent = new Intent(getContext(), ContestDetailActivity.class);
//                intent.putExtra("contestName",detailContest.getContestName());
                startActivity(intent);


            }
        }));

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //버튼 클릭시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alarmSetButton:
                    Log.d("click","click");
                    Intent intent = new Intent(getActivity(), AlarmSettingActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };

    public void alarmListDataGet(){

        alarmList = new ArrayList<>();

        db.collection("alarms")
                .whereEqualTo("destinationUid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int sum = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                alarmList.add(document.toObject(AlarmInfo.class));
                                if(!(boolean)document.getData().get("isRead")){
                                    sum += 1;
                                }
                            }
                            Collections.sort(alarmList);
                            recycleView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(getActivity());
                            recycleView.setLayoutManager(layoutManager);
                            mAdapter = new AlarmListAdapter(alarmList);
                            recycleView.setAdapter(mAdapter);
                            String newSum = String.format("새로운 알림(%d)",sum);
                            newAlarmText.setText(newSum);

                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // item 터치
    interface ClickListener {
        void onClick(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Fragment_Tab_4.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Fragment_Tab_4.ClickListener clickListener) {
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


}
