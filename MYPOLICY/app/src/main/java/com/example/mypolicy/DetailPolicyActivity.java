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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
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
    private TextView tv_dor;
    private TextView tv_si;
    private TextView tv_uri;
    private TextView tv_time;
    private EditText et_comment;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private TextView tv_review_blank;
    InputMethodManager imm;

    private int startflag=0;
    private int endflag=0;

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    private RecyclerView mRecyclerView;
    private Button reviewInsert;
    private int reviewLength=0;
    private String commentData;
    private String reviewLengthString;
    private Button policySaveButton;
    private String apply_Start_String="";
    private String apply_End_String="";
    private Button btn_refresh;

    final HashMap<String,Object> postReviewhashMap=new HashMap<>();//댓글을 보내는
    final HashMap<String,Object> getReviewhashMap=new HashMap<>();//댓글을 보는
    final HashMap<String,Object> postSavehashMap=new HashMap<>();
    SharedPreferences sharedPreferences;
    long now;
    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();

    Intent intent;
    long position;

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat stringToDateFormat=new SimpleDateFormat("yyyyMMdd");

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
        tv_dor=findViewById(R.id.tv_location_dor);
        tv_si=findViewById(R.id.tv_location_si);
        tv_uri=findViewById(R.id.tv_uri);
        tv_time=findViewById(R.id.tv_time);
        policySaveButton=findViewById(R.id.btn_policy_save);
        reviewInsert=findViewById(R.id.btn_review_insert);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        btn_refresh = findViewById(R.id.btn_refresh);
        ReviewAdapter ra;

        final String[] eng_mon={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        final String[] kor_mon={"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
        String[] eng_day={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        String[] kor_day={"월요일","화요일","수요일","목요일","금요일","토요일","일요일"};
        final StringBuilder startSB=new StringBuilder();
        final StringBuilder endSB=new StringBuilder();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        addSideView();  //사이드바 add

        intent=getIntent();
        position=intent.getLongExtra("position",1);
        Log.d("피코드","꺼낸"+position);
//================================시간할것=================================================//




        final Call<ArrayList<Policy>> call=iApiService.showselectedPolicy(position);
        final Call<ArrayList<Review>> reviewCall=iApiService.postShowReview(getReviewhashMap);
        final Call<JSONObject> postSaveCall=iApiService.storeinMyList(postSavehashMap);


        //각각 에 대한 상세정보 받는부분

        try{
            call.enqueue(new Callback<ArrayList<Policy>>() {
                @Override
                public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                    String tmp=new Gson().toJson(response.body());

                    Log.d("각각정보22",""+position+tmp);
                    //파싱코드
                    try{
                        JSONArray jsonArray=new JSONArray(tmp);
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String title=jsonObject.getString("title");
                        tv_title.setText(title);

                        if(jsonObject.has("contents"))
                        {
                            String contents=jsonObject.getString("contents");
                            tv_detail.setText(contents);
                        }
                        else
                        {
                            tv_detail.setText("-");
                        }


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

                        String dor=jsonObject.getString("dor");
                        tv_dor.setText(dor);

                        String si=jsonObject.getString("si");
                        tv_si.setText(si);


                        /* ===========jsonOBejct에서 apply_start 키 값이 있는지 체크================  */
                        if(jsonObject.has("apply_start")) {
                            apply_Start_String=jsonObject.getString("apply_start");
                            String [] splited=apply_Start_String.split("\\s");
                            startSB.append(splited[2]+"년 ");
                            Log.d("시작 플래그","  리플레이스 전"+splited[1]);
                            splited[1]=splited[1].replace(",","");
                            Log.d("시작 플래그","  리플레이스 후"+splited[1]);

                            for(int i=0;i<eng_mon.length;i++)
                            {
                                if(splited[0].equals(eng_mon[i]))
                                {
                                    startSB.append(kor_mon[i]);
                                }
                            }
                            startSB.append(" ");
                            startSB.append(splited[1]+"일");

                            tv_applyStart.setText(startSB.toString());
                        }
                        else {
                            tv_applyStart.setText("공고후 확인 신청 바람");
                            Log.d("시작 플래그", "" + "시작 없음");
                        }

                        /* ===========jsonOBejct에서 apply_end 키 값이 있는지 체크================  */
                        if(jsonObject.has("apply_end")) {
                            apply_End_String=jsonObject.getString("apply_end");
                            String [] splited2=apply_End_String.split("\\s");
                            endSB.append(splited2[2]+"년 ");
                            splited2[1]=splited2[1].replace(",","");

                            for(int i=0;i<eng_mon.length;i++)
                            {
                                if(splited2[0].equals(eng_mon[i]))
                                {
                                    endSB.append(kor_mon[i]);
                                }
                            }
                            endSB.append(" ");
                            endSB.append(splited2[1]+"일");

                            tv_applyEnd.setText(endSB.toString());
                        }
                        else {
                            tv_applyEnd.setText("공고후 확인 신청 바람");
                            Log.d("끝 플래그", "" + "끝없음");
                        }


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

        //댓글 정보 가져 오는 코드
        try{
            getReviewhashMap.put("p_code",position);
            reviewCall.enqueue(new Callback<ArrayList<Review>>() {
                @Override
                public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                    String tmp=new Gson().toJson(response.body());
                    Log.d("오는정보",""+tmp);
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
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //댓글을 작성하고 버튼을 누르면 서버로 댓글 정보 전송
        reviewInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now=System.currentTimeMillis();
                Date date=new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일");
                final String formatDate = sdfNow.format(date);
                Log.d("현재시간",""+formatDate);

                postReviewhashMap.put("review_uID",sharedPreferences.getString("userEmail",null));
                postReviewhashMap.put("p_code",position);
                commentData=et_comment.getText().toString();
                postReviewhashMap.put("contents",commentData);



                //댓글을 서버로 전송하는 코드
                Call<JSONObject> postReview=iApiService.postReview(postReviewhashMap);
                try{
                    postReview.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            Toasty.info(DetailPolicyActivity.this, "댓글 작성완료!!", Toast.LENGTH_SHORT, true).show();

                            // 댓글창 비우고 키보드 내리기
                            et_comment.setText("");
                            imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);


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
        //댓글창 새로고침 하는 부분 - 새로고침 버튼 누르면//

        btn_refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("댓글피코드", "ㄹㄹㄹ" + position);
                reviewCall.clone().enqueue(new Callback<ArrayList<Review>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                        String tmp = new Gson().toJson(response.body());

                        try{
                            JSONArray jsonArray=new JSONArray(tmp);
                            Log.d(" 메시지 길이",""+jsonArray.length());
                            tv_review_blank.setText(Integer.toString(jsonArray.length()));

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        ReviewAdapter ra = new ReviewAdapter(response.body());
                        mRecyclerView.setAdapter(ra);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

                    }
                });

            }
        });




        //댓글창 새로고침 하는 부분 - 새로고침 버튼 누르면//



        //*****************************저장하는 기능*************************************//
        policySaveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("저장 피코드 로그",""+position);
                postSavehashMap.put("uID",sharedPreferences.getString("userEmail",null));
                postSavehashMap.put("s_p_code",position);

                try {
                    postSaveCall.clone().enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            Log.d("저장","결과"+new Gson().toJson(response.body()));
                            Toasty.info(DetailPolicyActivity.this, "정책 저장 완료!!", Toast.LENGTH_SHORT, true).show();
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {

                        }
                    });
                }
                catch (Exception e)
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
            @Override
            public void btnTop() {

                Intent intent = new Intent(mContext, RankingActivity.class);
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
