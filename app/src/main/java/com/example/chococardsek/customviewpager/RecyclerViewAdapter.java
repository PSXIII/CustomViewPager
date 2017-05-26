package com.example.chococardsek.customviewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mResources;

    private SlideClickListener slideClickListener;
    private ItemClickListener itemClickListener;

    public RecyclerViewAdapter(Context mContext,
                               ArrayList<String> mViewPagerResources,
                               SlideClickListener slideClickListener,
                               ItemClickListener itemClickListener) {
        this.mResources = mViewPagerResources;
        this.mContext = mContext;
        this.slideClickListener = slideClickListener;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View rootView;
        switch (viewType) {
            case 0:
                rootView = inflater.inflate(R.layout.item_custom_viewpager, viewGroup, false);
                return new CustomViewPagerHolder(rootView);
            case 1:
                rootView = inflater.inflate(R.layout.item_recycler_view, viewGroup, false);
                return new RecyclerViewHolder(rootView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case 0:
                CustomViewPagerHolder customViewPagerHolder = (CustomViewPagerHolder) viewHolder;
                customViewPagerHolder.binding(mResources);
                customViewPagerHolder.setSlideItemOnClickListener(new CustomViewPager.SlideClickedListener() {
                    @Override
                    public void onSlideClickedListener(String imagePath) {
                        slideClickListener.onSlideClickListener(imagePath);
                    }
                });
                customViewPagerHolder.run(5000);
                break;
            case 1:
                RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
                recyclerViewHolder.setItemText("Item position: " + position);
                recyclerViewHolder.setRecyclerItemOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClickListener(position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if (position == 0) viewType = 0;
        return viewType;
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    interface SlideClickListener {
        void onSlideClickListener(String imagePath);
    }

    interface ItemClickListener {
        void onItemClickListener(int position);
    }
}
