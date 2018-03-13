package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 08.03.2018.
 */

public class OrderLineFragment extends Fragment {

    public OrderLineFragment() {
    }

    @BindView(R.id.recycle_order_line) RecyclerView recyclerViewOrderLine;

    @BindView(R.id.swipe_refres_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<OrderLineDetailModel> orderLines=new ArrayList<>();

    RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(OrderDetailActivity.ARG_ORDER_LINES)) {
            orderLines = getArguments().getParcelableArrayList(OrderDetailActivity.ARG_ORDER_LINES);
            adapter=new OrderLineAdapter(orderLines,getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_line_fragment, container, false);
        ButterKnife.bind(this,rootView);

        //OrderLineAdapter orderLineAdapter=new OrderLineAdapter(orderLines,getActivity());
        recyclerViewOrderLine.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerViewOrderLine.setLayoutManager(layoutManager);
        return rootView;
    }
}
