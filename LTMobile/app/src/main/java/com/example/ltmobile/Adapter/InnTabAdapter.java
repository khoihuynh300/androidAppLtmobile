package com.example.ltmobile.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ltmobile.Fragment.CommentFragment;
import com.example.ltmobile.Fragment.DescriptionFragment;

public class InnTabAdapter extends FragmentStateAdapter {

    public InnTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new CommentFragment();
        return new DescriptionFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
