package com.example.chococardsek.customviewpager;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chococardsek.customviewpager.databinding.ItemPagerBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class CustomViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mResources;
    private ItemPagerBinding mBinding;

    private SlideClickedListener slideClickedListener;

    public CustomViewPagerAdapter(Context mContext, ArrayList<String> mResources, SlideClickedListener slideClickedListener) {
        this.mContext = mContext;
        this.mResources = mResources;
        this.slideClickedListener = slideClickedListener;
    }

    @Override
    public int getCount() {
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

        Picasso.with(mContext)
                .load(mResources.get(position))
                .into(mBinding.imgPagerItem);

        mBinding.imgPagerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideClickedListener.onSlideClickedListener(position);
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    interface SlideClickedListener {
        void onSlideClickedListener(int position);
    }
}