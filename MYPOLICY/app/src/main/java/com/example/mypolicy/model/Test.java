package com.example.mypolicy.model;

import java.util.Date;

public class Test {
    private long p_code;
    private String title;
    private String uri;
    private Date apply_start;
    private Date apply_end;
    private int policy_hits;

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

    public void setApple_end(Date apple_end) {
        this.apply_end = apple_end;
    }

    public int getPolicy_hits() {
        return policy_hits;
    }

    public void setPolicy_hits(int policy_hits) {
        this.policy_hits = policy_hits;
    }
}
