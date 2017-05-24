package com.example.chococardsek.customviewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout pagerIndicator;
    private CustomViewPagerAdapter mAdapter;

    private ArrayList<String> mImageResources;
    private Handler handler;
    private CustomViewPager customViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding();
    }

    public void binding() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerIndicator = (LinearLayout) findViewById(R.id.pagerIndicator);

        mImageResources = new ArrayList<>();
        mImageResources.add("https://www.w3schools.com/css/img_fjords.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_mountains.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_lights.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_forest.jpg");
        mImageResources.add("http://dreamicus.com/data/image/image-06.jpg");
        mAdapter = new CustomViewPagerAdapter(MainActivity.this, mImageResources);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);

        handler = new Handler();
        customViewPager = new CustomViewPager(this, viewPager, handler, viewPager.getAdapter().getCount(), pagerIndicator);
        customViewPager.setUIPagerIndicator();
        viewPager.setOnPageChangeListener(customViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customViewPager.viewPagerLooper.run();
    }

    @Override
    protected void onStop() {
        super.onStop();
        customViewPager.stopViewPagerLooper();
    }
}
