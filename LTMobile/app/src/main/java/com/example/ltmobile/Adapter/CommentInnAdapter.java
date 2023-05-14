package com.example.ltmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Model.CommentInn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentInnAdapter extends RecyclerView.Adapter<CommentInnAdapter.CommentInnHolder>{
    private Context context;
    private LayoutInflater layout;
    private final List<CommentInn> commentInns;

    public CommentInnAdapter(Context context, List<CommentInn> commentInns) {
        this.context = context;
        this.layout = LayoutInflater.from(context);
        this.commentInns = commentInns;
    }

    @Override
    public CommentInnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layout.inflate(R.layout.comment_layout, parent, false);
        return new CommentInnHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentInnHolder holder, int position) {
        CommentInn cmt = commentInns.get(position);

        holder.user_name.setText(cmt.getUsername());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.comment_date.setText(sdf.format(cmt.getUpdatedAt()));
        holder.comment_content.setText(cmt.getContent());

        Glide.with(context)
                .load(Constant.ROOT_URL + "upload/" + cmt.getAvatar())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return commentInns.size();
    }

    public void addComment(CommentInn commentInn) {
        commentInns.add(commentInn);
        notifyDataSetChanged();
    }

    class CommentInnHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView imageView;
        private TextView user_name;
        private TextView comment_date;
        private TextView comment_content;

        public CommentInnHolder(View itemView)
        {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            comment_date = (TextView) itemView.findViewById(R.id.comment_date);
            comment_content = (TextView) itemView.findViewById(R.id.comment_content);
        }
    }
}
