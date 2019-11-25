package com.example.mypolicy.model;

import java.util.Date;

public class SearchData {
    private long p_code;
    private String title;
    private Date apply_start;
    private Date apply_end;
    private String category;
    private String region;
    private int match_score;


    public long getP_code() {
        return p_code;
    }

    public void setP_code(long p_code) {
        this.p_code = p_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getApply_start() {
        return apply_start;
    }

    public void setApply_start(Date apply_start) {
        this.apply_start = apply_start;
    }

    public Date getApply_end() {
        return apply_end;
    }

    public void setApply_end(Date apply_end) {
        this.apply_end = apply_end;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getMatch_score() {
        return match_score;
    }

    public void setMatch_score(int match_score) {
        this.match_score = match_score;
    }
}
