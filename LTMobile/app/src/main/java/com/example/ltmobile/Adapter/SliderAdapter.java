package com.example.ltmobile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderHolder> {
    private Context context;
    private List<ImageInn> arrayList;

    public SliderAdapter(Context context, List<ImageInn> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public SliderHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        return new SliderHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderHolder viewHolder, int position) {
//        Glide.with(context).load(arrayList.get(position)).into(viewHolder.imageView);
        Glide.with(context).load(Constant.ROOT_URL + "upload/" + arrayList.get(position).getImage()).into(viewHolder.imageView);
        String s = Constant.ROOT_URL + "upload/" + arrayList.get(position).getImage();;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public class SliderHolder extends SliderViewAdapter.ViewHolder {
        private ImageView imageView;
        public SliderHolder(View itemview) {
            super(itemview);
            imageView = itemview.findViewById(R.id.imgView);
        }
    }
}
