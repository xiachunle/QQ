package com.xiachunle.qq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiachunle.qq.R;
import com.xiachunle.qq.fragment.LaunchFirstFragment;
import com.xiachunle.qq.fragment.LaunchSecondFragment;
import com.xiachunle.qq.fragment.LaunchThirdFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiachunle on 2017/2/9.
 */

public class LaunchActivity extends BaseActivity {

    private ViewPager launchView;
    private LinearLayout mPoints;
    private List<Fragment> fragmentLists;
    private int lastPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        init();
    }

    private void init() {
        launchView = (ViewPager) findViewById(R.id.id_launch_view);
        mPoints = (LinearLayout) findViewById(R.id.id_point);
        fragmentLists = new ArrayList<>();
        fragmentLists.add(LaunchFirstFragment.getInstance());
        fragmentLists.add(LaunchSecondFragment.getInstance());
        fragmentLists.add(LaunchThirdFragment.getInstance());
        for (int i = 0; i < fragmentLists.size(); i++) {
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.launch_view_point);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.rightMargin=20;
            point.setLayoutParams(lp);
            mPoints.addView(point);
            if(i==0){
                point.setEnabled(true);
            }else {
                point.setEnabled(false);
            }
        }

        launchView.setAdapter(new MyLauchFragmentAdapater(getSupportFragmentManager(),fragmentLists));
        launchView.setCurrentItem(0);
        launchView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                position=position%fragmentLists.size();
                mPoints.getChildAt(position).setEnabled(true);
                mPoints.getChildAt(lastPosition).setEnabled(false);
                lastPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class  MyLauchFragmentAdapater extends FragmentPagerAdapter{
        private List<Fragment> fragments;

        public MyLauchFragmentAdapater(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
