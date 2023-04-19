package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.google.android.material.imageview.ShapeableImageView;

public class ImageInnFragment extends Fragment {
    private static final String KEY_IMAGE = "KEY_IMAGE";
    private Context context;

    public static ImageInnFragment newInstance(Context context, String image) {
        ImageInnFragment fragment = new ImageInnFragment();
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE, image);

        fragment.setArguments(args);
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String image = getArguments().getString(KEY_IMAGE);

        View v= inflater.inflate(R.layout.list_image_inn, container, false);
        ShapeableImageView imageInn = (ShapeableImageView) v.findViewById(R.id.imageInn);

        Glide.with(context).load(Constant.ROOT_URL + "upload/" + image).into(imageInn);

        return v;
    }
}
