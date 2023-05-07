package com.example.ltmobile.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ltmobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionFragment extends Fragment {
    private static final String INN_DES = "";
    private String des;
    TextView descriptiontxt;
    public static DescriptionFragment newInstance(String des) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(INN_DES, des);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            des = getArguments().getString(INN_DES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        descriptiontxt = (TextView) view.findViewById(R.id.descriptiontxt);

        descriptiontxt.setText(des);
        return view;
    }
}