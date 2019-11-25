package com.example.mypolicy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.view.View;

public class SideBarView extends RelativeLayout implements View.OnClickListener {

    public EventListener listener;
    public void setEventListener(EventListener l) {
        listener = l;
    }

    /** 메뉴버튼 클릭 이벤트 리스너 인터페이스 */
    public interface EventListener {

        // 닫기 버튼 클릭 이벤트
        void btnCancel();
        void btnHome();
        void btnSearch();
        void btnDownload();
        void btnProfile();
        void btnTop();
        void btnLogout();
    }

    public SideBarView(Context context) {
        this(context, null); init();
    }

    public SideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.sidebar, this, true);
        findViewById(R.id.menu_cancel).setOnClickListener(this);
        findViewById(R.id.menu_home).setOnClickListener(this);
        findViewById(R.id.menu_search).setOnClickListener(this);
        findViewById(R.id.menu_download).setOnClickListener(this);
        findViewById(R.id.menu_profile).setOnClickListener(this);
        findViewById(R.id.menu_top20).setOnClickListener(this);
        findViewById(R.id.menu_logout).setOnClickListener(this);

    }

    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_cancel : listener.btnCancel(); break;
            case R.id.menu_home : listener.btnHome(); break;
            case R.id.menu_search : listener.btnSearch(); break;
            case R.id.menu_download : listener.btnDownload(); break;
            case R.id.menu_top20: listener.btnTop(); break;
            case R.id.menu_profile : listener.btnProfile(); break;
            case R.id.menu_logout : listener.btnLogout(); break;
            default: break;
        }
    }
}
