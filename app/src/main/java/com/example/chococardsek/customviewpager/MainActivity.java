package com.example.chococardsek.customviewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.chococardsek.customviewpager.databinding.ActivityMainBinding;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mImageResources;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding();
    }

    public void binding() {
        mImageResources = new ArrayList<>();
        mImageResources.add("https://www.w3schools.com/css/img_fjords.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_mountains.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_lights.jpg");
        mImageResources.add("https://www.w3schools.com/css/img_forest.jpg");
        mImageResources.add("http://dreamicus.com/data/image/image-06.jpg");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this,
                mImageResources,
                new RecyclerViewAdapter.SlideClickListener() {
                    @Override
                    public void onSlideClickListener(String imagePath) {
                        Toast.makeText(MainActivity.this, "ImagePath: " + imagePath, Toast.LENGTH_SHORT).show();
                    }
                },
                new RecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        Toast.makeText(MainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                    }
                });
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setAdapter(recyclerViewAdapter);
    }
}
