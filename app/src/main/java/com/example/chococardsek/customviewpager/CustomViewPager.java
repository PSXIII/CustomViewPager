package com.example.chococardsek.customviewpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chococardsek.customviewpager.databinding.CustomViewpagerBinding;
import com.example.chococardsek.customviewpager.databinding.ItemPagerBinding;
import com.jakewharton.rxbinding2.support.v4.view.RxViewPager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.databinding.DataBindingUtil.bind;

public class CustomViewPager extends FrameLayout {

    private int mCurrentPosition = 0;
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

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstance();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        mAdapter = new CustomViewPagerAdapter();
        mBinding.viewPager.setAdapter(mAdapter);
        setUIPagerIndicator();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (delay == 0)
            return super.dispatchTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                stopSlide();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                runSlide(delay);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setupCustomViewPager(ArrayList<String> resources) {
        mAdapter.setResources(resources);
        setUIPagerIndicator();

        RxViewPager.pageSelections(mBinding.viewPager)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mCurrentPosition = integer;
                        //loop set active indicator
                        for (int index = 0; index < mBinding.pagerIndicator.getChildCount(); index++) {
                            ImageView view = (ImageView) mBinding.pagerIndicator.getChildAt(index);
                            view.setImageResource(index == integer ? R.drawable.shape_dot_selected : R.drawable.shape_dot);
                        }
                    }
                });

    }

    private void setUIPagerIndicator() {
        mBinding.pagerIndicator.removeAllViews();//remove all indicator
        //check size of content
        int count = mAdapter.getCount();
        if (count > 1) {
            mBinding.pagerIndicator.setVisibility(VISIBLE);

            //for loop create indicator
            for (int i = 0; i < count; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.shape_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(16, 0, 16, 0);
                mBinding.pagerIndicator.addView(imageView, params);
            }
            //set active indicator in position 0
            ImageView _1stImage = (ImageView) mBinding.pagerIndicator.getChildAt(0);
            _1stImage.setImageResource(R.drawable.shape_dot_selected);
        } else {
//            hide indicator layout
            mBinding.pagerIndicator.setVisibility(View.GONE);
        }
    }


    public void runSlide(int delay) {
        stopSlide();
        this.delay = delay;

        if (subscription == null || subscription.isDisposed())
            subscription = Observable.interval(delay, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            int count = mAdapter.getCount() - 1;
                            if (mCurrentPosition == count) {
                                mBinding.viewPager.setCurrentItem(mCurrentPosition, false);
                                mCurrentPosition = 0;
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

    private class CustomViewPagerAdapter extends PagerAdapter {

        private ArrayList<String> mResources;
        private ItemPagerBinding mBinding;

        public CustomViewPagerAdapter() {
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
            View itemView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_pager, container, false);

            mBinding = bind(itemView);

            GlideApp.with(container.getContext())
                    .load(mResources.get(position))
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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