package edu.feicui.news.utils;

/**
 * Created by Administrator on 2016/6/13.
 */
public interface OnRefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
