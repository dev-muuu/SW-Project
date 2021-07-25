package com.example.sw_project;

import java.io.Serializable;

public class WriteInfo implements Serializable { //인텐드로 class보내기위해 Serializable implement


    private String title;
    private String wantEtc; //기타란
    private String regionScope; //선호지역
    private String wantDept; //선호학과
    private String wantNum; //모집인원
    private String userUid;

    private Long createdAt; //모집글 작성일
    private int scrapNum;
    private String postid; //글 삭제시 필요한 포스트 id
    private String writerName;
    private String contestId;

    private boolean zoomCheck;
    private boolean meetCheck;

    public WriteInfo(){
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public int getScrapNum() {
        return scrapNum;
    }

    public void setScrapNum(int scrapNum) {
        this.scrapNum = scrapNum;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public boolean isZoomCheck() {
        return zoomCheck;
    }

    public void setZoomCheck(boolean zoomCheck) {
        this.zoomCheck = zoomCheck;
    }

    public boolean isMeetCheck() {
        return meetCheck;
    }

    public void setMeetCheck(boolean meetCheck) {
        this.meetCheck = meetCheck;
    }
}

