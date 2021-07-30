//package com.example.sw_project.Activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class SettingActivity extends AppCompatActivity {
//
//    private Button logout;
//    private Button dropout;
//    //private Button passwordresetButton;
//    private FirebaseAuth mAuth;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        Button passwordresetButton = (Button) findViewById(R.id.passwordresetButton);
//        passwordresetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Setting2Activity.class);
//                startActivity(intent);
//            }
//        });
//
//        logout = findViewById((R.id.logout));
//        logout.setOnClickListener(View -> {
//            new AlertDialog.Builder(SettingActivity.this)
//                    .setMessage("로그아웃 하시겠습니까?")
//                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //AppDatabase.removeLoginAuthKey();
//                            mAuth.signOut(); // logout
//                            Toast.makeText(getApplicationContext(), "로그아읏 되었습니다.", Toast.LENGTH_SHORT).show();
//                            moveToLogin();
//                            dialog.dismiss(); //팝업창 종료
//                        }
//                    })
//                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //"아니오" 버튼 클릭 시 할 동작
//                            dialog.dismiss(); //팝업창 종료
//                        }
//                    }).show();
//        });
//        dropout = findViewById((R.id.dropout));
//        dropout.setOnClickListener(View -> {
//            new AlertDialog.Builder(SettingActivity.this)
//                    .setMessage("탈퇴 하시겠습니까?")
//                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //AppDatabase.removeLoginAuthKey();
//                            mAuth.getCurrentUser().delete(); // logout
//                            Toast.makeText(getApplicationContext(), "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
//                            moveToLogin();
//                            dialog.dismiss(); //팝업창 종료
//                        }
//                    })
//                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //"아니오" 버튼 클릭 시 할 동작
//                            dialog.dismiss(); //팝업창 종료
//                        }
//                    }).show();
//        });
//
//    }
//
//    private void moveToLogin() {
//        Intent intent = new Intent(SettingActivity.this, LogInActivity.class);
//        startActivity(intent);
//
//    }
//}