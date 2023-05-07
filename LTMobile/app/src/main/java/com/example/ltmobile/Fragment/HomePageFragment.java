package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.R;


public class HomePageFragment extends Fragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_page, container, false);

        Button btn = view.findViewById(R.id.btnTest);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /// khi fragment thêm vào thì lưu biến context của activity gắn nó
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /// khi fragment được gỡ ra thì xóa context
    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}