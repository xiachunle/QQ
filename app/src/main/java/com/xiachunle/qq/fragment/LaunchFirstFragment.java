package com.xiachunle.qq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiachunle.qq.R;

/**
 * Created by xiachunle on 2017/2/9.
 */

public class LaunchFirstFragment extends BaseFragment {

    public static LaunchFirstFragment getInstance(){
        return new LaunchFirstFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__launch_first,container,false);
    }
}
