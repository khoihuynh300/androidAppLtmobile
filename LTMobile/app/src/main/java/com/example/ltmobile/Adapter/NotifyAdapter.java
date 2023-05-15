package com.example.ltmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltmobile.Activity.AskAndAnswerActivity;
import com.example.ltmobile.Activity.InnDetailActivity;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.Model.Notify;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewholder>{

    Context context;
    List<Notify> array;

    public NotifyAdapter(Context context, List<Notify> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public NotifyAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notify, null);
        return new NotifyAdapter.MyViewholder(view);
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        public TextView tvNot;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvNot = itemView.findViewById(R.id.tvNot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notify notify = array.get(getAdapterPosition());
                    String link = notify.getLink();

                    String[] parts = link.split("/");
                    String code = parts[0];
                    String id = parts[1];

                    Log.e("TAG", "onClick: " + code +" ;" + id);
                    if(code.equals("qes")){

                        //get Question by id
                        ServiceAPI.serviceapi.getQuestionById(Integer.parseInt(id))
                                .enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                JsonObject jsonObject2 = response.body();

                                                if (!jsonObject2.get("error").getAsBoolean()) {

                                                    JSONObject responseJson = new JSONObject(response.body().toString());
                                                    JSONObject jsonObject = responseJson.getJSONObject("result");


                                                    int id = jsonObject.getInt("questionId");
                                                    String title = jsonObject.getString("title");
                                                    Double view = jsonObject.getDouble("view");
                                                    String createdAtString = jsonObject.getString("createdAt");
                                                    String updatedAtString = jsonObject.getString("updatedAt");
                                                    JSONObject askedUser = jsonObject.getJSONObject("askedId");
                                                    int answererId = 0;
                                                    String avatarAnswered = "";
                                                    String answeredFullname = "";
                                                    String roleAnswered = "";
                                                    if (!jsonObject.isNull("answererId")) {
                                                        JSONObject answererUser = jsonObject.getJSONObject("answererId");
                                                        answererId = answererUser.getInt("userId");
                                                        answeredFullname = askedUser.getString("fullname");
                                                        roleAnswered = answererUser.getString("role");
                                                        avatarAnswered = answererUser.getString("avatar");
                                                    }
                                                    String avatarAsked = "";
                                                    int askedId = askedUser.getInt("userId");
                                                    String askedFullname = askedUser.getString("fullname");
                                                    String roleAsked = askedUser.getString("role");
                                                    avatarAsked = askedUser.getString("avatar");
                                                    Date createdAt = null;
                                                    Date updatedAt = null;
                                                    try {
                                                        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                                                        updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                                                    } catch (ParseException e) {
                                                        Log.e("TAG", e.toString());
                                                    }

                                                    Question question = new Question(id, createdAt, updatedAt, title, avatarAsked, askedId, answererId, askedFullname, answeredFullname, roleAsked, roleAnswered);
                                                    onClickGoToMessage(question);
                                                }

                                            } catch (JSONException e) {
                                                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {

                                    }
                                });

                    }
                    else if(code.equals("inn")){
                        Intent intent = new Intent(context, InnDetailActivity.class);
                        intent.putExtra("innId", Integer.parseInt(id));
                        intent.putExtra("Describe", "des");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private void onClickGoToMessage(Question question) {
        Intent intent = new Intent(context, AskAndAnswerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_question", question);
        intent.putExtras(bundle);
//        askAndAnswerAdapter = new AskAndAnswerAdapter(question);
        context.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.MyViewholder holder, int position) {

        holder.tvNot.setText(array.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return array == null? 0 :array.size();
    }
}
