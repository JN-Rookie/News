package edu.feicui.news.fragment;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.feicui.news.R;
import edu.feicui.news.adapter.NewsAdapter;
import edu.feicui.news.bean.News;
import edu.feicui.news.ui.CollectionActivity;
import edu.feicui.news.ui.MyBaseActivity;
import edu.feicui.news.ui.NewsListActivity;
import edu.feicui.news.ui.WebActivity;
import edu.feicui.news.utils.BitmapUtils;
import edu.feicui.news.utils.HttpUtils;
import edu.feicui.news.utils.OnRefreshListener;
import edu.feicui.news.view.RefreshListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnRefreshListener {
    private static final String TAG = "NewsListActivity";
    private RefreshListView     mListView;
    private List<News.DataBean> mData;
    private NewsAdapter         mAdapter;
    private Gson                gson;
    private ImageLoader         mImageLoader;
    private static final String path = "http://118.244.212.82:9092/" +
            "newsClient/news_list?ver=1&subid=1&dir=1&nid=5&stamp=20140321&cnt=20";
    private RequestQueue mQueue;
    private RelativeLayout mRl_main;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (RefreshListView) view.findViewById(R.id.lv_list);
        mRl_main = (RelativeLayout) view.findViewById(R.id.rll_main);
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
                ((NewsListActivity)getActivity()).mBean = gson.fromJson(info, News.class);
                return ((NewsListActivity)getActivity()).mBean;
            }

            @Override
            protected void onPostExecute(News dataBean) {
                mRl_main.setVisibility(View.INVISIBLE);
                mListView.setVisibility(View.VISIBLE);
                if(dataBean!=null){
                    Toast.makeText(getActivity(),"加载完成!",Toast.LENGTH_SHORT).show();
                    mAdapter.addData(dataBean.getData());
                    mAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(),"加载失败,请检查您的网络!",Toast.LENGTH_SHORT).show();
                }

                super.onPostExecute(dataBean);
            }
        }.execute();
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("link", mAdapter.getItem(position - 1).getLink());
                intent.putExtra("title", mAdapter.getItem(position - 1).getTitle());
                intent.putExtra("type", mAdapter.getItem(position - 1).getType());
                intent.putExtra("icon", mAdapter.getItem(position - 1).getIcon());
                intent.putExtra("stamp", mAdapter.getItem(position - 1).getStamp());
                intent.putExtra("nid", mAdapter.getItem(position - 1).getNid());
                intent.putExtra("summary", mAdapter.getItem(position - 1).getSummary());
                startActivity(intent);

            }
        });
        Log.d(TAG, "onCreateView: "+((NewsListActivity)getActivity()).mBean);
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
