package com.example.sw_project;

import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginButton:
                    Log.e("click","click");
                    signUp();
                    break;

            }
        }
    };

    private void profileRegister(String uid){
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        String college = ((EditText)findViewById(R.id.collegeSet)).getText().toString();
        String department = ((EditText)findViewById(R.id.departmentSet)).getText().toString();
        String studentId = ((EditText)findViewById(R.id.studentIdSet)).getText().toString();


        if(email.length() > 0){
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            StudentInfo studentInfo = new StudentInfo(email,userName,college,department,studentId);
//            db.collection("users").document(user.getUid()).set(studentInfo)
            db.collection("users").document(uid).set(studentInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            startToast("회원정보 등록을 성공하였습니다");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startToast("회원정보 등록을 실패하였습니다");
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }else{
            startToast("회원정보를 입력하세요");
        }
    }

    private void signUp(){

        String email = ((EditText)findViewById(R.id.idText)).getText().toString();
        email += "@proj.com";
        String password = ((EditText)findViewById(R.id.passwdText)).getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    final String uid = task.getResult().getUser().getUid();
                                    profileRegister(uid);
                                    moveLogInActivity();
                                    //UI
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    //UI
                                }
                            }
                        }
                );
    }

    private void moveLogInActivity(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
