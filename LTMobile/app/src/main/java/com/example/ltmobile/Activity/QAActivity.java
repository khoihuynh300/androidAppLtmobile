package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.InnAdapter;
import com.example.ltmobile.Adapter.OnItemClickListener;
import com.example.ltmobile.Adapter.QuestionAdapter;
import com.example.ltmobile.Fragment.InnFragment;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QAActivity extends AppCompatActivity {

    Context context;
    private RecyclerView rcvQuestion;
    private ImageView imageView;
    private TextInputEditText etKeyword;
    private TextView headline;
    private ImageView ivSearch;
    private QuestionAdapter questionAdapter;
    List<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaactivity);

        context = this;
        anhXa();
        getData();
        addQuestions();

        questionAdapter = new QuestionAdapter(context, questions);
        rcvQuestion.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        rcvQuestion.setAdapter(questionAdapter);
        questionAdapter.notifyDataSetChanged();
//        questionAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(Question item) {
//                Toast.makeText(QAActivity.this, ""+item.getTitle(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void anhXa() {

        rcvQuestion = (RecyclerView) findViewById(R.id.rcv_question);
        headline = (TextView) findViewById(R.id.headline);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void getData() {
        User user = SharedPrefManager.getInstance(context).getUser();
        headline.setText("Hi! " + user.getFullname());
        Glide.with(context).load(user.getAvatar()).into(imageView);
    }

    private void addQuestions() {
        ServiceAPI.serviceapi.getAllQuestions().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("questionId");
                            String title = jsonObject.getString("title");
                            Double view = jsonObject.getDouble("view");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            JSONObject askedUser = jsonObject.getJSONObject("askedId");
                            int answererId = 0;
                            String avatarAnswered = "";
                            String answeredFullname = "";
                            if (!jsonObject.isNull("answererId")) {
                                JSONObject answererUser = jsonObject.getJSONObject("answererId");
                                answererId = answererUser.getInt("userId");
                                String roleAnswered = answererUser.getString("role");
                                answeredFullname = askedUser.getString("fullname");
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


                            questions.add(new Question(id, createdAt, updatedAt, title, avatarAsked, askedId, answererId, askedFullname, answeredFullname));

                            questionAdapter.notifyDataSetChanged();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionAdapter != null) {
            questionAdapter.release();
        }
    }
}