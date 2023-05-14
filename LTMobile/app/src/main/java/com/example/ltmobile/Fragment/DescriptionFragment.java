package com.example.ltmobile.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.R;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionFragment extends Fragment {
    private static final String INN_DES = "";
    private static final String INN_proposed = "";
    private static final String INN_createDate = "";
    private static final String INN_address = "";
    private static final String INN_person = "";
    private static final String INN_elec_price = "";
    private static final String INN_water_price = "";
    private String des, proposed, createDate, address, person, elec_price, water_price;
    TextView descriptiontxt, txtproposed, txtcreateDate, txtaddress, txtperson, txtelec_price, txtwater_price;
    public static DescriptionFragment newInstance(Inn inn) {
        DescriptionFragment fragment = new DescriptionFragment();
        fragment.des = inn.getDescribe();
        fragment.proposed = inn.getProposed();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        fragment.createDate = sdf.format(inn.getUpdatedAt());
        fragment.address = inn.getAddress();
        fragment.person = String.valueOf(inn.getSize());
        fragment.water_price = String.valueOf(inn.getPriceWater().intValue());
        fragment.elec_price = String.valueOf(inn.getPriceELec().intValue());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        descriptiontxt = (TextView) view.findViewById(R.id.descriptiontxt);
        txtproposed = (TextView) view.findViewById(R.id.txtproposed);
        txtcreateDate = (TextView) view.findViewById(R.id.txtcreateDate);
        txtaddress = (TextView) view.findViewById(R.id.txtaddress);
        txtperson = (TextView) view.findViewById(R.id.txtperson);
        txtelec_price = (TextView) view.findViewById(R.id.txtelec_price);
        txtwater_price = (TextView) view.findViewById(R.id.txtwater_price);

        descriptiontxt.setText(des);
        txtproposed.setText(proposed);
        txtcreateDate.setText(createDate);
        txtaddress.setText(address);
        txtperson.setText(person);
        txtelec_price.setText(elec_price);
        txtwater_price.setText(water_price);
        return view;
    }
}