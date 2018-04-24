package com.javaman.olcudefteri.tailor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.OrderDetailActivity;
import com.javaman.olcudefteri.orders.OrderUpdateDialog;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;
import com.javaman.olcudefteri.utill.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TailorOrderFragment extends Fragment{

    OrderSummaryModel tailorOrders;
    private List<OrderDetailModel> mOrders=new ArrayList<>();
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    private int status;

    @BindView(R.id.recycler_tailor)
    RecyclerView recyclerView;



    @BindView(R.id.linear_layout_zero_orders)
    LinearLayout linearLayoutZeroOrders;


    public TailorOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSING)){
                tailorOrders =bundle.getParcelable(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSING);
                mOrders= tailorOrders.getOrders();
                status=3;
            }else if(bundle.containsKey(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSED)){
                tailorOrders =bundle.getParcelable(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSED);
                mOrders= tailorOrders.getOrders();
                status=4;
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
        setHasOptionsMenu(true);
        if(mOrders.size()>0){
            linearLayoutZeroOrders.setVisibility(View.GONE);
            adapter=new TailorOrderAdapter(mOrders,getActivity(),TailorOrderFragment.this);
            recyclerView.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

        }else{
            linearLayoutZeroOrders.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_tailor, menu);

        MenuItem menuItemLineRefresh = menu.findItem(R.id.item_refresh);
        MenuItem menuItemLineLogout = menu.findItem(R.id.item_logout);


        if (menuItemLineRefresh != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemLineRefresh,android.R.color.white);
        }

        if (menuItemLineLogout != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemLineLogout,android.R.color.white);
        }


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_refresh:
                if(status==3){
                    ((TailorHomeActivity)getActivity()).getProcessingOrderFragment();
                    return true;
                }else if(status==4){
                    ((TailorHomeActivity)getActivity()).getProcessedOrderFragment();
                    return true;
                }
            case R.id.item_logout:
                if(status==3){
                    ((TailorHomeActivity)getActivity()).logout();
                    return true;
                }else if(status==4){
                    ((TailorHomeActivity)getActivity()).logout();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
