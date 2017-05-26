package com.example.chococardsek.customviewpager;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chococardsek.customviewpager.databinding.ItemCustomViewpagerBinding;

import java.util.ArrayList;

public class CustomViewPagerHolder extends RecyclerView.ViewHolder {

    private ItemCustomViewpagerBinding mBinding;

    public CustomViewPagerHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public void binding(ArrayList<String> mResources) {
        mBinding.itemCustomViewPager.setupCustomViewPager(mResources);
    }

    public void setSlideItemOnClickListener(CustomViewPager.SlideClickedListener listener) {
        mBinding.itemCustomViewPager.setSlideClickedListener(listener);
    }

    public void run(int delay) {
        mBinding.itemCustomViewPager.runSlide(delay);
    }
}
