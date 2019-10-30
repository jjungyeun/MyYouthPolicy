package com.example.mypolicy.service;

import com.example.mypolicy.model.Policy;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IApiService {
    @GET("policy/show_all_policies")
    Call<ArrayList<Policy>> showAllPolicies();
}
