package com.example.chococardsek.customviewpager;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chococardsek.customviewpager.databinding.ItemRecyclerViewBinding;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private ItemRecyclerViewBinding mBinding;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public void setItemText(String text) {
        mBinding.tvItem.setText(text);
    }

    public void setRecyclerItemOnClickListener(View.OnClickListener listener) {
        mBinding.tvItem.setOnClickListener(listener);
    }
}
