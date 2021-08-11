package com.example.sw_project.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.sw_project.Activity.LogInActivity;
import com.example.sw_project.R;
import com.google.firebase.auth.FirebaseAuth;



public class Fragment_Tab_5 extends Fragment {

    View view;

    private Button logout;
    private Button dropout;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.activity_setting,container,false);

        mAuth = FirebaseAuth.getInstance();

//        Button passwordresetButton = (Button) view.findViewById(R.id.passwordresetButton);
//        passwordresetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Setting2Activity.class);
//                startActivity(intent);
//            }
//        });

        logout = view.findViewById((R.id.logout));
        logout.setOnClickListener(View -> {
            new AlertDialog.Builder(getActivity())
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //AppDatabase.removeLoginAuthKey();
                            mAuth.signOut(); // logout
                            Toast.makeText(getContext(), "로그아읏 되었습니다.", Toast.LENGTH_SHORT).show();
                            moveToLogin();
                            dialog.dismiss(); //팝업창 종료
                        }
                    })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //"아니오" 버튼 클릭 시 할 동작
                            dialog.dismiss(); //팝업창 종료
                        }
                    }).show();
        });

        dropout = view.findViewById((R.id.dropout));
        dropout.setOnClickListener(View -> {
            new AlertDialog.Builder(getActivity())
                    .setMessage("탈퇴 하시겠습니까?")
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //AppDatabase.removeLoginAuthKey();
                            mAuth.getCurrentUser().delete(); // logout
                            Toast.makeText(getContext(), "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                            moveToLogin();
                            dialog.dismiss(); //팝업창 종료
                        }
                    })
                    .setNegativeButton("아니", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //"아니오" 버튼 클릭 시 할 동작
                            dialog.dismiss(); //팝업창 종료
                        }
                    }).show();
        });
        return view;
    }

    private void moveToLogin() {
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        startActivity(intent);

    }
}
