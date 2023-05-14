package com.example.ltmobile.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ltmobile.Fragment.CommentFragment;
import com.example.ltmobile.Fragment.DescriptionFragment;
import com.example.ltmobile.Model.Inn;

public class InnTabAdapter extends FragmentStateAdapter {
    private Inn inn;
    private Context context;

    public InnTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Inn inn) {
        super(fragmentManager, lifecycle);
        this.inn = inn;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return CommentFragment.newInstance(inn.getInnId());
        return DescriptionFragment.newInstance(inn);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
