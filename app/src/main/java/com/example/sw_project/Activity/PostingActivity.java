package com.example.sw_project.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sw_project.AlarmInfo;
import com.example.sw_project.R;
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
import com.google.firebase.firestore.Source;

public class PostingActivity extends AppCompatActivity {

    private  static final String TAG = "Posting";
    private FirebaseUser user;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        findViewById(R.id.upLoadButton).setOnClickListener(onClickListener);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //버튼 클릭시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upLoadButton:
                    //게시글 등록 전 등록에 필요한 정보 추출 함
                    postInfoUpdate();
                    break;
            }

        }
    };

    private void postInfoUpdate() {

        WriteInfo writeInfo = new WriteInfo();

        final String wantEtc = ((EditText) findViewById(R.id.writeEtc)).getText().toString();    //기타
        final String regionScope = ((EditText) findViewById(R.id.writeRegionText)).getText().toString();   //선호 지역
        final String wantNum = ((EditText)findViewById(R.id.writeWantNum)).getText().toString();  //모집인원
        final String wantDept = ((EditText)findViewById(R.id.writeDeptText)).getText().toString(); //선호학과
        final String title = "";

        writeInfo.setCreatedAt(System.currentTimeMillis());
        writeInfo.setRegionScope(regionScope);
        writeInfo.setScrapNum(0);
        writeInfo.setTitle(title);
        writeInfo.setUserUid(user.getUid());
        writeInfo.setWantDept(wantDept);
        writeInfo.setWantEtc(wantEtc);
        writeInfo.setWantNum(wantNum);

        //checkBox 선택 여부 처리
        CheckBox zoomCheck = findViewById(R.id.zoom);
        CheckBox meetCheck = findViewById(R.id.meet);

        writeInfo.setZoomCheck(zoomCheck.isChecked());
        writeInfo.setMeetCheck(meetCheck.isChecked());

        //userName 정보 가져오기
        DocumentReference docRef = db.collection("users").document(user.getUid());
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Cached document data: " + document.getData());
                    String writerName = document.getData().get("userName").toString();
                    writeInfo.setWriterName(writerName);

                    //post 등록
                    if (wantNum.length() > 0 && wantEtc.length() > 0)
                        uploader(writeInfo);
                    else
                        startToast("모집 내용을 입력하시오.");
                }
                else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

    }

    private void uploader(final WriteInfo writeInfo){
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                        //postId 업데이
                        DocumentReference washingtonRef = db.collection("posts").document(documentReference.getId());
                        washingtonRef
                                .update("postid", documentReference.getId())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        startToast("게시글이 등록되었습니다.");
                                        postingAlarm(writeInfo);
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
                        startToast("게시글이 등록되지 않았습니다.");
                    }
                });

    }

    private void postingAlarm(final WriteInfo writeInfo){

        //임시 message
        String message = "Hi, This message uses for test";

        AlarmInfo alarmInfo = new AlarmInfo();

        alarmInfo.setDestinationUid("8glwlKqosYPghXZMRxMxW6CbNks2");
        alarmInfo.setUid(user.getUid());
        alarmInfo.setUserNickname(writeInfo.getWriterName());
        alarmInfo.setKind(0);
        alarmInfo.setTimeStamp(System.currentTimeMillis());
        alarmInfo.setIsRead(false);
        alarmInfo.setMessage(message);

        db.collection("alarms").add(alarmInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                        //postId 업데이
                        DocumentReference washingtonRef = db.collection("alarms").document(documentReference.getId());
                        washingtonRef
                                .update("alarmDocument", documentReference.getId())
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}


