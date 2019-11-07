package com.example.mypolicy.model;

import java.util.Date;

public class Review {
    int review_code;
    int p_code;
    String review_uID;
    String contents;
    Date review_time;

    public int getReview_code() {
        return review_code;
    }

    public void setReview_code(int review_code) {
        this.review_code = review_code;
    }

    public int getP_code() {
        return p_code;
    }

    public void setP_code(int p_code) {
        this.p_code = p_code;
    }

    public String getReview_uID() {
        return review_uID;
    }

    public void setReview_uID(String review_uID) {
        this.review_uID = review_uID;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getReview_time() {
        return review_time;
    }

    public void setReview_time(Date review_time) {
        this.review_time = review_time;
    }
}
