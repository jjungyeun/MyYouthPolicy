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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.adapter.PolicyAdapter;
import com.example.mypolicy.adapter.RankingAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.RankingData;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.android.gms.common.util.Hex;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "RankingActivity";

    private Context mContext = RankingActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    SharedPreferences sharedPreferences;

    private String mClassName = getClass().getName().trim();
    private RecyclerView mRecyclerView;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    Button btn_day_ranking;
    Button btn_week_ranking;
    Button btn_month_ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_20ranking);

        init();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);


        addSideView();  //사이드바 add

        btn_day_ranking=findViewById(R.id.btn_day_ranking);
        btn_week_ranking=findViewById(R.id.btn_week_ranking);
        btn_month_ranking=findViewById(R.id.btn_month_ranking);


        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        final Call<ArrayList<RankingData>> rankingdayCall=iApiService.sortDayViews();
        final Call<ArrayList<RankingData>> rankingweekCall=iApiService.sortWeekViews();
        final Call<ArrayList<RankingData>> rankingmonthCall=iApiService.sortMonthViews();

        //그래프
        final BarChart mBarChart=findViewById(R.id.barChart);

///////////////////////////****************디폴트 일일 랭킹값***************///////////////////////////////////////////
//  깃허브 URL: https://github.com/blackfizz/EazeGraph                                                          //

        mBarChart.clearChart();
        rankingdayCall.clone().enqueue(new Callback<ArrayList<RankingData>>() {
            @Override
            public void onResponse(Call<ArrayList<RankingData>> call, Response<ArrayList<RankingData>> response) {


                Log.d("랭킹데이터","week"+new Gson().toJson(response.body()));
                String rankingData=new Gson().toJson(response.body());


                Map<String,String> dayMapTitle=new HashMap<>();
                Map<String,Integer> dayMapValue=new HashMap<>();

                dayMapTitle.clear();
                dayMapValue.clear();


                try {
                    JSONArray jsonArray=new JSONArray(rankingData);
                    String mapString="";
                    int mapKeyValue=0;


                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        mapString=jsonObject.getString("title");
                        mapKeyValue=Integer.parseInt(jsonObject.get("views").toString());
                        dayMapTitle.put("title"+i,mapString);
                        dayMapValue.put("value"+i,mapKeyValue);
                    }

                    //0x뒤 두자리는 FF면 투명도1
                    int color1[] = {0xFF7AB4FF,0xFF6B84E8, 0xFF8581FF, 0xFF9D78FF, 0xFFBF7AFF };
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        Log.d("제이슨 타이틀3",""+dayMapTitle.get("title"+i)+"   "+dayMapTitle.get("title"+i));

                        mBarChart.addBar(new BarModel(Integer.toString(i+1)+"위",(float)dayMapValue.get("value"+i), color1[i]));
                        //0xFF56B7F1
                        if(i==4)break;
                    }

                    mBarChart.startAnimation();

                }catch(JSONException j)
                {
                    j.printStackTrace();
                }


                RankingAdapter ra=new RankingAdapter(response.body());
                mRecyclerView.setAdapter(ra);


            }

            @Override
            public void onFailure(Call<ArrayList<RankingData>> call, Throwable t) {

            }
        });



///////////////////////////**************** 일일 랭킹값***************///////////////////////////////////////////

        btn_day_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBarChart.clearChart();
                rankingdayCall.clone().enqueue(new Callback<ArrayList<RankingData>>() {

                    @Override
                    public void onResponse(Call<ArrayList<RankingData>> call, Response<ArrayList<RankingData>> response) {


                        Log.d("랭킹데이터","week"+new Gson().toJson(response.body()));
                        String rankingData=new Gson().toJson(response.body());


                        Map<String,String> dayMapTitle=new HashMap<>();
                        Map<String,Integer> dayMapValue=new HashMap<>();


                        dayMapTitle.clear();
                        dayMapValue.clear();


                        try {
                            JSONArray jsonArray=new JSONArray(rankingData);

                            String mapString="";
                            int mapKeyValue=0;

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                mapString=jsonObject.getString("title");
                                mapKeyValue=Integer.parseInt(jsonObject.get("views").toString());


                                dayMapTitle.put("title"+i,mapString);
                                dayMapValue.put("value"+i,mapKeyValue);

                            }

                            //맨뒤 두자리는 FF면 투명도1,FFC763
                            int color2[] = {0xFF7AB4FF,0xFF6B84E8, 0xFF8581FF, 0xFF9D78FF, 0xFFBF7AFF };
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Log.d("제이슨 타이틀3",""+dayMapTitle.get("title"+i)+"   "+dayMapTitle.get("title"+i));

                                mBarChart.addBar(new BarModel(Integer.toString(i+1)+"위",(float)dayMapValue.get("value"+i), color2[i]));


                                if(i==4)break;
                            }
                            mBarChart.startAnimation();


                        }catch(JSONException j)
                        {
                            j.printStackTrace();
                        }


                        RankingAdapter ra=new RankingAdapter(response.body());
                        mRecyclerView.setAdapter(ra);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<RankingData>> call, Throwable t) {

                    }
                });
            }
        });


