package edu.feicui.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import edu.feicui.news.R;
import edu.feicui.news.bean.News;
import edu.feicui.news.db.NewsDBManager;
import edu.feicui.news.fragment.MainFragment;


public class WebActivity extends MyBaseActivity {
    private static final String TAG = "NewsListActivity";
    private WebView mView;
    private ImageButton mButton;
    private String mLink;
    private String mTitle;
    private String mType;
    private String mIcon;
    private String mStamp;
    private String mNid;
    private String mSummary;
    private RelativeLayout mRl_web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent=getIntent();
        mLink = intent.getStringExtra("link");
        mTitle = intent.getStringExtra("title");
        mType = intent.getStringExtra("type");
        mIcon = intent.getStringExtra("icon");
        mStamp = intent.getStringExtra("stamp");
        mNid = intent.getStringExtra("nid");
        mSummary = intent.getStringExtra("summary");
        initView();
        WebSettings settings=mView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        mView.loadUrl(mLink);
        mView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    mRl_web.setVisibility(View.INVISIBLE);
                    mView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"加载完成!",Toast.LENGTH_SHORT).show();
                } else {
                    // 加载中

                }

            }
        });
    }

    private void initView() {
        mView= (WebView) findViewById(R.id.webview);
        mButton= (ImageButton) findViewById(R.id.ib_collection);
        mRl_web = (RelativeLayout) findViewById(R.id.rll_web);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDBManager manager=new NewsDBManager(getApplicationContext());
                if(manager.saveLoveNews(mSummary,mIcon,mStamp,mTitle,mNid,mLink,mType)){
                    Toast.makeText(getApplicationContext(),"收藏成功！\n在主界面侧滑菜单中查看",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"已经收藏过这条新闻了！\n在主界面侧滑菜单中查看",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
