package com.example.chococardsek.customviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chococardsek.customviewpager.databinding.CustomViewpagerBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CustomViewPager extends FrameLayout {

    private ImageView[] imageList;
    private int mCurrentPosition = 0;
    private int mScrollState;
    private int mContentSize;
    private Disposable subscription;
    private CustomViewpagerBinding mBinding;
    private CustomViewPagerAdapter mAdapter;

    public CustomViewPager(Context context) {
        super(context);
        initInflate();
        initInstance();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstance();
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = CustomViewpagerBinding.inflate(inflater, this, true);
    }

    private void initInstance() {
        mBinding.viewPager.performClick();
    }

    public void setResourceSize(int resourceSize) {
        mContentSize = resourceSize;
    }

    private void setUIPagerIndicator() {
        if (mContentSize > 1) {
            mBinding.pagerIndicator.setVisibility(VISIBLE);
            for (int i = 0; i < mContentSize; i++) {
                imageList[i] = new ImageView(getContext());
                imageList[i].setImageDrawable(getContext().getResources().getDrawable(R.drawable.shape_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(16, 0, 16, 0);
                mBinding.pagerIndicator.addView(imageList[i], params);
            }
            imageList[0].setImageDrawable(getContext().getResources().getDrawable(R.drawable.shape_dot_selected));
        } else {
            mBinding.pagerIndicator.setVisibility(View.GONE);
        }
    }

    public void setAdapter(CustomViewPagerAdapter adapter) {
        mAdapter = adapter;
        mContentSize = adapter.getCount();
        mBinding.viewPager.setAdapter(adapter);
        imageList = new ImageView[mContentSize];
        setUIPagerIndicator();
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                for (int i = 0; i < mContentSize; i++)
                    imageList[i].setImageDrawable(getContext().getResources().getDrawable(R.drawable.shape_dot));
                imageList[position].setImageDrawable(getContext().getResources().getDrawable(R.drawable.shape_dot_selected));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                handleScrollState(state);
                mScrollState = state;
            }
        });

        mBinding.viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        stopSlide();
                        break;
                    case MotionEvent.ACTION_UP:
                        runSlide();
                        break;
                }
                return false;
            }
        });
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
            mBinding.viewPager.setCurrentItem(lastPosition, false);
        else if (mCurrentPosition == lastPosition)
            mBinding.viewPager.setCurrentItem(0, false);
    }

    public void runSlide() {
        if (subscription == null || subscription.isDisposed())
            subscription = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (mCurrentPosition == mContentSize) {
                                mCurrentPosition = 0;
                                mBinding.viewPager.setCurrentItem(mCurrentPosition++, false);
                            } else {
                                mBinding.viewPager.setCurrentItem(mCurrentPosition++, true);
                            }
                        }
                    });
    }

    public void stopSlide() {
        if (subscription != null)
            subscription.dispose();
    }

    @Override
    public boolean performClick() {
        return true;
    }
}
