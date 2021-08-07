//package com.example.sw_project.Activity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.sw_project.adapter.PostListAdapter;
//import com.example.sw_project.R;
//import com.example.sw_project.WriteInfo;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import java.util.ArrayList;
//
//public class PostListActivity extends AppCompatActivity {
//
//    private Activity activity;
//    private final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//    private String TAG = "activity_postlist";
//    private  static FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_postlist);
//
//        db = FirebaseFirestore.getInstance();
//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//
//                            final ArrayList<WriteInfo> arrayList = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                arrayList.add(document.toObject(WriteInfo.class));
//                            }
//                            RecyclerView recyclerView = findViewById(R.id.recyclerview);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//                            RecyclerView.Adapter madapter = new PostListAdapter(arrayList,PostListActivity.this);
//                            recyclerView.setAdapter(madapter);
//
//                        }else {
//                            Log.w(TAG, "Error => ", task.getException());
//                        }
//                    }
//                });
//
//    }
//
//}
//
//
//
//
