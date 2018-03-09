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

public class OrderLineFragment extends Fragment {

    public OrderLineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_line_fragment, container, false);
        return rootView;
    }
}
