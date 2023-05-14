package com.example.ltmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Activity.InnDetailActivity;
import com.example.ltmobile.Activity.InnDetailAdminActivity;
import com.example.ltmobile.Activity.LoginActivity;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.example.ltmobile.Utils.MyClickListener;

import java.io.Serializable;
import java.util.List;

public class AdminInnAdapter extends RecyclerView.Adapter<AdminInnAdapter.MyViewholder> {
    Context context;
    List<Inn> array;

    private MyClickListener mListener;

    public AdminInnAdapter(Context context, List<Inn> array) {
        this.context = context;
        this.array = array;
    }

    public AdminInnAdapter(Context context, List<Inn> array, MyClickListener listener) {
        this.context = context;
        this.array = array;
        mListener = listener;
    }

    @NonNull
    @Override
    public AdminInnAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_inn, null);
        return new MyViewholder(view);
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        public TextView txtAddress;
        public TextView txtPhone;
        public TextView txtPrice;
        public ImageView imvInnImage;
        LinearLayout layoutConfirmed;
        LinearLayout layoutNotConfirmed;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imvInnImage = itemView.findViewById(R.id.imvInnImage);

            layoutConfirmed = itemView.findViewById(R.id.confirmed);
            layoutNotConfirmed = itemView.findViewById(R.id.notConfirmed);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
//                        mListener.onItemClick(getAdapterPosition());
//                        Intent intent = new Intent(context, InnDetailAdminActivity.class);
//                        intent.putExtra(InnDetailAdminActivity.KEY_EXTRA_INN, getAdapterPosition());
//                        context.startActivity(intent);

                        Inn inn = array.get(getAdapterPosition());
                        Intent intent = new Intent(context, InnDetailActivity.class);
                        intent.putExtra("innId", inn.getInnId());
                        intent.putExtra("Describe", inn.getDescribe());
                        context.startActivity(intent);
                    }
                    else{
//                    Inn inn = array.get(getAdapterPosition());
//
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(InnDetailAdminActivity.KEY_EXTRA_INN, inn);
                    Intent intent = new Intent(context, InnDetailAdminActivity.class);
//                    intent.putExtras(bundle);
                    intent.putExtra(InnDetailAdminActivity.KEY_EXTRA_INN, getAdapterPosition());
                    context.startActivity(intent);
                    }
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(@NonNull AdminInnAdapter.MyViewholder holder, int position) {
        holder.itemView.setBackground(new RoundedRectShape(10));
        holder.itemView.setClipToOutline(true);

        Inn inn = array.get(position);
        holder.txtAddress.setText(inn.getAddress());
        holder.txtPhone.setText(inn.getPhoneNumber());
        holder.txtPrice.setText(String.valueOf(inn.getPrice()));
        if (inn.isConfirmed()) {
            holder.layoutConfirmed.setVisibility(View.VISIBLE);
            holder.layoutNotConfirmed.setVisibility(View.GONE);
        } else {
            holder.layoutConfirmed.setVisibility(View.GONE);
            holder.layoutNotConfirmed.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(Constant.ROOT_URL + "upload/" + inn.getMainImage().getImage()).into(holder.imvInnImage);
    }

    @Override
    public int getItemCount() {
        return array == null? 0 :array.size();
    }

    public void addItem(Inn item) {
        array.add(item);
        notifyItemInserted(array.size() - 1);
    }

    public void removeItem(Inn item) {
        int index = array.indexOf(item);
        if (index < 0)
            return;
        array.remove(index);
        notifyItemRemoved(index);
    }

    private class RoundedRectShape extends ShapeDrawable {
        private final float radius;

        public RoundedRectShape(float radius) {
            this.radius = radius;
            setShape(new RoundRectShape(new float[] { radius, radius, radius, radius, radius, radius, radius, radius }, null, null));
            getPaint().setColor(Color.WHITE);
        }
    }

    public void setClickListener(MyClickListener mListener) {
        this.mListener = mListener;
    }
}
