package com.example.mypolicy.model;

import java.io.Serializable;
import java.util.Date;

public class Referral implements Serializable {
    private long p_code;
    private String title;
    private String uri;
    private String dor;
    private String si;

    private Date apply_start;
    private Date apply_end;

    private double cg_priority;
    private double ml_priority;
    private double cl_priority;

    public Referral(long p_code, String title) {
        this.p_code = p_code;
        this.title = title;
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

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
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

    public double getCg_priority() {
        return cg_priority;
    }

    public void setCg_priority(double cg_priority) {
        this.cg_priority = cg_priority;
    }

    public double getMl_priority() {
        return ml_priority;
    }

    public void setMl_priority(double ml_priority) {
        this.ml_priority = ml_priority;
    }

    public double getCl_priority() {
        return cl_priority;
    }

    public void setCl_priority(double cl_priority) {
        this.cl_priority = cl_priority;
    }
}
