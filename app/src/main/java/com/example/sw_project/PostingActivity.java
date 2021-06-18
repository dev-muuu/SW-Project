package com.example.sw_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostingActivity extends MainActivity {

    private  static final String TAG="WritePostActivity";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        findViewById(R.id.upLoadButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //버튼 클릭시
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upLoadButton:
                    postInfoUpdate(); //게시글 등록
                    break;
            }

        }
    };

    private void postInfoUpdate() {
        final String wantEtc = ((EditText) findViewById(R.id.writeEtc)).getText().toString();    //기타
        final String regionScope = ((EditText) findViewById(R.id.writeRegionText)).getText().toString();   //선호 지역
        final String wantNum = ((EditText)findViewById(R.id.writeWantNum)).getText().toString();  //모집인원
        final String wantDept = ((EditText)findViewById(R.id.writeDeptText)).getText().toString(); //선호학과


        if (wantNum.length() > 0 && wantEtc.length() > 0) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo writeInfo = new WriteInfo(wantNum,wantEtc,wantDept,regionScope, user.getUid());
            uploader(writeInfo);
        }
        else {
            //내용 입력 안했을 떄
            startToast("모집 내용을 입력하시오.");
        }


    }
    private void uploader(WriteInfo writeInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        startToast("게시글이 등록되었습니다.");
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

    private void startToast(String msg) //내용 입력안할 시 뜨는 창함수
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();

    }
}


