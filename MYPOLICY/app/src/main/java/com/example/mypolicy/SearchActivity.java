package com.example.mypolicy;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypolicy.adapter.PolicyAdapter;
import com.example.mypolicy.model.Policy;
import com.example.mypolicy.model.SearchData;
import com.example.mypolicy.service.IApiService;
import com.example.mypolicy.service.RestClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "SearchActivity";

    private Context mContext = SearchActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    SharedPreferences sharedPreferences;

    private String mClassName = getClass().getName().trim();
    private RecyclerView mRecyclerView;
    final String[] categories = new String[]{"전체","취업지원","창업지원","생활/복지","주거/금융"};
    Button btn_select_category, btn_search;
    Button btn_apply_always,btn_apply_now,btn_apply_before,btn_apply_after,btn_apply_all;
    TextView tv_categories;
    EditText et_keyword;
    Spinner sp_do, sp_si, sp_age, sp_time;
    ArrayList<String> search_region =new ArrayList<>();
    String search_category = "10000";
    String selected_categories = "";
    String selected_age = "전체";
    String selected_time="전체";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);
        addSideView();  //사이드바 add

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final IApiService iApiService=new RestClient("http://49.236.136.213:3000/").getApiService();
        final Call<ArrayList<Policy>> call=iApiService.showAllPolicies();
        final Call<ArrayList<Policy>> alwaysCall=iApiService.showAllPolicies_always();
        final Call<ArrayList<Policy>> nowCall=iApiService.showAllPolicies_nowPossible();
        final Call<ArrayList<Policy>> beforeCall=iApiService.showAllPolicies_beforeApply();
        final Call<ArrayList<Policy>> afterCall=iApiService.showAllPolicies_afterApply();






        tv_categories = findViewById(R.id.tv_categories);
        btn_select_category = findViewById(R.id.btn_select_category);
        btn_search = findViewById(R.id.btn_search);
        sp_do = findViewById(R.id.sp_do);
        sp_si = findViewById(R.id.sp_si);
        sp_time = findViewById(R.id.sp_timesorting);
        sp_age = findViewById(R.id.sp_age);
        et_keyword = findViewById(R.id.et_search_keyword);
        btn_apply_always=findViewById(R.id.btn_apply_always);
        btn_apply_now=findViewById(R.id.btn_apply_now);
        btn_apply_before=findViewById(R.id.btn_apply_before);
        btn_apply_after=findViewById(R.id.btn_apply_after);
        btn_apply_all=findViewById(R.id.btn_apply_all);

/**********************************전체 정보 받아오는 부분**********************************************************************/
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

/**************************************************키워드 클릭 검색********************************************/
        btn_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_category = "10000";
                final boolean[] selected = new boolean[]{false, false, false, false, false};
                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchActivity.this);
                dialog.setTitle("정책 유형 선택")
                        .setMultiChoiceItems(
                                categories,
                                new boolean[]{false, false, false, false, false},
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                                        if(isChecked){
                                            selected[position] = true;
                                        }
                                        else {
                                            selected[position] = false;
                                        }
                                    }
                                }
                        )
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String txt = "";
                                selected_categories = "선택한 카테고리: ";
                                if(selected[0]){
                                    txt = "10000";
                                    selected_categories += "전체";
                                    for(int j = 1;j<5;j++){
                                        selected[j] = false;
                                    }
                                }
                                else {
                                    for(int j=0;j<5;j++){
                                        if(selected[j]){
                                            txt += "1";
                                            selected_categories += categories[j] + ", ";
                                        }
                                        else
                                            txt += "0";
                                    }
                                    if(txt.equals("00000")){
                                        txt = "10000";
                                        selected_categories = "";
                                    }
                                    else {
                                        selected_categories = selected_categories.substring(0,selected_categories.length()-2);
                                    }
                                }

                                //Toast.makeText(mContext, txt, Toast.LENGTH_SHORT).show();
                                search_category = txt;
                                tv_categories.setText(selected_categories);
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                search_category = "10000";
                                selected_categories = "";
                                tv_categories.setText(selected_categories);
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });



        search_region.add("전국");
        search_region.add("전체");

        sp_do.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String region = parent.getItemAtPosition(position).toString();
                search_region.set(0,region);
                search_region.set(1,"전체");
                ArrayAdapter adapter= ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.전체,
                        android.R.layout.simple_list_item_1);
                switch (region){
                    case "서울":
                        adapter = ArrayAdapter.createFromResource(
                                getApplicationContext(),
                                R.array.서울,
                                android.R.layout.simple_list_item_1);
                        break;
                    case "경기":
                        adapter = ArrayAdapter.createFromResource(
                                getApplicationContext(),
                                R.array.경기,
                                android.R.layout.simple_list_item_1);
                        break;
                    default:

                        adapter = ArrayAdapter.createFromResource(
                                getApplicationContext(),
                                R.array.전체,
                                android.R.layout.simple_list_item_1);
                        break;
                }
                sp_si.setAdapter(adapter);
                sp_si.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String region = parent.getItemAtPosition(position).toString();
                search_region.set(1,region);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_time=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_age = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selected_age = "전체";
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(SearchActivity.this, "검색중입니다....", Toast.LENGTH_SHORT, true).show();

                int age;
                if(selected_age.equals("전체"))
                    age = 0;
                else
                    age = Integer.parseInt(selected_age);

                String keyword = et_keyword.getText().toString();
                Bundle extras=new Bundle();
                Intent intent=new Intent(SearchActivity.this,SearchKeywordActivity.class);
                extras.putStringArrayList("search_region",search_region);
                extras.putString("search_category",search_category);
                extras.putInt("age",age);
                extras.putString("keyword",keyword);
                extras.putString("time",selected_time);
                intent.putExtras(extras);
                startActivity(intent);
//                final Call<ArrayList<SearchData>> postSearchcall=iApiService.postSearchKeyword(search_region,search_category,age,keyword);
//                Log.d("모르겠다","search_region: "+search_region);
//                Log.d("모르겠다","search_category: "+search_category);
//                Log.d("모르겠다","age: "+age);
//                Log.d("모르겠다","keyword: "+keyword);


//                 ------------------ 서버로 req 보내기 --------------------------
//                 정책유형: search_category (String - ex: "00101")
//                 지역: search_region (ArrayList<String> - ex: {"서울", "강남구"})
//                 나이: age (int)
//                 키워드: keyword (String)



            }
        });


        /************************************시간별 버튼 4개에 대한 정책 정보 수집*********************************/
        btn_apply_always.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alwaysCall.clone().enqueue(new Callback<ArrayList<Policy>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                        Log.d("상시모집",""+new Gson().toJson(response.body()));
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

                    }
                });

            }
        });


        btn_apply_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowCall.clone().enqueue(new Callback<ArrayList<Policy>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                            Log.d("나우버튼",""+new Gson().toJson(response.body()));

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

                    }
                });

            }
        });

        btn_apply_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beforeCall.clone().enqueue(new Callback<ArrayList<Policy>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                        Log.d("나우버튼","비포어"+new Gson().toJson(response.body()));

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

                    }
                });

            }
        });
        btn_apply_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    afterCall.clone().enqueue(new Callback<ArrayList<Policy>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Policy>> call, Response<ArrayList<Policy>> response) {
                            Log.d("나우버튼","애프터"+new Gson().toJson(response.body()));
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

                        }
                    });
            }
        });
        btn_apply_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    call.clone().enqueue(new Callback<ArrayList<Policy>>() {
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
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
