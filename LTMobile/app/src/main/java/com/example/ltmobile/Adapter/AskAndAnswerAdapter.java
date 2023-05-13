package com.example.ltmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Model.Messages;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AskAndAnswerAdapter extends RecyclerView.Adapter<AskAndAnswerAdapter.AAAviewHolder> {

    private Context mContext;

    private List<Messages> mListMessages = new ArrayList<>();

    public AskAndAnswerAdapter(Context mContext, List<Messages> mListMessages) {
        this.mContext = mContext;
        this.mListMessages = mListMessages;
    }

    @NonNull
    @Override
    public AskAndAnswerAdapter.AAAviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiverchatlayout, null);
        return new AAAviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AAAviewHolder holder, int position) {
        final Messages message = mListMessages.get(position);
        if (message == null) {
            return;
        }
        holder.senderName.setText(message.getUsername());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.timeofmessage.setText(sdf.format(message.getUpdatedAt()));
        holder.sendermessage.setText(message.getMessage());
        Glide.with(mContext)
                .load(message.getUserAvatar())
                .into(holder.imgUser);
//        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickGoToMessage(question);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mListMessages == null ? 0 : mListMessages.size();
    }

    public class AAAviewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgUser;
        protected TextView senderName, sendermessage, timeofmessage;
        protected RelativeLayout layoutItem;

        public AAAviewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgUser = itemView.findViewById(R.id.img_user);
            this.layoutItem = itemView.findViewById(R.id.layoutformessage);
            this.senderName = itemView.findViewById(R.id.senderName);
            this.sendermessage = itemView.findViewById(R.id.sendermessage);
            this.timeofmessage = itemView.findViewById(R.id.timeofmessage);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mContext, AskAndAnswerActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("object_question", )
//                }
//            });
        }
    }
}


