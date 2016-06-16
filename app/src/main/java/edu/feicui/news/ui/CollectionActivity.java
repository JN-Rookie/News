package edu.feicui.news.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.List;

import edu.feicui.news.R;
import edu.feicui.news.adapter.NewsAdapter;
import edu.feicui.news.bean.News;
import edu.feicui.news.db.NewsDBManager;
import edu.feicui.news.utils.BitmapUtils;

public class CollectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView            mTitle;
    private ListView            mListView;
    private List<News.DataBean> mData;
    private NewsAdapter         mAdapter;
    private ImageLoader         mImageLoader;
    private RequestQueue        mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mTitle = (TextView) findViewById(R.id.title);
        mListView= (ListView) findViewById(R.id.lv_collection);
        mTitle.setText("收藏界面");
        mQueue = Volley.newRequestQueue(getApplicationContext());
        mImageLoader = new ImageLoader(mQueue, new BitmapUtils());
        mAdapter=new NewsAdapter(this,mImageLoader);
        loadLoveNews();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    /**从数据库中加载保存的新闻*/
    private void loadLoveNews() {
        mData=new NewsDBManager(getApplicationContext()).queryNews();
        mAdapter.addData(mData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("link", mAdapter.getItem(position).getLink());
        startActivity(intent);
    }
}
