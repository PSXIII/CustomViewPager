package com.example.chococardsek.customviewpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressWarnings("WeakerAccess")
public class CustomViewPager implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private ViewPager mViewPager;
    private Handler mHandler;
    private LinearLayout mViewIndicator;
    private ImageView[] imageList;
    private int mCurrentPosition;
    private int mScrollState;
    private int mContentSize;

    public CustomViewPager(Context mContext, ViewPager mViewPager, Handler mHandler, int mContentSize, LinearLayout mViewIndicator) {
        this.mContext = mContext;
        this.mViewPager = mViewPager;
        this.mHandler = mHandler;
        this.mContentSize = mContentSize;
        this.mViewIndicator = mViewIndicator;
    }

    public void setUIPagerIndicator() {
        if (mContentSize > 1) {
            mViewIndicator.setVisibility(View.VISIBLE);
            imageList = new ImageView[mContentSize];
            for (int i = 0; i < mContentSize; i++) {
                imageList[i] = new ImageView(mContext);
                imageList[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(16, 0, 16, 0);
                mViewIndicator.addView(imageList[i], params);
            }
            imageList[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_dot_selected));
        } else {
            mViewIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mContentSize; i++)
            imageList[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_dot));
        imageList[position].setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_dot_selected));
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        handleScrollState(state);
        mScrollState = state;
    }

    private void handleScrollState(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) setNextItemIfNeeded();
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) handleSetNextItem();
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = mContentSize - 1;
        if (mCurrentPosition == 0)
            mViewPager.setCurrentItem(lastPosition, false);
        else if (mCurrentPosition == lastPosition)
            mViewPager.setCurrentItem(0, false);
    }

    public Runnable viewPagerLooper = new Runnable() {
        @Override
        public void run() {
            if (mCurrentPosition == mContentSize) mCurrentPosition = 0;
            mViewPager.setCurrentItem(mCurrentPosition++, true);
            mHandler.postDelayed(viewPagerLooper, 3000); // 5 seconds
        }
    };

    public void stopViewPagerLooper() {
        mHandler.removeCallbacks(viewPagerLooper);
    }

    @Override
    public void onPageScrolled(int position, float v, int i1) {
    }
}
