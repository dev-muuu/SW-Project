package com.example.sw_project.Activity;

import android.content.Intent;
import android.os.AsyncTask;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sw_project.MailSend;
import com.example.sw_project.R;
import com.example.sw_project.StudentInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayAdapter collegeadapter;
    private Spinner collegespinner;

    private ArrayAdapter majoradapter;
    private Spinner majorspinner;
    private String certification;
    private boolean sswuStudentCertification = false;
    private AlertDialog dialog;

    EditText passwordEditText, passwordCheckEditText, correctEditText, canUseIdText, canEmailUseText, emailCertificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.canUseIdButton).setOnClickListener(onClickListener);
        findViewById(R.id.emailPushButton).setOnClickListener(onClickListener);

        // 단과대 스크롤
        collegespinner = (Spinner) findViewById(R.id.collegeSpinner);
        collegeadapter = ArrayAdapter.createFromResource(this, R.array.college, android.R.layout.simple_spinner_dropdown_item);
        collegespinner.setAdapter(collegeadapter);

        // 학과 스크롤
        majorspinner = (Spinner) findViewById(R.id.majorSpinner);
        majoradapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_spinner_dropdown_item);
        majorspinner.setAdapter(majoradapter);

        canEmailUseText = (EditText)findViewById(R.id.canEmailUseText);
        emailCertificationText = (EditText)findViewById(R.id.emailCertificationText);
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

        emailCertificationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (emailCertificationText.getText().toString().equals(certification)) {
                    // correct
                    canEmailUseText.setText("인증되었습니다.");
                    sswuStudentCertification = true;
                } else {
                    // incorrect
//                    canEmailUseText.setText("인증번호가 일치하지 않습니다.");
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signUpButton:
                    Log.e("click","click");
                    signUp();
                    break;
                case R.id.canUseIdButton:
                    Log.e("id","id");
                    isIdExist();
                    break;
                case R.id.emailPushButton:
                    Log.e("emailPush","emailPush");
                    emailCertification();
                    break;

            }
        }
    };

    private void emailCertification(){

        //초기화
        canEmailUseText.setText("");
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                canEmailUseText.setText("이미 인증이 완료된 이메일입니다.");
                                return;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(canEmailUseText.getText().toString().equals("")){

            //인증번호 문자/순자 5자리 생성
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < 5; i++){
                int index = random.nextInt(3);
                switch (index){
                    case 0 :
                        sb.append((char)(random.nextInt(26)+97));
                        break;
                    case 1 :
                        sb.append((char)(random.nextInt(26)+65));
                        break;
                    case 2 :
                        sb.append(random.nextInt(10));
                        break;
                }
            }
            System.out.println(sb);
            certification = sb.toString();

            AsyncTask.execute(() -> {
                MailSend.mailSend(email, certification);
            });
        }
    }

        private void profileRegister(String uid){
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        Spinner spinner_co = (Spinner)findViewById(R.id.collegeSpinner);
        Spinner spinner_ma = (Spinner)findViewById(R.id.majorSpinner);
        String college = spinner_co.getSelectedItem().toString();
        String department = spinner_ma.getSelectedItem().toString();
        String studentId = ((EditText)findViewById(R.id.studentIdSet)).getText().toString();
        String id = ((EditText)findViewById(R.id.idText)).getText().toString();
        String contestParticipate = ((EditText)findViewById(R.id.contestCountEditText)).getText().toString();

//        String emailCertificationText = ((EditText)findViewById(R.id.emailCertificationText)).getText().toString();

        if(sswuStudentCertification){
            StudentInfo studentInfo = new StudentInfo(email,userName,college,department,studentId, id, contestParticipate,uid);
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


    private void isIdExist(){

        canUseIdText = ((EditText)findViewById(R.id.canUseIdText));
        canUseIdText.setText("");
        String willUseIdText = ((EditText)findViewById(R.id.idText)).getText().toString();

        db.collection("users")
                .whereEqualTo("id", willUseIdText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                canUseIdText.setText("이미 존재하는 아이디입니다.");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        if(canUseIdText.getText().toString().equals("")){
            canUseIdText.setText("사용 가능한 아이디입니다.");
        }

    }

    private void signUp(){

        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String userName = ((EditText)findViewById(R.id.userNameText)).getText().toString();
        Spinner spinner_co = (Spinner)findViewById(R.id.collegeSpinner);
        Spinner spinner_ma = (Spinner)findViewById(R.id.majorSpinner);
        String college = spinner_co.getSelectedItem().toString();
        String department = spinner_ma.getSelectedItem().toString();
        String studentId = ((EditText)findViewById(R.id.studentIdSet)).getText().toString();
        String id = ((EditText)findViewById(R.id.idText)).getText().toString();
        String contestParticipate = ((EditText)findViewById(R.id.contestCountEditText)).getText().toString();

//        if (email.equals("") || userName.equals("") || college.equals("") || department.equals("")
//                || studentId.equals("") || id.equals("")
//                || contestParticipate.equals("")|| !(canEmailUseText.getText().equals("인증되었습니다."))
//                || !(correctEditText.getText().toString().equals("일치합니다."))) {
//
////            || college.equals("") || department.equals("")
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//
//            dialog = builder.setMessage("빈 칸이 있습니다.")
//                    .setNegativeButton("확인", null)
//                    .create();
//            dialog.show();
//        }
//        else {
            String idToEmail = ((EditText) findViewById(R.id.idText)).getText().toString();
            idToEmail += "@proj.com";
            String password = ((EditText) findViewById(R.id.passwdText)).getText().toString();
            mAuth.createUserWithEmailAndPassword(idToEmail, password)
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
//    }

    private void moveLogInActivity(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
