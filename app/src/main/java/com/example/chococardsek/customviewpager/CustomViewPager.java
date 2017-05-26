package com.example.chococardsek.customviewpager;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.chococardsek.customviewpager.databinding.CustomViewpagerBinding;
import com.example.chococardsek.customviewpager.databinding.ItemPagerBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CustomViewPager extends FrameLayout {

    private ImageView[] imageList;
    private int mCurrentPosition = 0;
    private int mScrollState;
    private int mContentSize = 0;
    private Disposable subscription;
    private CustomViewpagerBinding mBinding;
    private CustomViewPagerAdapter mAdapter;
    public SlideClickedListener slideClickedListener;
    private int delay;

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
        delay = 0;
        mBinding.viewPager.performClick();
        mAdapter = new CustomViewPagerAdapter(getContext());
        mBinding.viewPager.setAdapter(mAdapter);
        mContentSize = mAdapter.getCount();
        imageList = new ImageView[mContentSize];
        setUIPagerIndicator();
    }

    public void setupCustomViewPager(ArrayList<String> resources) {
        mAdapter.setResources(resources);
        mContentSize = mAdapter.getCount();
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
                if (delay == 0)
                    return false;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        stopSlide();
                        break;
                    case MotionEvent.ACTION_UP:
                        runSlide(5000);
                        break;
                }
                return false;
            }
        });
    }

    private void setUIPagerIndicator() {
        mBinding.pagerIndicator.removeAllViews();
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

    public void runSlide(int delay) {
        stopSlide();
        this.delay = delay;

        if (subscription == null || subscription.isDisposed())
            subscription = Observable.interval(0, delay, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
//                            Log.d("time",aLong+"");
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

    private class CustomViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<String> mResources;
        private ItemPagerBinding mBinding;

        public CustomViewPagerAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            if (mResources == null)
                return 0;
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pager, container, false);
            mBinding = DataBindingUtil.bind(itemView);

            Glide.with(mContext)
                    .load(mResources.get(position))
                    .into(mBinding.imgPagerItem);

            mBinding.imgPagerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideClickedListener.onSlideClickedListener(mResources.get(position));
                }
            });
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        public void setResources(ArrayList<String> mResources) {
            this.mResources = mResources;
            notifyDataSetChanged();
        }
    }

    public void setSlideClickedListener(SlideClickedListener slideClickedListener) {
        this.slideClickedListener = slideClickedListener;
    }

    interface SlideClickedListener {
        void onSlideClickedListener(String imagePath);
    }
}