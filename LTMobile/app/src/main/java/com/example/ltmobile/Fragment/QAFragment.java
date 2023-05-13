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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.QuestionAdapter;
import com.example.ltmobile.Model.Question;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QAFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QAFragment extends Fragment {

    Context context;
    private RecyclerView rcvQuestion;
    private ImageView imageView;
    private TextInputEditText etKeyword;
    private TextView headline;
    private ImageView ivSearch;
    private QuestionAdapter questionAdapter;
    List<Question> questions = new ArrayList<>();

    public QAFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QAFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QAFragment newInstance(String param1, String param2) {
        QAFragment fragment = new QAFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_a, container, false);
        anhXa(view);
        getData();
        addQuestions();

        questionAdapter = new QuestionAdapter(context, questions);
        rcvQuestion.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        rcvQuestion.setLayoutManager(linearLayoutManager);
        rcvQuestion.setAdapter(questionAdapter);
        questionAdapter.notifyDataSetChanged();
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

    private void getData() {
        User user = SharedPrefManager.getInstance(context).getUser();
        headline.setText("Hi! " + user.getFullname());
        Glide.with(context).load(user.getAvatar()).into(imageView);
    }

    public void anhXa(View view) {

        rcvQuestion = (RecyclerView) view.findViewById(R.id.rcv_question);
        headline = (TextView) view.findViewById(R.id.headline);
        imageView = (ImageView) view.findViewById(R.id.imageView);
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
                            if (!jsonObject.isNull("answererId")) {
                                JSONObject answererUser = jsonObject.getJSONObject("answererId");
                                answererId = answererUser.getInt("userId");
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

//                            ImageInn imageInn = new ImageInn(mainImage.getInt("imageInnId"), mainImage.getString("image"));

                            questions.add(new Question(id, createdAt, updatedAt, title, avatarAsked, askedId, answererId, askedFullname));

                            questionAdapter.notifyDataSetChanged();
//                            fragments.add(InnFragment.newInstance(getApplicationContext(), describe, String.valueOf(price), imageInn.getImage(), String.valueOf(priceWater), String.valueOf(priceELec)));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
//                    innId = inns.get(0).getInnId();
//                    des = inns.get(0).getDescribe();
//                    innAdapter = new InnAdapter(ListInnActivity.this, fragments);
//                    viewListInn.setAdapter(innAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (questionAdapter != null) {
            questionAdapter.release();
        }
    }
}