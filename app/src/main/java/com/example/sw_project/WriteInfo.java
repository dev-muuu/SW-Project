package com.example.sw_project;

import com.google.firebase.auth.FirebaseUser;

public class WriteInfo {

    private String wantEtc; //기타란
    private String regionScope; //선호지역
    private String wantDept; //선호학과
    private String wantNum; //모집인원
    private String userUid; //모집인원



    public WriteInfo(String wantNum, String wantEtc, String wantDept, String regionScope, String userUid) {
        this.wantNum = wantNum;
        this.wantEtc = wantEtc;
        this.wantDept = wantDept;
        this.regionScope = regionScope;
        this.userUid = userUid;

    }

    public String getWantEtc() {
        return wantEtc;
    }

    public void setWantEtc(String wantEtc) {
        this.wantEtc = wantEtc;
    }

    public String getRegionScope() {
        return regionScope;
    }

    public void setRegionScope(String regionScope) {
        this.regionScope = regionScope;
    }

    public String getWantDept() {
        return wantDept;
    }

    public void setWantDept(String wantDept) {
        this.wantDept = wantDept;
    }

    public String getWantNum() {
        return wantNum;
    }

    public void setWantNum(String wantNum) {
        this.wantNum = wantNum;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
