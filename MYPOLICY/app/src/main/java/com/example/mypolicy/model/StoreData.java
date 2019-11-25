package com.example.mypolicy.model;

import java.util.Date;

public class StoreData {
    private long p_code;
    private String title;
    private Date apply_start;
    private Date apply_end;

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
}
