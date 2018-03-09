package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javaman.olcudefteri.R;

/**
 * Created by javaman on 08.03.2018.
 */

public class CustomerDetailFragment extends Fragment {


    public CustomerDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_detail_fragment, container, false);
        return rootView;
    }
}
