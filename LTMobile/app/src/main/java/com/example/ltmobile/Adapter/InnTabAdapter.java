package com.example.ltmobile.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ltmobile.Fragment.CommentFragment;
import com.example.ltmobile.Fragment.DescriptionFragment;

public class InnTabAdapter extends FragmentStateAdapter {
    private int innId;
    private String des;
    private Context context;

    public InnTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, @NonNull int innId, String des) {
        super(fragmentManager, lifecycle);
        this.innId = innId;
        this.des = des;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return CommentFragment.newInstance(innId);
        return DescriptionFragment.newInstance(des);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
