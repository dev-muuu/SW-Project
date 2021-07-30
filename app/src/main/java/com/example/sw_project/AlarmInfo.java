package com.example.sw_project;

import java.text.SimpleDateFormat;

public class AlarmInfo implements Comparable<AlarmInfo>{

    private String destinationUid;
    private String userNickname;
    private String uid;
    private int kind;
    private String message;
    private Long timeStamp;
    private boolean isRead;
    private String alarmDocument;
    private boolean isCheck = false;

    public AlarmInfo(String destinationUid, String userNickname, String uid, int kind, String message,
                     Long timeStamp, boolean isRead, String alarmDocument, boolean isCheck) {
        
        this.destinationUid = destinationUid;
        this.userNickname = userNickname;
        this.uid = uid;
        this.kind = kind;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
        this.alarmDocument = alarmDocument;
        this.isCheck = isCheck;
    }

    public AlarmInfo() {
    }

    // 월 일 구분 방법.... 다시 생각해보기

   @Override
    public int compareTo(AlarmInfo alarmInfo) {

       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd.HH.mm.ss");

       String a = simpleDateFormat.format(alarmInfo.getTimeStamp());
       String b = simpleDateFormat.format(timeStamp);
        if(a.compareTo(b) > 0){
            return 1;
        }else if(a.compareTo(b) < 0){
            return -1;
        }else{
            return 0;
        }
    }

    public String getDestinationUid() {
        return destinationUid;
    }

    public void setDestinationUid(String destinationUid) {
        this.destinationUid = destinationUid;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public String getAlarmDocument() {
        return alarmDocument;
    }

    public void setAlarmDocument(String alarmDocument) {
        this.alarmDocument = alarmDocument;
    }

    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean check) {
        isCheck = check;
    }
}
