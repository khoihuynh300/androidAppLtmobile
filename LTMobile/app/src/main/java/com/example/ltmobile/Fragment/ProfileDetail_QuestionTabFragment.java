package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ltmobile.Adapter.QuestionAdapter;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ItemMarginDecoration;
import com.example.ltmobile.Utils.ServiceAPI;
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

public class ProfileDetail_QuestionTabFragment extends Fragment {
    Context context;

    int userId;

    //RecyclerView
    List<Question> questionList;
    RecyclerView rvQuestions;
    QuestionAdapter questionAdapter;

    public ProfileDetail_QuestionTabFragment(int userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_detail__question_tab, container, false);
        questionList = new ArrayList<>();
        rvQuestions = view.findViewById(R.id.rvQuestions);

        questionAdapter = new QuestionAdapter(context, questionList);
        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(30);
        rvQuestions.addItemDecoration(itemMarginDecoration);
        rvQuestions.setAdapter(questionAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvQuestions.setLayoutManager(linearLayoutManager);

        ServiceAPI.serviceapi.findQuestionsByUserProposed(userId)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {


                            try {
                                JsonObject jsonObject1 = response.body();
                                JSONArray jsonArray = new JSONArray(jsonObject1.get("result").getAsJsonArray().toString());
//                                Log.e("TAG", "onResponse: " + response.body() );
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
                                        answeredFullname = askedUser.getString("fullname");
                                        String roleAnswered = answererUser.getString("role");
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
                                    questionList.add(new Question(id, createdAt, updatedAt, title, avatarAsked, askedId, answererId, askedFullname, answeredFullname));

                                    questionAdapter.notifyDataSetChanged();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("TAG", t.toString());
                        Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
                    }
                });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}