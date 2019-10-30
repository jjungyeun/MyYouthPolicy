package com.example.firebase_20190925.service;

import com.example.firebase_20190925.model.Policy;
import com.example.firebase_20190925.model.Realdata;
import com.example.firebase_20190925.model.UserModel;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {
    @GET("policy/show_all_policies")
    Call<ArrayList<Policy>> showAllPolicies();

    @GET("policy/show_all_policies")
    Call<ResponseBody> showAllPolicies2();
}
