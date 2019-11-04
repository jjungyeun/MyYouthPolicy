package com.example.mypolicy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.adapter.PolicyAdapter;
import com.example.mypolicy.adapter.ReviewAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.Review;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPolicyActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "DetailPolicyActivity";

    private Context mContext = DetailPolicyActivity.this;
    /*상세정보*/
    private TextView tv_title;
    private TextView tv_detail;
    private TextView tv_applyStart;
    private TextView tv_applyEnd;
    private TextView tv_age_start;
    private TextView tv_age_end;
    private TextView tv_location;
    private TextView tv_uri;
    private TextView tv_time;
    private EditText et_comment;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private TextView tv_review_blank;

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    private RecyclerView mRecyclerView;
    private Button reviewInsert;
    private int reviewLength=0;
    private String commentData;
    private String reviewLengthString;
    final HashMap<String,Object> hashMap=new HashMap<>();
    SharedPreferences sharedPreferences;
    long now;
    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();

    Intent intent;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_policy);
        init();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        mRecyclerView=findViewById(R.id.recyclerView);
        tv_review_blank=findViewById(R.id.tv_review_blank);

        et_comment=findViewById(R.id.et_comment);
        tv_age_start=findViewById(R.id.tv_age_start);
        tv_age_end=findViewById(R.id.tv_age_end);
        tv_title=findViewById(R.id.tv_policy_name);
        tv_applyStart=findViewById(R.id.tv_apply_start);
        tv_applyEnd=findViewById(R.id.tv_apply_end);
        tv_detail=findViewById(R.id.tv_policy_detail);
        tv_location=findViewById(R.id.tv_location);
        tv_uri=findViewById(R.id.tv_uri);
        tv_time=findViewById(R.id.tv_time);
        reviewInsert=findViewById(R.id.btn_review_insert);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        addSideView();  //사이드바 add

        intent=getIntent();
        position=intent.getIntExtra("position",1);
//================================시간할것=================================================//
//        now=System.currentTimeMillis();
//        Date date=new Date(now);
//        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        String formatDate = sdfNow.format(date);
//        Log.d("현재시간",""+formatDate);
//        tv_time.setText(formatDate);


        Call<ArrayList<Policy>> call=iApiService.showselectedPolicy(position);
        Call<ArrayList<Review>> reviewcall=iApiService.showReview(position);

        try{
            call.enqueue(new Callback<ArrayList<Policy>>() {
                @Override
                public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                    String tmp=new Gson().toJson(response.body());

                    Log.d("각각정보",""+position+tmp);
                    //파싱코드
                    try{
                        JSONArray jsonArray=new JSONArray(tmp);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String title=jsonObject.getString("title");
                        tv_title.setText(title);

                        String contents=jsonObject.getString("contents");
                        tv_detail.setText(contents);

                        String age=Integer.toString(jsonObject.getInt("start_age"));
                        if(age.equals("0"))
                        {
                            tv_age_start.setText("제한없음");
                        }
                        else
                        tv_age_start.setText(age+"살");

                        String age_end=Integer.toString(jsonObject.getInt("end_age"));
                        if(age_end.equals("0"))
                        {
                            tv_age_end.setText("제한없음");
                        }
                        else
                        tv_age_end.setText(age_end+"살");

                        String url=jsonObject.getString("uri");
                        tv_uri.setText(url);

                        if(jsonObject.length()==8)//지원날짜 끝날짜 없을떄
                        {
                            tv_applyStart.setText("지원날짜 상관없음");
                            tv_applyEnd.setText("지원날짜 상관없음");
                        }
                        else if(jsonObject.length()==9)
                        {
                            Log.d("각각하나",""+jsonObject.getString("apply_start"));
                            Log.d("각각하나",""+jsonObject.getString("apply_end"));
                        }
                        else
                        {

                        }


//                        String applyStart=jsonObject.getString("apply_start");
//                        if( applyStart==null)
//                        {
//                            tv_applyStart.setText("기한제한 없음");
//                        }





                        //Log.d("각각정보세부",""+jsonObject);

                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Policy>> call, Throwable t) {

                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        try{// 댓글 통신 retrofit
            reviewcall.enqueue(new Callback<ArrayList<Review>>() {
                @Override
                public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                    String tmp=new Gson().toJson(response.body());
                    try {//정보길이
                        JSONArray jsonArray = new JSONArray(tmp);
                        reviewLength=jsonArray.length();
                        Log.d("길이",""+reviewLength);
                        reviewLengthString=Integer.toString(reviewLength);
                        Log.d("길이2",""+reviewLengthString);
                        tv_review_blank.setText(reviewLengthString);

                    }catch (JSONException j)
                    {
                        j.printStackTrace();
                    }
                    ReviewAdapter ra=new ReviewAdapter(response.body());
                    mRecyclerView.setAdapter(ra);

                    Log.d("리뷰값",""+position+tmp);
                }

                @Override
                public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

                }
            });
        }catch (Exception t)
        {
            t.printStackTrace();
        }

        reviewInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("review_uID",sharedPreferences.getString("userEmail",null));
                hashMap.put("p_code",position);
                commentData=et_comment.getText().toString();
                hashMap.put("contents",commentData);



//        hashMap.put("review_uID","iwls1234@naver.com");
//        hashMap.put("p_code",0);
//        hashMap.put("contents","성공했어");
                Call<JSONObject> postReview=iApiService.postReview(hashMap);
                try{
                    postReview.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            Log.d("성공",""+response.body());
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {

                        }
                    });
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if(isMenuShow){
            closeMenu();
        }else{
            finish();
        }
    }

    private void init(){

        findViewById(R.id.btn_menu).setOnClickListener(this);

        mainLayout = findViewById(R.id.id_main);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);

    }

    private void addSideView(){

        SideBarView sidebar = new SideBarView(mContext);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sidebar.setEventListener(new SideBarView.EventListener() {

            @Override
            public void btnCancel() {
                Log.e(TAG, "btnCancel");
                closeMenu();
            }

            @Override
            public void btnHome() {
                Log.e(TAG, "btnHome");
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                closeMenu();
                finish();
            }

            @Override
            public void btnSearch() {
                Intent intent = new Intent(mContext,SearchActivity.class);
                startActivity(intent);
                closeMenu();
                finish();
            }

            @Override
            public void btnDownload() {
                Intent intent = new Intent(mContext,DownloadActivity.class);
                startActivity(intent);
                closeMenu();
                finish();
            }

            @Override
            public void btnProfile() {
                Intent intent = new Intent(mContext,ProfileActivity.class);
                startActivity(intent);
                closeMenu();
                finish();
            }

            @Override
            public void btnLogout() {
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                closeMenu();
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_menu :

                showMenu();
                break;
        }
    }

    public void closeMenu(){

        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(mContext, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLayout.setVisibility(View.GONE);
                viewLayout.setEnabled(false);
                mainLayout.setEnabled(true);
            }
        }, 450);
    }

    public void showMenu(){

        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
        Log.e(TAG, "메뉴버튼 클릭");
    }
}
