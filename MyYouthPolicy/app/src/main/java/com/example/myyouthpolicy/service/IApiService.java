package com.example.myyouthpolicy.service;

import com.example.myyouthpolicy.model.Policy;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {
    @GET("policy/show_all_policies")
    Call<ArrayList<Policy>> showAllPolicies();

}
