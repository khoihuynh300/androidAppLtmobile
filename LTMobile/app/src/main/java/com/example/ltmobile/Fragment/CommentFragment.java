package com.example.ltmobile.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.Activity.InnDetailActivity;
import com.example.ltmobile.Activity.VerifyOTPActivity;
import com.example.ltmobile.Adapter.CommentInnAdapter;
import com.example.ltmobile.Adapter.InnAdapter;
import com.example.ltmobile.Model.CommentInn;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {

    private static final String INN_ID = "0";

    private int innId;
    RecyclerView commentInnLayout;
    CommentInnAdapter commentInnAdapter;
    List<CommentInn> commentInns = new ArrayList<>();
    EditText rvText;
    RelativeLayout send;
    TextView some_id;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(int innId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(INN_ID, innId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            innId = getArguments().getInt(INN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        commentInnLayout = (RecyclerView) view.findViewById(R.id.commentInnLayout);
        rvText = (EditText) view.findViewById(R.id.rvText);
        send = (RelativeLayout) view.findViewById(R.id.send);
        some_id = (TextView) getActivity().findViewById(R.id.some_id);

        getAllCommentOfInn(innId);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();
                } else {
                    addCommentOfInn();
                }
            }
        });

        return view;
    }

    private void getAllCommentOfInn(int innId) {
        ServiceAPI.serviceapi.getAllCommentOfInn(innId).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("commentInnId");
                            String content = jsonObject.getString("content");
                            String image = jsonObject.getString("image");
                            String username = jsonObject.getString("username");
                            String avatar = jsonObject.getString("avatar");
                            String createdAtString = jsonObject.getString("createdAt");
                            String updatedAtString = jsonObject.getString("updatedAt");
                            Date createdAt = null;
                            Date updatedAt = null;
                            try{
                                createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(createdAtString);
                                updatedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(updatedAtString);
                            } catch (ParseException e) {
                                Log.e("TAG", e.toString());
                            }

                            commentInns.add(new CommentInn(id, content, image, createdAt, updatedAt, username, avatar));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    commentInnAdapter = new CommentInnAdapter(getContext(), commentInns);
                    if(commentInns.isEmpty())
                        some_id.setText("0");
                    else
                        some_id.setText(String.valueOf(commentInnAdapter.getItemCount()));
                    commentInnLayout.setAdapter(commentInnAdapter);
                    commentInnLayout.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    commentInnLayout.scrollToPosition(commentInnAdapter.getItemCount() - 1);
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCommentOfInn() {
        CommentInn commentInn = new CommentInn(rvText.getText().toString(), 1, innId);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), rvText.getText().toString());
        RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(innId));
        RequestBody tinnId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(1));
        ServiceAPI.serviceapi.createCommentOfInn(commentInn).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        int id = jsonObject.getInt("commentInnId");
                        String content = jsonObject.getString("content");
                        String image = jsonObject.getString("image");
                        String username = jsonObject.getString("username");
                        String avatar = jsonObject.getString("avatar");
                        String createdAtString = jsonObject.getString("createdAt");
                        String updatedAtString = jsonObject.getString("updatedAt");
                        Date createdAt = null;
                        Date updatedAt = null;
                        try{
                            createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(createdAtString);
                            updatedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(updatedAtString);
                        } catch (ParseException e) {
                            Log.e("TAG", e.toString());
                        }

                        commentInnAdapter.addComment(new CommentInn(id, content, image, createdAt, updatedAt, username, avatar));
                        commentInnLayout.scrollToPosition(commentInnAdapter.getItemCount() - 1);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                rvText.setText("");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}