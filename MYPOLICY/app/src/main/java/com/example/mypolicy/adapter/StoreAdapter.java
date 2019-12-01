package com.example.mypolicy.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.DetailPolicyActivity;
import com.example.mypolicy.DownloadActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.StoreData;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder>{

    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    SharedPreferences sharedPreferences;
    final HashMap<String,Object> deleteDataMap=new HashMap<>();
    final Call<JSONObject> deleteCall=iApiService.deleteMyList(deleteDataMap);



    public ArrayList<StoreData> sList;


    final HashMap<String,Object> clickHashMap=new HashMap<>();
    Call<JSONObject> clickPolicyCall=iApiService.clickPolicy(clickHashMap);


    SimpleDateFormat sdf=new SimpleDateFormat("yyyy년 MM월 dd일");
    public StoreAdapter(ArrayList<StoreData> list) {
        sList=list;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreViewHolder holder, final int position) {
        sharedPreferences=holder.itemView.getContext().getSharedPreferences("session",Context.MODE_PRIVATE);

        holder.tv_title.setText(sList.get(position).getTitle());
        Log.d("저장위치","위치"+sList.get(position).getP_code());
        final long pcode=sList.get(position).getP_code();
        Log.d("피코","드"+pcode);
        holder.tv_Pcode.setText(Long.toString(pcode));


        if(sList.get(position).getApply_start()==null &&sList.get(position).getApply_end()==null) {
            holder.tv_applyStart.setText("공고후 확인 신청 바람");
            holder.tv_applyEnd.setText("공고후 확인 신청 바람");

        }


        else if(sList.get(position).getApply_start()==null&&sList.get(position).getApply_end()!=null) {
            holder.tv_applyStart.setText("공고후 확인 신청 바람");
        }

        else if(sList.get(position).getApply_start()!=null&&sList.get(position).getApply_end()==null) {
            holder.tv_applyEnd.setText("공고후 확인 신청 바람");
        }

        else // 둘다 기간이 있으면
        {
            holder.tv_applyStart.setText(sdf.format(sList.get(position).getApply_start()));
            holder.tv_applyEnd.setText(sdf.format(sList.get(position).getApply_end()));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*******************************통신으로 클릭수 보내주고 디테일 부분으로 이동*///////////////////////////////////////
                clickHashMap.put("uID",sharedPreferences.getString("userEmail",null));
                clickHashMap.put("p_code",pcode);
                Log.d("해쉬","스토어"+sharedPreferences.getString("userEmail",null)+" "+pcode);
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




        holder.btn_storeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context=view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
//                    Log.d("유아이디","ㅠㅠ"+sharedPreferences.getString("userEmail",null));
                                    deleteDataMap.put("uID",sharedPreferences.getString("userEmail",null));
                                    deleteDataMap.put("s_p_code",pcode);
                                    deleteCall.enqueue(new Callback<JSONObject>() {
                                        @Override
                                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<JSONObject> call, Throwable t) {

                                        }
                                    });
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                Toasty.error(context, "삭제완료!!", Toast.LENGTH_SHORT, true).show();                Context context=view.getContext();
                                Intent intent=new Intent(context, DownloadActivity.class);
                                context.startActivity(intent);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            }
        });
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }
}
