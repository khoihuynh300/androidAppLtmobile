package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ltmobile.Model.Question;
import com.example.ltmobile.R;

public class AskAndAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_and_answer);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Question question = (Question) bundle.get("object_question");
        TextView test = findViewById(R.id.textView3);
        test.setText(question.getAskedFullname());
    }
}