package edu.feicui.news.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import edu.feicui.news.R;
import edu.feicui.news.ui.CollectionActivity;
import edu.feicui.news.ui.NewsListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMenu extends Fragment implements View.OnClickListener {
    private RelativeLayout[] rls =new RelativeLayout[5];

    public FragmentMenu() {
        // Required empty public constructor
    }


    //重写 onCreateView 方法，设置当前的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 利用回调中的参数 LayoutInflater 对象导入布局文件，并发挥此 View
        View view=inflater.inflate(R.layout.fragment_menu,container,false);
        rls[0]= (RelativeLayout) view.findViewById(R.id.rl_news);
        rls[1]= (RelativeLayout) view.findViewById(R.id.rl_reading);
        rls[2]= (RelativeLayout) view.findViewById(R.id.rl_local);
        rls[3]= (RelativeLayout) view.findViewById(R.id.rl_commnet);
        rls[4]= (RelativeLayout) view.findViewById(R.id.rl_photo);
        rls[0].setBackgroundColor(0x33c85555);
        for (int i = 0; i < rls.length; i++) {
            rls[i].setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < rls.length; i++) {
            rls[i].setBackgroundColor(0);
        }
        NewsListActivity newsListActivity=new NewsListActivity();
        switch (v.getId()){
            case R.id.rl_news:
                newsListActivity.initLocation();
                break;
            case R.id.rl_reading:
                rls[1].setBackgroundColor(0x33c85555);
                Intent intent=new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_local:
                rls[2].setBackgroundColor(0x33c85555);
                break;
            case R.id.rl_commnet:
                rls[3].setBackgroundColor(0x33c85555);
                break;
            case R.id.rl_photo:
                rls[4].setBackgroundColor(0x33c85555);
                break;
        }
    }
}
