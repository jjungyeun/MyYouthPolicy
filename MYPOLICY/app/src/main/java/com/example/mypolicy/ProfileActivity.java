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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;

    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;

    FirebaseFirestore db;
    SharedPreferences session, autoLogin;

    TextView tv_profile_email, tv_name;
    TextView tv_edit_info,tv_center, tv_logout;
    LinearLayout ll_request, ll_edit_interest,ll_please_donate;
    Switch sw_autoLogin;
    requestDialog rd;
    versionDialog vd;
    ImageView versionImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        addSideView();  //사이드바 add

        db = FirebaseFirestore.getInstance();
        session = getSharedPreferences("session",MODE_PRIVATE);
        autoLogin = getSharedPreferences("autoLogin",MODE_PRIVATE);
        tv_name = findViewById(R.id.tv_name);
        tv_profile_email = findViewById(R.id.tv_profile_email);
        tv_edit_info = findViewById(R.id.tv_edit_personal_info);
        ll_edit_interest = findViewById(R.id.ll_edit_interest);
        tv_center=findViewById(R.id.tv_center);
        tv_logout = findViewById(R.id.tv_logout);
        ll_request = findViewById(R.id.ll_request);
        sw_autoLogin = findViewById(R.id.sw_autoLogin);
        ll_please_donate=findViewById(R.id.ll_please_donate);

        rd=new requestDialog(this);
        vd=new versionDialog(this);


        DocumentReference userInfo = db.collection("user").document(session.getString("userEmail",null));
        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        tv_name.setText(document.get("name").toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        tv_profile_email.setText(session.getString("userEmail",null));

        tv_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditPersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.edit().clear().apply();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ll_edit_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this, EditCategoryActivity.class);
                startActivity(intent);
            }
        });

        tv_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vd.callFunction();
            }
        });
//        versionImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                vd.dismissFunction();
//            }
//        });
        ll_please_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "1000원기부하기", Toast.LENGTH_LONG).show(); // 잠깐 뜨는 메세지
                Log.d("기부버튼","눌렀다"); // 콘솔창에 뜸
                Intent intent = new Intent(mContext, DonateActivity.class);
                startActivity(intent);
            }
        });



        ll_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rd.callFunction();

            }
        });

        if (autoLogin.getString("state","").equals("no"))
            sw_autoLogin.setChecked(false);
        sw_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor loginEditor = autoLogin.edit();
                if(isChecked){
                    loginEditor.putString("state", "yes");
                } else {
                    loginEditor.putString("state", "no");
                }
                loginEditor.apply();

//                Toast.makeText(mContext, "autoLogin.state: "+autoLogin.getString("state",""), Toast.LENGTH_SHORT).show();
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
                session.edit().clear().apply();
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
