package com.javaman.olcudefteri.tailor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TailorOrderFragment extends Fragment {

    OrderSummaryModel tailorOrders;
    private List<OrderDetailModel> mOrders=new ArrayList<>();
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.recycler_tailor)
    RecyclerView recyclerView;

    @BindView(R.id.tailor_content)
    View viewIncludeOrder;

    public TailorOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey(TailorHomeActivity.ARG_TAILOR_ORDERS)){
                tailorOrders =bundle.getParcelable(TailorHomeActivity.ARG_TAILOR_ORDERS);
                mOrders= tailorOrders.getOrders();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tailor_order, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {

        if(mOrders.size()>0){
            viewIncludeOrder.setVisibility(View.VISIBLE);
            adapter=new TailorOrderAdapter(mOrders,getActivity(),TailorOrderFragment.this);
            recyclerView.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

        }
    }


}
