package com.example.ltmobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Activity.AskAndAnswerActivity;
import com.example.ltmobile.Model.CommentInn;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.example.ltmobile.Utils.ImagePicker;

import java.text.SimpleDateFormat;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionviewHolder> {

    private Context mContext;
    public static final String KEY_QuestionID = "QuestionID";

    private LayoutInflater layout;
    //    private OnItemClickListener onItemClickListener;
    private List<Question> mListQuestion;

    public QuestionAdapter(Context mContext, List<Question> mListQuestion) {
        this.mContext = mContext;
        this.mListQuestion = mListQuestion;
    }

    public void setData(List<Question> list) {
        this.mListQuestion = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, null);
        return new QuestionviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionviewHolder holder, int position) {
        final Question question = mListQuestion.get(position);
        if (question == null) {
            return;
        }
        holder.tvName.setText(question.getAskedFullname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.tvDate.setText(sdf.format(question.getUpdatedAt()));
        holder.tvTitle.setText(question.getTitle());
        Glide.with(mContext)
                .load(question.getAvatar())
                .into(holder.imgUser);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToMessage(question);
            }
        });
    }

    private void onClickGoToMessage(Question question) {
        Intent intent = new Intent(mContext, AskAndAnswerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_question", question);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public void release() {
        mContext = null;
    }

    @Override
    public int getItemCount() {
        return mListQuestion == null ? 0 : mListQuestion.size();
    }

    public class QuestionviewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgUser;
        protected TextView tvName, tvDate, tvTitle, tvQuestionId;
        protected RelativeLayout layoutItem;

        public QuestionviewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgUser = itemView.findViewById(R.id.img_user);
            this.layoutItem = itemView.findViewById(R.id.layout_item);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvDate = itemView.findViewById(R.id.tv_date);
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.tvQuestionId = itemView.findViewById(R.id.tv_questionId);
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
