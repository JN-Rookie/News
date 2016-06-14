package edu.feicui.news.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.feicui.news.R;
import edu.feicui.news.adapter.NewsAdapter;
import edu.feicui.news.bean.News;
import edu.feicui.news.ui.WebActivity;
import edu.feicui.news.utils.BitmapUtils;
import edu.feicui.news.utils.HttpUtils;
import edu.feicui.news.utils.OnRefreshListener;
import edu.feicui.news.view.RefreshListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnRefreshListener {
    private RefreshListView     mListView;
    private List<News.DataBean> mData;
    private NewsAdapter         mAdapter;
    private Gson                gson;
    private ImageLoader         mImageLoader;
    private static final String path = "http://118.244.212.82:9092/" +
            "newsClient/news_list?ver=1&subid=1&dir=1&nid=5&stamp=20140321&cnt=20";
    private RequestQueue mQueue;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (RefreshListView) view.findViewById(R.id.lv_list);
        mQueue = Volley.newRequestQueue(getActivity());
        mImageLoader = new ImageLoader(mQueue, new BitmapUtils());
        mAdapter = new NewsAdapter(getActivity(), mImageLoader);
        mListView.setAdapter(mAdapter);
        new AsyncTask<String, Void, News>() {
            @Override
            protected News doInBackground(String... params) {
                String info = HttpUtils.getInfo(path);
                mData = new ArrayList<>();
                gson = new Gson();
                News bean = gson.fromJson(info, News.class);
                return bean;
            }

            @Override
            protected void onPostExecute(News dataBean) {
                mAdapter.addData(dataBean.getData());
                mAdapter.notifyDataSetChanged();
                super.onPostExecute(dataBean);
            }
        }.execute();
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", mAdapter.getItem(position - 1).getLink());
                startActivity(intent);
            }
        });
        return view;
    }

    //    实现listview的下拉刷新
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<String, Void, News>() {

            @Override
            protected News doInBackground(String... params) {
                String info = HttpUtils.getInfo(path);
                mData = new ArrayList<>();
                gson = new Gson();
                News bean = gson.fromJson(info, News.class);
                return bean;
            }

            //            添加完数据后,隐藏头部view
            protected void onPostExecute(News dataBean) {
                mAdapter.addData(dataBean.getData());
                mAdapter.notifyDataSetChanged();
                mListView.hideHeaderView();
            }
        }.execute();

    }

    //    实现listview的上拉加载
    @Override
    public void onLoadingMore() {
        new AsyncTask<String, Void, News>() {

            @Override
            protected News doInBackground(String... params) {
                String info = HttpUtils.getInfo(path);
                mData = new ArrayList<>();
                gson = new Gson();
                News bean = gson.fromJson(info, News.class);
                return bean;
            }

            //            添加完数据后,隐藏底部view
            protected void onPostExecute(News dataBean) {
                mAdapter.addData(dataBean.getData());
                mAdapter.notifyDataSetChanged();
                mListView.hideFooterView();
            }
        }.execute();

    }
}
