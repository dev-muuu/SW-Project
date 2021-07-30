//package com.example.sw_project.Activity;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.sw_project.ContestInfo;
//import com.example.sw_project.R;
//import com.example.sw_project.adapter.ContestScrapAdapter;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class NoticeActivity extends MainActivity {
//
//    private RecyclerView recycleView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notice_page);
//
//        recycleView = findViewById(R.id.alarmRecyclerView);
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("contest")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                scrapContestList.add(new ContestInfo(document.getData().get("contestName").toString(),
//                                        document.getData().get("endLine").toString()));
//                                temporaryList.add(document.getData().get("endLine").toString());
//                            }
//                            recycleView.setHasFixedSize(true);
//                            layoutManager = new LinearLayoutManager();
//                            recycleView.setLayoutManager(layoutManager);
//                            mAdapter = new ContestScrapAdapter(scrapContestList);
//                            recycleView.setAdapter(mAdapter);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//    }
//
//}
