package com.example.mypolicy.model;

import java.util.Date;

public class RankingData {
    private long p_code;
    private String title;
    private Date apply_start;
    private Date apply_ned;
    private String category;
    private String region;
    private int views;

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

    public Date getApply_ned() {
        return apply_ned;
    }

    public void setApply_ned(Date apply_ned) {
        this.apply_ned = apply_ned;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
