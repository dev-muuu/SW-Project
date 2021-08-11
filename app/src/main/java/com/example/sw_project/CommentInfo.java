package com.example.sw_project;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommentInfo implements Serializable {

    private String Commentwriter;
    private String userUid;
    private String contents;
    private String createdAt;
    private String postid;
    private String commentid;

    public String getCommentwriter() {
        return Commentwriter;
    }

    public void setCommentwriter(String Commentwriter) { this.Commentwriter=Commentwriter; }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) { this.userUid=userUid; }

    public String getContents() { return contents; }

    public void setContents(String contents) { this.contents = contents; }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPostid() {return postid;}

    public void setPostid(String postid) {this.postid=postid;}

    public String getCommentid() {return commentid;}

    public void setCommentid(String commentid) {this.commentid=commentid;}





}


