package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.AskAndAnswerAdapter;
import com.example.ltmobile.Adapter.QuestionAdapter;
import com.example.ltmobile.Model.CommentInn;
import com.example.ltmobile.Model.Messages;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskAndAnswerActivity extends AppCompatActivity {

    private Context context = this;
    private EditText getMessage;
    private ImageButton sendMessageButton;
    private TextView nameOfUser, headline, tvtitle;
    private ImageView imageView;

    private String enteredMessage;
    Intent intent;
    String receiverName, senderName;
    int receiverId, senderId;
    RecyclerView messageRecyclerView;
    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    List<Messages> messages = new ArrayList<>();

    AskAndAnswerAdapter askAndAnswerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_and_answer);

        anhXa();
        getData();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Question question = (Question) bundle.get("object_question");
        int questionId = question.getQuestionId();
        String title = question.getTitle();
        tvtitle.setText("Tiêu đề câu hỏi: "+title);
        senderName = question.getAskedFullname();
        receiverName = question.getAnsweredFullname();
        senderId = question.getAskedId();
        receiverId = question.getAnswererId();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        loadMessages(questionId);
        askAndAnswerAdapter = new AskAndAnswerAdapter(context, messages);
        messageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(askAndAnswerAdapter);
        askAndAnswerAdapter.notifyDataSetChanged();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredMessage = getMessage.getText().toString();
                if (enteredMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tin nhắn!", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        addMessage(senderId, receiverId, enteredMessage, questionId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
//                    Date date = new Date();
//                    currentTime = simpleDateFormat.format(calendar.getTime());
//                    try{
//                        date = simpleDateFormat.parse(currentTime);
//                        User user = SharedPrefManager.getInstance(context).getUser();
//                        if((user.getUserId() == senderId) || (user.getUserId() == receiverId)){
//                            int userId = user.getUserId();
//                            String image="";
//                            Messages message = new Messages(enteredMessage,image,date,date,userId,questionId);
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }

    private void getData() {
        User user = SharedPrefManager.getInstance(context).getUser();
        headline.setText("Hi! " + user.getFullname());
//        Glide.with(context).load(user.getAvatar()).into(imageView);
    }

    public void anhXa() {

        getMessage = (EditText) findViewById(R.id.getMessage);
        sendMessageButton = (ImageButton) findViewById(R.id.imageviewsendmessage);
        headline = (TextView) findViewById(R.id.headline);
        tvtitle = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.imageView);
        messageRecyclerView =(RecyclerView) findViewById(R.id.rcv_askedUser);
        intent = getIntent();
//        rcvQuestion = (RecyclerView) view.findViewById(R.id.rcv_question);

    }

    public void loadMessages(int questionId){
        ServiceAPI.serviceapi.loadMessages(questionId).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String message = jsonObject.getString("message");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            JSONObject user = jsonObject.getJSONObject("userId");
                            int userId = 0;
                            String userAvatar = "";
                            String userFullname = "";
                            String userRole = "";
                            if (!jsonObject.isNull("userId")) {
                                JSONObject userObject = jsonObject.getJSONObject("userId");
                                userId = userObject.getInt("userId");
                                userRole = userObject.getString("role");
                                userFullname = userObject.getString("fullname");
                                userAvatar = userObject.getString("avatar");
                            }
                            Date createdAt = null;
                            Date updatedAt = null;
                            try {
                                createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAtString);
                                updatedAt = new SimpleDateFormat("yyyy-MM-dd").parse(updatedAtString);
                            } catch (ParseException e) {
                                Log.e("TAG", e.toString());
                            }
                            messages.add( new Messages(message, createdAt, updatedAt, userId, userFullname, userAvatar));
                            askAndAnswerAdapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getApplicationContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addMessage(int senderId, int receiverId, String enteredMessage, int questionIdTest) throws JSONException {
        User user = SharedPrefManager.getInstance(context).getUser();

        if ((user.getUserId() == senderId) || (user.getUserId() == receiverId)) {


            int userIdTest = user.getUserId();
            ServiceAPI.serviceapi.addMessage(enteredMessage, userIdTest, questionIdTest).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
//                            Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                            int id = jsonObject.getInt("messageId");
//                            String message = jsonObject.getString("message");
//                            String image = jsonObject.getString("image");
//                            JSONObject userId = jsonObject.getJSONObject("userId");
//                            JSONObject questionId = jsonObject.getJSONObject("questionId");
//                            String avatar = jsonObject.getString("avatar");
//                            String createdAtString = jsonObject.getString("createdAt");
//                            String updatedAtString = jsonObject.getString("updatedAt");
//                            Date createdAt = null;
//                            Date updatedAt = null;
//                            try {
//                                createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(createdAtString);
//                                updatedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(updatedAtString);
//                            } catch (ParseException e) {
//                                Log.e("TAG", e.toString());
//                            }

//                            commentInnAdapter.addComment(new CommentInn(id, content, image, createdAt, updatedAt, username, avatar));
//                            commentInnLayout.scrollToPosition(commentInnAdapter.getItemCount() - 1);
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "An error occur" + response.code(), Toast.LENGTH_SHORT).show();

                    }
//                    rvText.setText("");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("TAG", t.toString());
                    Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}