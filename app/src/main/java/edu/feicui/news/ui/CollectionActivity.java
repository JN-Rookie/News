package edu.feicui.news.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.feicui.news.R;

public class CollectionActivity extends AppCompatActivity {

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("收藏界面");
    }
}
