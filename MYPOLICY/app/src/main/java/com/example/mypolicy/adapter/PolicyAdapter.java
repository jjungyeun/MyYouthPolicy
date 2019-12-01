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
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyViewHolder> {
    //    dataParser=new DataParser()
    String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};


    public ArrayList<Policy> pList;
    SharedPreferences sharedPreferences;



    final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final HashMap<String,Object> clickHashMap=new HashMap<>();
    Call<JSONObject> clickPolicyCall=iApiService.clickPolicy(clickHashMap);

    public PolicyAdapter(ArrayList<Policy> list) {
        pList = list;
    }
    @NonNull
    @Override
    public PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        return new PolicyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyViewHolder holder, final int position) {
        sharedPreferences=holder.itemView.getContext().getSharedPreferences("session",Context.MODE_PRIVATE);

//        String format=(pList.get(position).getApply_start());
        Log.d("ddddddddddddddddddddd",""+pList.get(position).getP_code());
        final long pcode=pList.get(position).getP_code();
        Log.d("d",""+pList.get(position).getApply_end());
        holder.policyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("위치확인 시작",""+pList.get(position).getApply_start());
                Log.d("위치확인 끝",""+pList.get(position).getApply_end());

            }
        });
        holder.policyName.setText(pList.get(position).getTitle());

        String apply_start=""; String apply_end="";
        if(pList.get(position).getApply_start()==null &&pList.get(position).getApply_end()==null) {
            holder.applyStart.setText("공고 확인 후 신청 바람");
            holder.applyEnd.setText("공고 확인 후 신청 바람");
        }
        else if(pList.get(position).getApply_start()==null&&pList.get(position).getApply_end()!=null) {
            holder.applyStart.setText("공고 확인 후 신청 바람");
            apply_end=pList.get(position).getApply_end().toString();
            apply_end=date_parse(apply_end);

            holder.applyEnd.setText(apply_end);
        }

        else if(pList.get(position).getApply_start()!=null&&pList.get(position).getApply_end()==null) {
            apply_start=pList.get(position).getApply_start().toString();
            apply_start=date_parse(apply_start);
            Log.d("날짜",""+apply_start);

            holder.applyStart.setText(apply_start);
            holder.applyEnd.setText("공고 확인 후 신청 바람");
        }

        else
        {
            apply_start=pList.get(position).getApply_start().toString();
            apply_end=pList.get(position).getApply_end().toString();
            apply_start=date_parse(apply_start);
            apply_end=date_parse(apply_end);

            holder.applyStart.setText(apply_start);
            holder.applyEnd.setText(apply_end);
        }

        holder.policyName.setText(pList.get(position).getTitle());
///////*****************************정책 이름이 눌렸을때 detail로 이동********************************************************************////
        holder.policyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHashMap.put("uID",sharedPreferences.getString("userEmail",null));
                clickHashMap.put("p_code",pcode);
                Log.d("해쉬","폴리시"+sharedPreferences.getString("userEmail",null)+" "+pcode);

                clickPolicyCall.clone().enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Log.d("디테일 클릭 조회",""+new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
//디테일 부분으로 이동/
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);
                intent.putExtra("position",pcode);
                Log.d("주소주소",""+pcode);
                context.startActivity(intent);
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////*****************************holder 자체를 눌렸을때 detail로 이동********************************************************************////

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*******************************통신으로 클릭수 보내주고 디테일 부분으로 이동*///////////////////////////////////////

                clickHashMap.put("uID",sharedPreferences.getString("userEmail",null));
                clickHashMap.put("p_code",pcode);
                Log.d("해쉬","폴리시"+sharedPreferences.getString("userEmail",null)+" "+pcode);

                clickPolicyCall.clone().enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Log.d("디테일 클릭 조회",""+new Gson().toJson(response.body()));
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
////디테일 부분으로 이동////
                Context context=view.getContext();
                Intent intent=new Intent(context, DetailPolicyActivity.class);
                intent.putExtra("position",pcode);
                Log.d("주소주소",""+pcode);
                context.startActivity(intent);

            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private String date_parse(String rawDate) {
        StringBuilder sb=new StringBuilder();
        Log.d("원본","data"+rawDate);

        String [] words=rawDate.split("\\s");
        for(int i=0;i<words.length;i++){
            Log.d("원본","split"+i+words[i]);
        }
//*********************************년도****************************************//
        String year=words[5];
       // Log.d("원본","년도"+year);
        sb.append(year); sb.append("년 ");

        //*********************************월****************************************//
        String month=words[1];
        for(int i=0;i<eng_mon.length;i++)
        {
            if(month.equals(eng_mon[i]))
            {
                sb.append(kor_mon[i]);
                sb.append(" ");
                break;
            }
        }

        //*********************************일****************************************//
        String day=words[2];

        sb.append(day);sb.append("일");
        sb.append(" ");


        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return pList.size();
    }
}
