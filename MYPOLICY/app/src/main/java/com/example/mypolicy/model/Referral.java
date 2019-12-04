package com.example.mypolicy.model;

import java.io.Serializable;
import java.util.Date;

public class Referral implements Serializable {
    private long p_code;
    private String title;
    private String uri;
    private Date apply_start;
    private Date apply_end;

    public Referral(long p_code, String title, String uri) {
        this.p_code = p_code;
        this.title = title;
        this.uri = uri;

    }

    public Referral(long p_code, String title, String uri, Date apply_start, Date apply_end) {
        this.p_code = p_code;
        this.title = title;
        this.uri = uri;
        this.apply_start = apply_start;
        this.apply_end = apply_end;
    }

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
