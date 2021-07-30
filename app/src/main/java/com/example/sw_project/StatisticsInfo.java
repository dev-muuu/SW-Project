package com.example.sw_project;

import java.util.HashMap;

public class StatisticsInfo {

    private int scrapNum;
    private HashMap<String, Integer> schoolNumCount;
    private HashMap<String, Integer> majorCount;

    public HashMap<String, Integer> getSchoolNumCount() {
        return schoolNumCount;
    }

    public void setSchoolNumCount(HashMap<String, Integer> schoolNumCount) {
        this.schoolNumCount = schoolNumCount;
    }

    public HashMap<String, Integer> getMajorCount() {
        return majorCount;
    }

    public void setMajorCount(HashMap<String, Integer> majorCount) {
        this.majorCount = majorCount;
    }

    public int getScrapNum() {
        return scrapNum;
    }

    public void setScrapNum(int scrapNum) {
        this.scrapNum = scrapNum;
    }
}

class ContestStatisticsInfo{

    //{a,b} a: 공모전 참여횟수, b: 학생
    private HashMap<String, int[]> schoolNum;
    private HashMap<String, int[]> major;

    public HashMap<String, int[]> getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(HashMap<String, int[]> schoolNum) {
        this.schoolNum = schoolNum;
    }

    public HashMap<String, int[]> getMajor() {
        return major;
    }

    public void setMajor(HashMap<String, int[]> major) {
        this.major = major;
    }
}
