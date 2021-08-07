package com.example.sw_project.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw_project.R;
import com.example.sw_project.WriteInfo;
import com.example.sw_project.adapter.PostListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Random;

public class Fragment_Tab_1 extends Fragment {

    View view;
    private Activity activity;
    private final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String TAG = "activity_postlist";
    public FirebaseFirestore db;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_postlist, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerview);

        listDbGet();

        return view;
    }

    public void init(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecyclerView.Adapter madapter = new PostListAdapter(new ArrayList<WriteInfo>(), getActivity());
        recyclerView.setAdapter(madapter);
    }

    public void listDbGet(){

        init();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) { // 데이터를 가져오는 작업이 잘 동작했을 떄
                            final ArrayList<WriteInfo> arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                arrayList.add(document.toObject(WriteInfo.class));
                            }
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            RecyclerView.Adapter madapter = new PostListAdapter(arrayList, getActivity());
                            recyclerView.setAdapter(madapter);
                        } else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });
    }
}
