package com.example.mypolicy.service;

import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.Review;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IApiService {
    @GET("policy/show_all_policies")
    Call<ArrayList<Policy>> showAllPolicies();

    @GET("policy/{number}")
    Call<ArrayList<Policy>>showselectedPolicy(@Path("number") int number);

    @GET("review/{number}")
    Call<ArrayList<Review>> showReview(@Path("number") int number);
}
