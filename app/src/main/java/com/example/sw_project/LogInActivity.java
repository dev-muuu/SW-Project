package com.example.sw_project;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logInButton).setOnClickListener(onClickListener);
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

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
                case R.id.logInButton:
                    Log.e("click","click");
                    LogIn();
                    break;
                case R.id.signUpButton:
                    Log.e("cli","cli");
                    startSignUpActivity();
                    break;

            }
        }
    };

    private void LogIn(){

        String email = ((EditText)findViewById(R.id.idText)).getText().toString();
        email += "@proj.com";
        String password = ((EditText)findViewById(R.id.passwdText)).getText().toString();
        if(email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다");
                                startMainActivity();

                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }

                            }
                        }
                    });
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요");
        }
    }

    private void startSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
//임시로 posting 설
    private void startMainActivity(){
        Intent intent = new Intent(this, PostingActivity.class);
        startActivity(intent);
    }


    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}