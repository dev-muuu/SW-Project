package com.example.sw_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    private ArrayAdapter collegeadapter;
    private Spinner collegespinner;

    private ArrayAdapter majoradapter;
    private Spinner majorspinner;

    EditText passwordEditText, passwordCheckEditText, correctEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

        // 단과대 스크롤
        collegespinner = (Spinner) findViewById(R.id.collegeSpinner);
        collegeadapter = ArrayAdapter.createFromResource(this, R.array.college, android.R.layout.simple_spinner_dropdown_item);
        collegespinner.setAdapter(collegeadapter);

        // 학과 스크롤
        majorspinner = (Spinner) findViewById(R.id.majorSpinner);
        majoradapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_spinner_dropdown_item);
        majorspinner.setAdapter(majoradapter);

        passwordEditText = (EditText)findViewById(R.id.passwdText);
        passwordCheckEditText = (EditText)findViewById(R.id.passwdCheckText);
        correctEditText = (EditText) findViewById(R.id.correctEditText);
        passwordCheckEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())) {
                    // correct
                    correctEditText.setText("일치합니다.");
                } else {
                    // incorrect
                    correctEditText.setText("일치하지 않습니다.");
                }
            }
        });

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
                case R.id.signUpButton:
                    Log.e("click","click");
                    signUp();
                    break;

            }
        }
    };

    private void profileRegister(String uid){
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        Spinner spinner_co = (Spinner)findViewById(R.id.collegeSpinner);
        Spinner spinner_ma = (Spinner)findViewById(R.id.majorSpinner);
        String college = spinner_co.getSelectedItem().toString();
        String department = spinner_ma.getSelectedItem().toString();
        String studentId = ((EditText)findViewById(R.id.studentIdSet)).getText().toString();


        if(email.length() > 0){
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            StudentInfo studentInfo = new StudentInfo(email,userName,college,department,studentId);
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
