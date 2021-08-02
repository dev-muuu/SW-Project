package com.example.sw_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sw_project.Activity.PostingActivity;
import com.example.sw_project.ContestInfo;
import com.example.sw_project.ParticipateInfo;
import com.example.sw_project.R;
import com.example.sw_project.StudentInfo;
import com.example.sw_project.WriteInfo;
import com.example.sw_project.adapter.PostListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class ContestDetailListFragment extends Fragment {

    View view;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String TAG = "ContestDetailListFragment";
    private ContestInfo contestInfo;
    private StudentInfo info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_contest_scrap_postlist, container, false);

        Bundle bundle = getArguments();
        contestInfo = (ContestInfo)bundle.getSerializable("contestInfo");

        view.findViewById(R.id.movePostingButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.alreadyPartiButton).setOnClickListener(onClickListener);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("participate")
                .whereEqualTo("contestId",contestInfo.getContestId())
                .whereEqualTo("userUid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<WriteInfo> arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                view.findViewById(R.id.alreadyPartiButton).setEnabled(false);
                            }
                        } else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });

        db.collection("posts")
                .whereEqualTo("contestId",contestInfo.getContestId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            final ArrayList<WriteInfo> arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                arrayList.add(document.toObject(WriteInfo.class));
                            }
                            RecyclerView recyclerView = view.findViewById(R.id.postlistRecycle);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            RecyclerView.Adapter mAdapter = new PostListAdapter(arrayList, getActivity());
                            recyclerView.setAdapter(mAdapter);

                        }else {
                            Log.w(TAG, "Error => ", task.getException());
                        }
                    }
                });


        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //버튼 클릭시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.movePostingButton:
                    Intent intent = new Intent(getActivity(), PostingActivity.class);
                    intent.putExtra("contestDetail", contestInfo);
                    getActivity().startActivity(intent);
                    break;
                case R.id.alreadyPartiButton:
                    participateContest();
                    break;
            }
        }
    };

    private void participateContest(){

        ParticipateInfo participate = new ParticipateInfo();
        participate.setContestId(contestInfo.getContestId());
        participate.setUserUid(user.getUid());

        db.collection("participate")
                .add(participate)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        final DocumentReference sfDocRef = db.collection("users").document(user.getUid());
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                info = snapshot.toObject(StudentInfo.class);

                int newParticipateNum = Integer.parseInt(snapshot.getString("contestParticipate")) + 1;
                transaction.update(sfDocRef, "contestParticipate", String.valueOf(newParticipateNum));

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                updateStatisticsDB();
                startToast("해당 공모전을 참여했습니다.");
                view.findViewById(R.id.alreadyPartiButton).setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }

    private void updateStatisticsDB(){

        final DocumentReference sfDocRef = db.collection("statistics").document("contestParticipateDocument");
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                HashMap<String, ArrayList> major = (HashMap<String, ArrayList>) snapshot.getData().get("major");
                HashMap<String, ArrayList> schoolNum = (HashMap<String, ArrayList>) snapshot.getData().get("schoolNum");

                Long preCollege = (Long) major.get(info.getCollege()).get(0);
                major.get(info.getCollege()).set(0,preCollege.intValue() + 1);

                Long preNum = (Long) schoolNum.get(info.getStudentId()).get(0);
                schoolNum.get(info.getStudentId()).set(0,preNum.intValue() + 1);

                transaction.update(sfDocRef, "major", major);
                transaction.update(sfDocRef, "schoolNum", schoolNum);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Transaction failure.", e);
            }
        });

    }

    private void startToast(String msg){
        Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
    }



}
