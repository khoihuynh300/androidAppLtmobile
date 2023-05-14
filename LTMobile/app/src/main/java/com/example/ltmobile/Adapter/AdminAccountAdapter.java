package com.example.ltmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Activity.AdminUserDetailActivity;
import com.example.ltmobile.Activity.UserDetailActivity;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;

import java.util.List;

public class AdminAccountAdapter extends RecyclerView.Adapter<AdminAccountAdapter.MyViewholder>{
    Context context;
    List<User> array;

    public AdminAccountAdapter(Context context, List<User> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public AdminAccountAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user, null);
        return new AdminAccountAdapter.MyViewholder(view);
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        public TextView tvEmail;
        public TextView tvFullName;
        public TextView tvGender;
        public ImageView imvAvatar;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvGender = itemView.findViewById(R.id.tvGender);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdminUserDetailActivity.class);
                    intent.putExtra(AdminUserDetailActivity.KEY_EXTRA_USER, getAdapterPosition());
                    context.startActivity(intent);
//                    int userID = 2;
//
//                    Intent intent = new Intent(context, UserDetailActivity.class);
//                    intent.putExtra(UserDetailActivity.KEY_EXTRA_USER, userID);
//                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAccountAdapter.MyViewholder holder, int position) {
        User user = array.get(position);
        holder.tvEmail.setText(user.getEmail());
        holder.tvFullName.setText(user.getFullname());
        holder.tvGender.setText(user.getGender());

        Glide.with(context).load(user.getAvatar()).into(holder.imvAvatar);
    }

    @Override
    public int getItemCount() {
        return array == null? 0 :array.size();
    }
}
