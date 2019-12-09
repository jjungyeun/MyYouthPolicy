package com.example.mypolicy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DonateActivity  extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "DonateActivity";
    private Context mContext = DonateActivity.this;
    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역

    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    SharedPreferences sharedPreferences;

    private WebView mainWebView;
    private static final String APP_SCHEME = "iamporttest://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);//당신이 꾸밀 xml을 만들어 준다
        //Toast.makeText(getApplicationContext(), "세번째 띄우기", Toast.LENGTH_LONG).show();

        init();
        addSideView();  //사이드바 add

        sharedPreferences = getSharedPreferences("session",MODE_PRIVATE);


        mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.setWebViewClient(new DonateURL(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        Intent intent = getIntent();
        Uri intentData = intent.getData();

        if ( intentData == null ) {
            mainWebView.loadUrl("file:///android_asset/www/test.html");
            //mainWebView.loadUrl("http://www.iamport.kr/demo");
            // Log.d("import함수","여기냐?1");

        } else {
            //Log.d("import함수","여기냐?22");
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            if (url.startsWith(APP_SCHEME)) {
                String redirectURL = url.substring(APP_SCHEME.length() + "://".length());
                //Log.d("import함수 결제끝나면",redirectURL);
                mainWebView.loadUrl(redirectURL); //웹뷰에서 트는건데.
            }
        }

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