package com.example.mypolicy.service;

import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.Review;
import com.example.mypolicy.model.SearchData;
import com.example.mypolicy.model.StoreData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
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
    @POST("review/selected_review")
    Call<ArrayList<Review>> postShowReview(@FieldMap HashMap<String,Object> parameters);


    @FormUrlEncoded
    @POST("review/write_review")
    Call<JSONObject> postReview(@FieldMap HashMap<String,Object> parameters);


    @FormUrlEncoded
    @POST("review/delete_review")
    Call<JSONObject> deleteReview(@FieldMap HashMap<String,Object> parameters);

    //저장하는 부분
    @FormUrlEncoded
    @POST("my_list/store_in_mylist")
    Call<JSONObject> storeinMyList(@FieldMap HashMap<String,Object> parameters);

    //저장한걸 전부 보는 부분
    @FormUrlEncoded
    @POST("my_list/show_all_mylist")
    Call<ArrayList<StoreData>> showallMyList(@FieldMap HashMap<String,Object> parameters);

    //저장한걸 지우는 부분
    @FormUrlEncoded
    @POST("my_list/delete")
    Call<JSONObject> deleteMyList(@FieldMap HashMap<String,Object> parameters);

    //시간순으로 저장한걸 보는 부분
    @FormUrlEncoded
    @POST("my_list/ordered_mylist")
    Call<JSONObject> orderedMyList(@FieldMap HashMap<String,Object> parameters);

    //search에서 검색조건
    @FormUrlEncoded
    @POST("search/test")
    Call<ArrayList<SearchData>> postSearchKeyword(@Field("location") ArrayList<String> location, @Field("category") String category
    , @Field("age")int age, @Field("keyword") String keyword);

}
