package com.example.ltmobile.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ltmobile.Fragment.InnFragment;

import java.util.List;

public class InnAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;
    public InnAdapter(FragmentActivity fa, List<Fragment> fragments)
    {
        super(fa);
        this.fragments = fragments;
    }
    @Override
    public Fragment createFragment(int position) {
        return this.fragments.get(position);
    }



    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
