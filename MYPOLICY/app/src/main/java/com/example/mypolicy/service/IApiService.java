package com.example.mypolicy.service;

import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.Review;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IApiService {
    @GET("policy/show_all_policies")//전체 공공정책 리스트
    Call<ArrayList<Policy>> showAllPolicies();

    @GET("policy/{number}")//세부 공공정책 사항
    Call<ArrayList<Policy>>showselectedPolicy(@Path("number") int number);

    @GET("review/{number}")//정책당 사람들 리뷰
    Call<ArrayList<Review>> showReview(@Path("number") int number);

    @FormUrlEncoded
    @POST("review/write_review")
    Call<JSONObject> postReview(@FieldMap HashMap<String,Object> parameters);
}
