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

public class InnFragment extends Fragment {
    private static final String KEY_DESC = "KEY_DESC";
    private static final String KEY_PRICE = "KEY_PRICE";
    private static final String KEY_IMAGE = "KEY_IMAGE";
    private Context context;

    public static InnFragment newInstance(Context context, String desc, String price, String image) {
        InnFragment fragment = new InnFragment();
        Bundle args = new Bundle();
        args.putString(KEY_DESC, desc);
        args.putString(KEY_PRICE, price);
        args.putString(KEY_IMAGE, image);

        fragment.setArguments(args);
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String desc = getArguments().getString(KEY_DESC);
        String price = getArguments().getString(KEY_PRICE);
        String image = getArguments().getString(KEY_IMAGE);

        View v= inflater.inflate(R.layout.list_inn, container, false);
        TextView txtcontent = (TextView) v.findViewById(R.id.txtcontent);
        TextView txtinn_price = (TextView) v.findViewById(R.id.txtinn_price);
        ShapeableImageView unsplash_fc = (ShapeableImageView)  v.findViewById(R.id.unsplash_fc);

        txtcontent.setText(desc);
        txtinn_price.setText(price);
        Glide.with(context).load(Constant.ROOT_URL + "upload/" + image).into(unsplash_fc);

        return v;
    }
}
