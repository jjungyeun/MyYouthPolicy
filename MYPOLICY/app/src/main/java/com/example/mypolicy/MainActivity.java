package com.example.mypolicy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mypolicy.model.Referral;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "MainActivity";

    private Context mContext = MainActivity.this;

    static List<Referral> referralList = new ArrayList<>();

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    TextView tv_name, tv_name2;

    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    ScrollerViewPager viewPager;
//    SpringIndicator springIndicator;
//    PagerModelManager manager;
//    ModelPagerAdapter adapter;
    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);

        tv_name = findViewById(R.id.tv_name_main);
        tv_name2 = findViewById(R.id.tv_name_main2);

        // 사용자 이름 불러오기
        DocumentReference userInfo = db.collection("user").document(sharedPreferences.getString("userEmail",null));
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        tv_name.setText(document.get("name").toString());
                        tv_name2.setText(document.get("name").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        final HashMap<String,Object> referralMap=new HashMap<>();
        referralMap.put("uID",sharedPreferences.getString("userEmail",null));
        final Call<ArrayList<Referral>> referralCall=iApiService.showReferral(referralMap);

        referralCall.clone().enqueue(new Callback<ArrayList<Referral>>() {
            @Override
            public void onResponse(Call<ArrayList<Referral>> call, Response<ArrayList<Referral>> response) {
                Log.d("여긴가",""+new Gson().toJson(response.body()));
                try{
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()).toString());
                    Date start;
                    Date end;
                    long p_code;
                    String uri;
                    String title;
                    Log.d("여긴가길이",""+jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        title=jsonObject.getString("title");

                        p_code=jsonObject.getLong("p_code");
                        uri=jsonObject.getString("uri");

                        Log.d("여긴가1",""+jsonObject.getString("apply_start"));


                        Log.d("여긴가길이",""+title + "  "+p_code+ " "+uri);
                        Referral referral=new Referral(p_code,title,uri);
                        referralList.add(referral);
                    }
                    Log.d("여긴가사이즈",""+referralList.size());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Referral>> call, Throwable t) {

            }
        });


        addSideView();  //사이드바 add

        viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment2.class, getBgRes(), getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();

        // just set viewPager
        springIndicator.setViewPager(viewPager);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);

                PagerModelManager manager = new PagerModelManager();

                manager.addCommonFragment(GuideFragment.class, getNetwork(), getTitles());
                ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
                adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);

                viewPager.setAdapter(adapter);

            }
        }, 1000);// 0.5초 정도 딜레이를 준 후 시작


//        manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
//
//        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
//
////
////
//        viewPager.setAdapter(adapter);
//        viewPager.fixScrollSpeed();
//        Log.d("여긴가",""+springIndicator);
//
//        // just set viewPager
//        springIndicator.setViewPager(viewPager);

        ;


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

    static List<String> getTitles(){
        return Lists.newArrayList("1", "2", "3", "4","5");
    }

    static List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4,R.drawable.bg1);
    }

    static List<Referral> getNetwork(){
        return Lists.newArrayList(referralList.get(0),referralList.get(1),referralList.get(2),referralList.get(3),referralList.get(4));
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//


}
