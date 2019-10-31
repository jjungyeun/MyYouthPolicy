package com.example.mypolicy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypolicy.adapter.PolicyAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPolicyActivity extends AppCompatActivity {
    Intent intent;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_policy);
        intent=getIntent();
        position=intent.getIntExtra("position",1);
        Log.d("값",""+intent.getIntExtra("position",1));
        IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        Call<ArrayList<Policy>> call=iApiService.showselectedPolicy(position);

        try{
            call.enqueue(new Callback<ArrayList<Policy>>() {
                @Override
                public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                    String tmp=new Gson().toJson(response.body());
                    Log.d("각각정보",""+position+tmp);
                    setUI();
                }

                private void setUI() {

                }

                @Override
                public void onFailure(Call<ArrayList<Policy>> call, Throwable t) {

                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
