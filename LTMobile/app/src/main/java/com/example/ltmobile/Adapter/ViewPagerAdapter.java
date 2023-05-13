package com.example.ltmobile.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ltmobile.Fragment.ManagerAccountFragment;
import com.example.ltmobile.Fragment.ManagerInnsFragment;
import com.example.ltmobile.Fragment.ProfileDetail_InnTabFragment;
import com.example.ltmobile.Fragment.ProfileDetail_QuestionTabFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int userId;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, int userId) {
        super(fm, behavior);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProfileDetail_QuestionTabFragment();
            case 1:
                return new ProfileDetail_InnTabFragment(userId);
            default:
                return new ManagerAccountFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position){
            case 0:
                title = "Câu hỏi";
                break;
            case 1:
                title = "Bài đăng";
                break;
            default:
                title = "Home";
                break;
        }
        return title;
    }
}
