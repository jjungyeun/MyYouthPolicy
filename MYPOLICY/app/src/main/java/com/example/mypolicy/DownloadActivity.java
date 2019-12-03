package com.example.mypolicy;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.adapter.StoreAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.StoreData;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    saveDialog sd;
    homeDialog hd;
    private String TAG = "DownloadActivity";
    private Context mContext = DownloadActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    private Button btn_home_nav;
    private RecyclerView mRecyclerView;
    private String dateValue="";
    ImageView hdImage;
    Spinner sp_sort;


    IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
    final HashMap<String,Object> showStoreDataMap=new HashMap<>();
    final HashMap<String,Object> sortMap=new HashMap<>();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        init();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);

        addSideView();  //사이드바 add




        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn_home_nav=findViewById(R.id.btn_home_nav);
        sp_sort=findViewById(R.id.sp_sort);



        final Call<ArrayList<StoreData>> storeDataCall=iApiService.showallMyList(showStoreDataMap);
        final Call<ArrayList<StoreData>> sortCall=iApiService.sortMyList(sortMap);
        sd = new saveDialog(this);
        hd=new homeDialog(this);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        final int width = dm.widthPixels; //디바이스 화면 너비
        final int height = dm.heightPixels; //디바이스 화면 높이



       //=========================서버에서 저장한거 가져오는 코드=========================//
        try {
            showStoreDataMap.put("uID",sharedPreferences.getString("userEmail",null));
            storeDataCall.enqueue(new Callback<ArrayList<StoreData>>() {
                @Override
                public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {
                    try {
                        //디폴트에서 길이가 0이면
                        if(response.body().size()==0)
                        {


                            sd.callFunction();

                        }
                        StoreAdapter sa = new StoreAdapter(response.body());
                        mRecyclerView.setAdapter(sa);
                        Log.d("저장정보", "" + new Gson().toJson(response.body()));
                    }catch(Exception j)
                    {
                        j.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

                }
            });
        }catch(Exception e)
        {
            e.printStackTrace();
        }


        /**********************************다운로드에서 정렬하기*****************************************************/

        sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("소트값",""+sp_sort.getItemAtPosition(position));

                //저장날짜와 지원 날짜로 sorting하기
                // ***********************************값 널기*******************************//
                if(sp_sort.getItemAtPosition(position).equals("저장날짜순"))
                {
                    Toasty.success(mContext, "저장날짜순", Toast.LENGTH_SHORT, true).show();
                    dateValue=sp_sort.getItemAtPosition(position).toString();
                }


                if(sp_sort.getItemAtPosition(position).equals("지원날짜순"))
                {
                    Toasty.success(mContext, "지원날짜순", Toast.LENGTH_SHORT, true).show();
                    dateValue=sp_sort.getItemAtPosition(position).toString();
                }


                sortMap.put("uID",sharedPreferences.getString("userEmail",null));
                sortMap.put("Sort_by",dateValue);


                Log.d("추가정렬전",""+sharedPreferences.getString("userEmail",null) + " " + dateValue);
                //******************************통신하기****************************//
                if(dateValue.equals("저장날짜순") || dateValue.equals("지원날짜순")) {
                    try{
                        sortCall.clone().enqueue(new Callback<ArrayList<StoreData>>() {
                            @Override
                            public void onResponse(Call<ArrayList<StoreData>> call, Response<ArrayList<StoreData>> response) {

                                Log.d("추가정렬정보",""+new Gson().toJson(response.body()));
                                StoreAdapter sa = new StoreAdapter(response.body());
                                mRecyclerView.setAdapter(sa);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<StoreData>> call, Throwable t) {

                            }
                        });
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


}
