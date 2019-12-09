package com.example.mypolicy;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;


public class DonateURL  extends WebViewClient {

    private WebView mWebView;
    private Activity activity;
    private DonateURL mContext = DonateURL.this;

    public DonateURL(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            Intent intent = null;
            //Intent intent=new Intent(activity, ProfileActivity.class); //intent 사용하여 넘어갈 activity 정함
            Log.d("import함수", "웹뷰 링크? " + url);
            try {

                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); //IntentURI처리
                Uri uri = Uri.parse(intent.getDataString());

                activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                Log.d("import함수","결제진행완료?");
                return true;
            } catch (URISyntaxException ex) {
                return false;
            } catch (ActivityNotFoundException e) {
                if ( intent == null )   return false;

                //if ( handleNotFoundPaymentScheme(intent.getScheme()) )   return true;

                String packageName = intent.getPackage();
                if (packageName != null) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    return true;
                }

                return false;
            }
        }

        return false;
    }

}