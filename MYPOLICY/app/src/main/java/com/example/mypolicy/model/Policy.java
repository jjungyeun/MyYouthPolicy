package com.example.mypolicy.model;

import android.widget.EditText;

import java.util.Date;

public class Policy {
    int p_code;
    String title;
    String uri;
    Date apply_start;
    Date apply_end;
    int start_age;
    int end_age;
    String contents;
    String application_target;
    String location;
    Date crawing_date;
    int expiration_flag;

    public int getP_code() {
        return p_code;
    }

    public void setP_code(int p_code) {
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

    public int getStart_age() {
        return start_age;
    }

    public void setStart_age(int start_age) {
        this.start_age = start_age;
    }

    public int getEnd_age() {
        return end_age;
    }

    public void setEnd_age(int end_age) {
        this.end_age = end_age;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getApplication_target() {
        return application_target;
    }

    public void setApplication_target(String application_target) {
        this.application_target = application_target;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCrawing_date() {
        return crawing_date;
    }

    public void setCrawing_date(Date crawing_date) {
        this.crawing_date = crawing_date;
    }

    public int getExpiration_flag() {
        return expiration_flag;
    }

    public void setExpiration_flag(int expiration_flag) {
        this.expiration_flag = expiration_flag;
    }
}