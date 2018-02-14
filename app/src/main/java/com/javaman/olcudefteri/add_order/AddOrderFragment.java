package com.javaman.olcudefteri.add_order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.javaman.olcudefteri.R;

/**
 * Created by javaman on 14.12.2017.
 */

public class AddOrderFragment extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs" ;


    private boolean isRegisterCustomer=false;

    TextView tvTakeMeasurement;
    Button btnTakeMeasurement;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.add_order_layout,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Sipariş bilgilerini gir.");
        tvTakeMeasurement=view.findViewById(R.id.textViewTakeMeasurement);
        btnTakeMeasurement=view.findViewById(R.id.btnTakeMeasurement);

        btnTakeMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOrderLineFragment addOrderLineFragment =new AddOrderLineFragment();
                addOrderLineFragment.show(getFragmentManager(),"take measurement fragment");

            }
        });




    }

    public void getRegisterCustomerStatus(boolean value) {
        this.isRegisterCustomer=value;
        if(isRegisterCustomer){
            tvTakeMeasurement.setText("Kayıtlı Müşteri");
        }else{
            tvTakeMeasurement.setText("Müşteri Kaydı eksik");
        }
    }

    
}
