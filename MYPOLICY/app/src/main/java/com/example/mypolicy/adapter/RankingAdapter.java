package com.example.mypolicy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.RankingData;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder>{

    final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final HashMap<String,Object> clickHashMap=new HashMap<>();
    Call<JSONObject> clickPolicyCall=iApiService.clickPolicy(clickHashMap);
    SharedPreferences sharedPreferences;

    public ArrayList<RankingData> rList;

    public RankingAdapter(ArrayList<RankingData> list) {
        rList = list;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_row, parent, false);
        return new RankingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        sharedPreferences=holder.itemView.getContext().getSharedPreferences("session",Context.MODE_PRIVATE);
       ;
        holder.wee.setText(Integer.toString(holder.getAdapterPosition()+1));
        holder.title.setText(rList.get(position).getTitle());
        final long pcode=rList.get(position).getP_code();
        
        holder.views.setText(Integer.toString(rList.get(position).getViews()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*******************************통신으로 클릭수 보내주고 디테일 부분으로 이동*///////////////////////////////////////
                clickHashMap.put("uID",sharedPreferences.getString("userEmail",null));
                clickHashMap.put("p_code",pcode);
                Log.d("해쉬","랭킹"+sharedPreferences.getString("userEmail",null)+" "+pcode);

                clickPolicyCall.clone().enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Log.d("디테일 클릭 조회",""+new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
                ///////////////////////////////////디테일 부분으로 이동////////////////////////////////////////////
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);

                intent.putExtra("position",pcode);
                context.startActivity(intent);
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*******************************통신으로 클릭수 보내주고 디테일 부분으로 이동*///////////////////////////////////////
                clickHashMap.put("uID",sharedPreferences.getString("userEmail",null));
                clickHashMap.put("p_code",pcode);

                clickPolicyCall.clone().enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Log.d("디테일 클릭 조회",""+new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
                ///////////////////////////////////디테일 부분으로 이동////////////////////////////////////////////
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);

                intent.putExtra("position",pcode);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rList.size();
    }
}
