package com.example.ltmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;

import java.util.List;

public class AdminInnAdapter extends RecyclerView.Adapter<AdminInnAdapter.MyViewholder> {
    Context context;
    List<Inn> array;


    public AdminInnAdapter(Context context, List<Inn> array) {
        this.context = context;
        this.array = array;
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

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtPrice = itemView.findViewById(R.id.txtPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "bật trang chi tiết" + getAdapterPosition(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(@NonNull AdminInnAdapter.MyViewholder holder, int position) {
        Inn inn = array.get(position);
        holder.txtAddress.setText(inn.getAddress());
        holder.txtPhone.setText(inn.getPhoneNumber());
        holder.txtPrice.setText(String.valueOf(inn.getPrice()));
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

}
