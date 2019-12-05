package com.example.mypolicy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.SearchActivity;
import com.example.mypolicy.R;
import com.example.mypolicy.model.Review;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final HashMap<String,Object> deleteDataMap=new HashMap<>();
    final Call<JSONObject> deleteCall=iApiService.deleteReview(deleteDataMap);

    String parseTime="";
    SharedPreferences sharedPreferences;

    public ArrayList<Review> rList;
    public ReviewAdapter(ArrayList<Review> list) {
        rList = list;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new ReviewViewHolder(v);
    }
    ////////////////////////////댓글 한줄에해당하는 정보들 코딩하는 부분///////////////////////////////
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        sharedPreferences=holder.itemView.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        String pattern = "yyyy/MM/dd HH시 mm분";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        parseTime=simpleDateFormat.format(rList.get(position).getReview_time());

        //sharedPreferences.getString("userEmail",null)//아이디 꺼내는 함수
        //구현할부분
        //rList.get(position).getReview_uID와 shared.......가 같다면 xml에 버튼이 visible하게 셋팅팅
        String userID = sharedPreferences.getString("userEmail",null);
        String commentID = rList.get(position).getReview_uID();
        final long review_code=rList.get(position).getReview_code();
        if(userID.equals(commentID)){
            Log.d("ff","아이디같음??");
            holder.delete_btn.setVisibility(View.VISIBLE);
        }
        else {
            holder.delete_btn.setVisibility(View.GONE);
        }
        //holder.id.setText(plus);// 통신에서 받아온 아이디
        holder.id.setText(rList.get(position).getReview_uID());// 통신에서 받아온 아이디
        holder.comment.setText(rList.get(position).getContents());// 통신에서 받아온 코멘트

        holder.time.setText(parseTime);// 통신에서 받아온 시간 ->파싱

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Context context=view.getContext();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("댓글을 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
//
                                    deleteDataMap.put("review_uID",sharedPreferences.getString("userEmail",null));
                                    deleteDataMap.put("review_code",review_code);
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

                                Toasty.error(context, "댓글삭제완료!!", Toast.LENGTH_SHORT, true).show();                Context context=view.getContext();
                                //Intent intent=new Intent(context, SearchActivity.class);
                                //context.startActivity(intent);

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
        return rList.size();
    }
}