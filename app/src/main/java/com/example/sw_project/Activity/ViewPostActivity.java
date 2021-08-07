package com.example.sw_project.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sw_project.ContestInfo;
import com.example.sw_project.R;
import com.example.sw_project.ScrapInfo;
import com.example.sw_project.WriteInfo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewPostActivity extends AppCompatActivity {

    private TextView titleView,writerView,dateView,regionView,wantnumView,wantdepView,etcView;
    private Activity activity;
    private WriteInfo writeinfo;
    private ScrapInfo alreadyScrap;
    private String newScrapId;
    private FirebaseUser user;
    private String TAG = "ScrapActivity";
    private FirebaseFirestore db;
    private Button scrapButton, scrapCancelButton;
    private ArrayList<String> commentId, scrapId;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        scrapButton = findViewById(R.id.scrapButton);
        scrapButton.setOnClickListener(onClickListener);
        scrapCancelButton = findViewById(R.id.scrapCancelButton);
        scrapCancelButton.setOnClickListener(onClickListener);
        findViewById(R.id.moveContestPageButton).setOnClickListener(onClickListener);

        Intent intent = getIntent();// 인텐트 받아오기
        writeinfo = (WriteInfo) intent.getSerializableExtra("writeInfo");

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        ActionBar actionBar = getSupportActionBar();
        if(writeinfo.getUserUid().equals(user.getUid()))
            actionBar.setTitle("");
        else
            actionBar.hide();

//        titleView =findViewById(R.id.titleView);
//        titleView.setText(extras.getString("title"));

        writerView =findViewById(R.id.writerView);
        String writerText = String.format("글쓴이: %s",writeinfo.getWriterName());
        writerView.setText(writerText);

        dateView = findViewById(R.id.dateView);
        SimpleDateFormat sdf = new SimpleDateFormat("작성일: yy.MM.dd");
        String dateText = sdf.format(new Date(writeinfo.getCreatedAt()));
        dateView.setText(dateText);

        regionView = findViewById(R.id.regionView);
        regionView.setText(writeinfo.getRegionScope());

        wantnumView = findViewById(R.id.wantnumView);
        wantnumView.setText(writeinfo.getWantNum());

        wantdepView = findViewById(R.id.wantdepView);
        wantdepView.setText(writeinfo.getWantDept());

        etcView =findViewById(R.id.etcView);
        etcView.setText(writeinfo.getWantEtc());

        CheckBox meet = findViewById(R.id.meetCheckBox);
        CheckBox zoom = findViewById(R.id.zoomCheckBox);
        meet.setChecked(writeinfo.isMeetCheck());
        zoom.setChecked(writeinfo.isZoomCheck());

        //색상 등 수정 필요......
        meet.setEnabled(false);
        zoom.setEnabled(false);

        scrapCancelButton.setVisibility(View.INVISIBLE);
        scrapButton.setVisibility(View.VISIBLE);

        db.collection("scrapsPost")
                .whereEqualTo("postId", writeinfo.getPostid())
                .whereEqualTo("scrapUserUid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                alreadyScrap = document.toObject(ScrapInfo.class);
                                scrapButton.setVisibility(View.INVISIBLE);
                                scrapCancelButton.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scrapButton:
                    scrapDbUpload();
                    break;
                case R.id.scrapCancelButton:
                    scrapDbDelete();
                    break;
                case R.id.moveContestPageButton:
                    moveContestActivity();
                    break;
            }
        }
    };

    private void moveContestActivity(){

        DocumentReference docRef = db.collection("contests").document(writeinfo.getContestId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ContestInfo contestInfo = documentSnapshot.toObject(ContestInfo.class);

                Intent intent = new Intent(getApplicationContext(), ContestDetailActivity.class);
                intent.putExtra("contestDetail", contestInfo);
                startActivity(intent);
            }
        });
    }

    private void scrapDbUpload(){

        ScrapInfo scrapInfo = new ScrapInfo();
        scrapInfo.setPostId(writeinfo.getPostid());
        scrapInfo.setScrapUserUid(user.getUid());

        db.collection("scrapsPost")
                .add(scrapInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        DocumentReference washingtonRef = db.collection("scrapsPost").document(documentReference.getId());
                        washingtonRef
                                .update("scrapId", documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        newScrapId = documentReference.getId();
                                        scrapNumberIncrease();
                                        startToast("해당 게시글이 스크랩되었습니다.");
                                        scrapButton.setVisibility(View.INVISIBLE);
                                        scrapCancelButton.setVisibility(View.VISIBLE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void scrapNumberIncrease(){

        final DocumentReference sfDocRef = db.collection("posts").document(writeinfo.getPostid());
        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newScrapNum = snapshot.getDouble("scrapNum") + 1;
                transaction.update(sfDocRef, "scrapNum", (int)newScrapNum);
                return newScrapNum;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d(TAG, "Transaction success: " + result);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }

    private void scrapDbDelete(){

        //스크랩 후 바로 스크랩 취소하는 경우
        String document;
        try{
            document = alreadyScrap.getScrapId();
        }catch (NullPointerException e){
            document = newScrapId;
        }

        db.collection("scrapsPost").document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        scrapNumberDecrease();
                        startToast("해당 게시글 스크랩 취소되었습니다.");
                        scrapButton.setVisibility(View.VISIBLE);
                        scrapCancelButton.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void scrapNumberDecrease(){

        final DocumentReference sfDocRef = db.collection("posts").document(writeinfo.getPostid());
        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newScrapNum = snapshot.getDouble("scrapNum") - 1;
                transaction.update(sfDocRef, "scrapNum", (int)newScrapNum);
                return newScrapNum;
            }
        })
                .addOnSuccessListener(new OnSuccessListener<Double>() {
                    @Override
                    public void onSuccess(Double result) {
                        Log.d(TAG, "Transaction success: " + result);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }


    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        switch(item.getItemId()) {
            case R.id.delete:
                builder = new AlertDialog.Builder(ViewPostActivity.this);
                dialog = builder.setMessage("작성한 팀원 모집글을 삭제합니다")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postDeleteFunc();
                            }
                        })
                        .create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postDeleteFunc(){

        db.collection("posts").document(writeinfo.getPostid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("게시글을 삭제하였습니다");
                        relatedPostDbCheck();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("게시글을 삭제하지 못하였습니다.");
                    }
                });
    }

    private void relatedPostDbCheck(){

        commentId = new ArrayList<>();
        scrapId = new ArrayList<>();

        db.collection("comments")
                .whereEqualTo("postid", writeinfo.getPostid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                commentId.add(document.getId());
                            }
                            deleteRelated(commentId, "comments");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("scrapsPost")
                .whereEqualTo("postId", writeinfo.getPostid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                scrapId.add(document.getId());
                            }
                            deleteRelated(scrapId, "scrapsPost");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void deleteRelated(final ArrayList<String> list, final String collection){

        for(String id : list){
            db.collection(collection).document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
    }


}