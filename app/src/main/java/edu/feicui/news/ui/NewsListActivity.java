package edu.feicui.news.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.feicui.news.R;
import edu.feicui.news.adapter.NewsFragmentPagerAdapter;
import edu.feicui.news.bean.NewsClassify;
import edu.feicui.news.fragment.FragmentMenu;
import edu.feicui.news.fragment.FragmentMenuRight;
import edu.feicui.news.fragment.MainFragment;
import edu.feicui.news.utils.BaseTools;
import edu.feicui.news.utils.NewsTOP;
import edu.feicui.news.view.ColumnHorizontalScrollView;

public class NewsListActivity extends FragmentActivity {
    private static final String TAG = "NewsListActivity";
    private ColumnHorizontalScrollView mScrollView;//自定义HorizontalScrollView
    LinearLayout   mRadioGroup_content;
    LinearLayout   ll_more_columns;
    RelativeLayout rl_column;
    private ImageView button_more_columns;
    private ViewPager mViewPager;
    private ArrayList<NewsClassify> newsClassify      = new ArrayList<NewsClassify>();//新闻分类列表
    private int                     columnSelectIndex = 0;//当前选中的栏目
    public ImageView shade_left;//左阴影部分
    public ImageView shade_right;//右阴影部分
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentMenu      mFragmentMenu;//左菜单栏
    private FragmentMenuRight mFragmentMenuRight;//右菜单栏
    private MainFragment      mFragmentMain;
    private RelativeLayout    rl_main;//中间主布局
    private RelativeLayout    rl_left_frg;//左菜单栏布局
    private RelativeLayout    rls_right_frg;//右菜单栏布局
    private ImageButton       mButtonHome;//左侧菜单栏弹出按钮
    private ImageButton       mButtonShare;//右侧侧菜单栏弹出按钮
    private boolean First = true;
    private int x1, x2, x3;//定义屏幕X轴坐标
    private RelativeLayout.LayoutParams mLayoutParams;//主布局对象
    private int                         mWidth;//手机屏幕宽度
    private int mItemWidth = 0;//Item宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        mWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mWidth / 7;// 一个Item宽度为屏幕的1/7
        initView();
        isFirstRun();
    }

    /** 初始化layout控件*/
    private void initView() {
        mButtonHome = (ImageButton) findViewById(R.id.title_home);
        mButtonShare = (ImageButton) findViewById(R.id.title_share);
        rl_left_frg = (RelativeLayout) findViewById(R.id.rl_left_frg);
        rls_right_frg = (RelativeLayout) findViewById(R.id.rl_right_frg);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        mScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns=(LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLeftFragment();
            }
        });
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRightFragment();
            }
        });
        setChangelView();
    }
    /**
     *  当栏目项发生变化时候调用
     * */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }
    /** 获取Column栏目 数据*/
    private void initColumnData() {
        newsClassify = NewsTOP.getData();
    }

    /**
     *  初始化Column栏目项
     * */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  newsClassify.size();
        mScrollView.setParam(this, mWidth, mRadioGroup_content, shade_left,
                shade_right, ll_more_columns, rl_column);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            TextView localTextView = new TextView(this);
            localTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);
            localTextView.setText(newsClassify.get(i).getTitle());
            localTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(localTextView, i ,params);
        }
    }
    /**
     *  选择的Column里面的Tab
     * */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mWidth / 2;
            mScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }
    /**
     *  初始化Fragment
     * */
    private void initFragment() {
        int count =  newsClassify.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", newsClassify.get(i).getTitle());
            MainFragment fragment = new MainFragment();
            fragment.setArguments(data);
            fragments.add(fragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }
    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    //    加载右侧菜单栏
    private void getRightFragment() {
        if (mFragmentMenuRight == null) {
            mFragmentMenuRight = new FragmentMenuRight();
        }
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frg_right, mFragmentMenuRight);//加载fragment布局
        transaction.commit();//提交
        int width = rls_right_frg.getWidth();//右侧菜单栏宽度
        mLayoutParams.leftMargin = -width;//设置左边距相对于屏幕的偏移量
        mLayoutParams.rightMargin = width;//设置右边距相对于屏幕的偏移量
        rl_main.setLayoutParams(mLayoutParams);//重新加载布局
    }

    //    加载左侧菜单栏
    private void getLeftFragment() {
        if (mFragmentMenu == null) {
            mFragmentMenu = new FragmentMenu();
        }
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frg_left, mFragmentMenu);//加载fragment布局
        transaction.commit();//提交
        int width = rl_left_frg.getWidth();//左侧菜单栏宽度
        mLayoutParams.leftMargin = width;//设置左边距相对于屏幕的偏移量
        mLayoutParams.rightMargin = -width;//设置右边距相对于屏幕的偏移量
        rl_main.setLayoutParams(mLayoutParams);//重新加载布局
    }

    //      初始化屏幕偏移量,并移除两侧的菜单栏
    public void initLocation() {
        mLayoutParams.leftMargin = 0;
        mLayoutParams.rightMargin = 0;
        rl_main.setLayoutParams(mLayoutParams);//重新加载布局
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (mFragmentMenu != null) {
            transaction.remove(mFragmentMenu);//移除左侧菜单栏
        }
        if (mFragmentMenuRight != null) {
            transaction.remove(mFragmentMenuRight);//移除右侧菜单栏
        }
        transaction.commit();
    }

    //    判断是否第一次加载当前activity，如果是获取屏幕的宽度，并锁定主布局的宽度
    public void isFirstRun() {
        if (First) {
            //获取的手机屏幕的宽度(PX)
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mWidth = metrics.widthPixels;
            mItemWidth = mWidth / 7;// 一个Item宽度为屏幕的1/7
            mLayoutParams = (RelativeLayout.LayoutParams) rl_main.getLayoutParams();
            mLayoutParams.width = mWidth;
            First = false;
        }
    }
    //    实现左右滑动的方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                x3 = rl_main.getLeft();//主布局左侧偏移量
                x1 = (int) ev.getX();//获取按下的X轴坐标
                break;
            case MotionEvent.ACTION_MOVE:
                x2= (int) ev.getX();
                if(x1<100&&x2>x1&&x3==0){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                x2 = (int) ev.getX();//获取松开时的X轴坐标
                //向右滑
                if ((x2 - x1) > 0) {
                    if ((x2 - x1) > 200 && (x1 - x3) < 200) {//滑动距离大于200px并且滑动起点相对于主布局左边距的坐标小于200px
                        getLeftFragment();
                        return false;
                    }
                    if ((x2 - x1) > 200 && (x1 - x3) > (mWidth - 200))//滑动距离大于200px并且滑动起点距离主布局右边距小于200px
                    {
                        initLocation();
                        return false;
                    }
                }
                //向左滑
                if ((x2 - x1) < 0) {
                    if (Math.abs((x2 - x1)) > 200 && ((x1 - x3)) > (mWidth - 200)) {
                        getRightFragment();
                        return false;
                    }
                    if ((Math.abs((x2 - x1)) > 200) && (x1 - x3) < 200) {
                        initLocation();
                        return false;
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