///////////////////////////****************주 랭킹값***************///////////////////////////////////////////

        btn_week_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBarChart.clearChart();

                rankingweekCall.clone().enqueue(new Callback<ArrayList<RankingData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RankingData>> call, Response<ArrayList<RankingData>> response) {


                        Log.d("랭킹데이터","week"+new Gson().toJson(response.body()));
                        String rankingData=new Gson().toJson(response.body());


                        Map<String,String> weekMapTitle=new HashMap<>();
                        Map<String,Integer> weekMapValue=new HashMap<>();


                        weekMapTitle.clear();
                        weekMapValue.clear();


                        try {
                            JSONArray jsonArray=new JSONArray(rankingData);
                            String mapString="";
                            int mapKeyValue=0;


                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                mapString=jsonObject.getString("title");
                                mapKeyValue=Integer.parseInt(jsonObject.get("views").toString());


                                weekMapTitle.put("title"+i,mapString);
                                weekMapValue.put("value"+i,mapKeyValue);


                            }
                            int color3[] = {0xFFFFC7FF,0xFFFFC7DD, 0xFFFFC7BB, 0xFFFFC799, 0xFFFFC777 };
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Log.d("제이슨 타이틀3",""+weekMapTitle.get("title"+i)+"   "+weekMapTitle.get("title"+i));

                                mBarChart.addBar(new BarModel(Integer.toString(i+1)+"위",(float)weekMapValue.get("value"+i), color3[i]));

//                                mBarChart.addBar(new BarModel("야",2.7f, 0xFF56B7F1));
//                                mBarChart.addBar(new BarModel("야",2.f,  0xFF343456));
//                                mBarChart.addBar(new BarModel("야",0.4f, 0xFF1FF4AC));
//                                mBarChart.addBar(new BarModel("야",4.f,  0xFF1BA4E6));
                                if(i==4)break;

                            }
                            mBarChart.startAnimation();


                        }catch(JSONException j)
                        {
                            j.printStackTrace();
                        }


                        RankingAdapter ra=new RankingAdapter(response.body());
                        mRecyclerView.setAdapter(ra);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<RankingData>> call, Throwable t) {

                    }
                });
            }
        });




///////////////////////////****************월 랭킹값***************///////////////////////////////////////////

        btn_month_ranking.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mBarChart.clearChart();
                rankingmonthCall.clone().enqueue(new Callback<ArrayList<RankingData>>() {

                    @Override
                    public void onResponse(Call<ArrayList<RankingData>> call, Response<ArrayList<RankingData>> response) {

                        Log.d("랭킹데이터","week"+new Gson().toJson(response.body()));
                        String rankingData=new Gson().toJson(response.body());


                        Map<String,String> monthMapTitle=new HashMap<>();
                        Map<String,Integer> monthMapValue=new HashMap<>();


                        monthMapTitle.clear();
                        monthMapValue.clear();

                        try {

                            JSONArray jsonArray=new JSONArray(rankingData);

                            String mapString="";
                            int mapKeyValue=0;

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                mapString=jsonObject.getString("title");
                                mapKeyValue=Integer.parseInt(jsonObject.get("views").toString());


                                monthMapTitle.put("title"+i,mapString);
                                monthMapValue.put("value"+i,mapKeyValue);

                            }

                            int color4[] = {0xFF75CCCC, 0xFF75CCAA, 0xFF75CC77, 0xFF75CC44, 0xFF759911 };
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                Log.d("제이슨 타이틀3",""+monthMapTitle.get("title"+i)+"   "+monthMapTitle.get("title"+i));

                                mBarChart.addBar(new BarModel(Integer.toString(i+1)+"위",(float)monthMapValue.get("value"+i), color4[i]));
//                                에메랄드0xFF63CBB0
//                                mBarChart.addBar(new BarModel("야",2.7f, 0xFF56B7F1));
//                                mBarChart.addBar(new BarModel("야",2.f,  0xFF343456));
//                                mBarChart.addBar(new BarModel("야",0.4f, 0xFF1FF4AC));
//                                mBarChart.addBar(new BarModel("야",4.f,  0xFF1BA4E6));
                                if(i==4)break;
                            }
                            mBarChart.startAnimation();


                        }catch(JSONException j)
                        {
                            j.printStackTrace();
                        }


                        RankingAdapter ra=new RankingAdapter(response.body());
                        mRecyclerView.setAdapter(ra);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<RankingData>> call, Throwable t) {

                    }
                });
            }
        });
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

            if(isExitFlag){
                finish();
            } else {

                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.",  Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
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

    public void setBar(){

    }
}
