package com.example.mypolicy.service;

import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.RankingData;
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
    @GET("policy/selected_policies")//전체 공공정책 리스트
    Call<ArrayList<Policy>> showAllPolicies();

    @GET("policy/{number}")//세부 공공정책 사항
    Call<ArrayList<Policy>>showselectedPolicy(@Path("number") long number);

    @GET("review/{number}")//정책당 사람들 리뷰
    Call<ArrayList<Review>> showReview(@Path("number") long number);

    /**************************필터링 전 상시,현재 ,공고전,공고후****************************/
    @GET("policy/after_apply_policies")//전체 공공정책 리스트 공고 종료후
    Call<ArrayList<Policy>> showAllPolicies_afterApply();

    @GET("policy/before_apply_policies")//전체 공공정책 리스트 공고 시작전
    Call<ArrayList<Policy>> showAllPolicies_beforeApply();

    @GET("policy/possible_apply_policies")//전체 공공정책 리스트 현재 신청 가능
    Call<ArrayList<Policy>> showAllPolicies_nowPossible();

    @GET("policy/always_apply_policies")//전체 공공정책 리스트 상시
    Call<ArrayList<Policy>> showAllPolicies_always();


    /************************************************************************************/


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

    //조회수 별 일일 Top20
    @GET("sorting/day_views")
    Call<ArrayList<RankingData>> sortDayViews();

    //조회수 별 주별 Top20
    @GET("sorting/week_views")
    Call<ArrayList<RankingData>> sortWeekViews();

    //조회수 별 달별 Top20
    @GET("sorting/month_views")
    Call<ArrayList<RankingData>> sortMonthViews();




    /*****===========정책당 클릭스 따로 구현==============*/
    //경우의수 1. search에서 각각 누르면 디테일로 들어갈때(해결)
    //        2. MyList에서 각각 누르면 디테일로 들어갈때(해결)
    //        3.  Top20에서 각각 누르면 디테일로 들어갈때(해결)
    //        4. 키워드에서 디테일로 넘어갈때(해결)
    @FormUrlEncoded
    @POST("policy/click")
    Call<JSONObject> clickPolicy(@FieldMap HashMap<String,Object> parameters);

}
