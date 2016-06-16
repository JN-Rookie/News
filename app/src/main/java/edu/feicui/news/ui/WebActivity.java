package edu.feicui.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.feicui.news.R;
import edu.feicui.news.bean.News;
import edu.feicui.news.db.NewsDBManager;
import edu.feicui.news.fragment.MainFragment;


public class WebActivity extends MyBaseActivity {
    private static final String TAG = "NewsListActivity";
    private WebView     mView;
    private ImageButton mButton;
    List<Map<String, String>> moreList;
    private PopupWindow pwMyPopWindow;// popupwindow
    private ListView    lvPopupList;// popupwindow中的ListView
    private int NUM_OF_VISIBLE_LIST_ROWS = 3;// 指定popupwindow中Item的数量
    private String         mLink;
    private String         mTitle;
    private String         mType;
    private String         mIcon;
    private String         mStamp;
    private String         mNid;
    private String         mSummary;
    private RelativeLayout mRl_web;
    private String mNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        mNum = intent.getStringExtra("num");
        mLink = intent.getStringExtra("link");
        mTitle = intent.getStringExtra("title");
        mType = intent.getStringExtra("type");
        mIcon = intent.getStringExtra("icon");
        mStamp = intent.getStringExtra("stamp");
        mNid = intent.getStringExtra("nid");
        mSummary = intent.getStringExtra("summary");
        initView();
        WebSettings settings = mView.getSettings();
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
                    Toast.makeText(getApplicationContext(), "加载完成!", Toast.LENGTH_SHORT).show();
                } else {
                    // 加载中

                }

            }
        });
    }

    private void initView() {
        mView = (WebView) findViewById(R.id.webview);
        mButton = (ImageButton) findViewById(R.id.ib_collection);
        mRl_web = (RelativeLayout) findViewById(R.id.rll_web);
        iniData();
        iniPopupWindow();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwMyPopWindow.isShowing()) {

                    pwMyPopWindow.dismiss();// 关闭
                } else {

                    pwMyPopWindow.showAsDropDown(mButton);// 显示
                }
//
            }
        });
    }

    private void iniPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
        pwMyPopWindow = new PopupWindow(layout);
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

        lvPopupList.setAdapter(new SimpleAdapter(getApplicationContext(), moreList,
                R.layout.list_item_popupwindow, new String[]{"share_key"},
                new int[]{R.id.tv_list_item}));
        lvPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(mNum.equals("1")){
                    switch (moreList.get(position).get("share_key")) {
                        case "收藏":
                            NewsDBManager manager = new NewsDBManager(getApplicationContext());
                            if (manager.saveLoveNews(mSummary, mIcon, mStamp, mTitle, mNid, mLink, mType)) {
                                Toast.makeText(getApplicationContext(), "收藏成功！\n在主界面侧滑菜单中查看",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "已经收藏过这条新闻了！" +
                                        "\n在主界面侧滑菜单中查看", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }else{
                    switch (moreList.get(position).get("share_key")) {
                        case "收藏":
                          Toast.makeText(getApplicationContext(),"已经收藏过了,无需重复收藏!",Toast.LENGTH_SHORT).show();
                            break;
                        case "取消收藏":
                            NewsDBManager manager = new NewsDBManager(getApplicationContext());
                            manager.deleteNews(mNid);
                            if (!mNid.isEmpty()) {
                                Intent intent=new Intent(getApplicationContext(),CollectionActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "删除成功！",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "还没有收藏过这条新闻了！" +
                                        "\n请在主界面侧滑菜单中选择", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }



            }
        });

        // 控制popupwindow的宽度和高度自适应
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)
                * NUM_OF_VISIBLE_LIST_ROWS);

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
    }

    private void iniData() {
        moreList = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        map = new HashMap<String, String>();
        map.put("share_key", "收藏");
        moreList.add(map);
        map = new HashMap<String, String>();
        map.put("share_key", "取消收藏");
        moreList.add(map);
        map = new HashMap<String, String>();
        map.put("share_key", "跟帖");
        moreList.add(map);
    }
}
