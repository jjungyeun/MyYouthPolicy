package com.example.mypolicy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.adapter.PolicyAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private String mClassName = getClass().getName().trim();
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        Call<ArrayList<Policy>> call=iApiService.showAllPolicies();

        try{
            call.enqueue(new Callback<ArrayList<Policy>>() {
                @Override
                public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                    try{
                        PolicyAdapter pa=new PolicyAdapter(response.body());
                        mRecyclerView.setAdapter(pa);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Policy>> call, Throwable t) {
                        t.printStackTrace();
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
