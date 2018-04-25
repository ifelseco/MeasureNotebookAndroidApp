package com.javaman.olcudefteri.tailor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.OrderAdapter;
import com.javaman.olcudefteri.orders.OrderDetailActivity;
import com.javaman.olcudefteri.orders.OrderUpdateDialog;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.OrderSummaryModel;
import com.javaman.olcudefteri.utill.MyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TailorOrderFragment extends Fragment {

    OrderSummaryModel tailorOrders;
    private List<OrderDetailModel> mOrders = new ArrayList<>();
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    private int status;

    @BindView(R.id.recycler_tailor)
    RecyclerView recyclerView;


    @BindView(R.id.linear_layout_zero_orders)
    LinearLayout linearLayoutZeroOrders;

    @BindView(R.id.tv_empty_message)
    TextView textViewEmptyMessage;


    public TailorOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSING)) {
                tailorOrders = bundle.getParcelable(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSING);
                mOrders = tailorOrders.getOrders();
                status = 3;

            } else if (bundle.containsKey(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSED)) {
                tailorOrders = bundle.getParcelable(TailorHomeActivity.ARG_TAILOR_ORDERS_PROCESSED);
                mOrders = tailorOrders.getOrders();
                status = 4;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tailor_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setHasOptionsMenu(true);
        if(status==3){
            textViewEmptyMessage.setText("Hiç devam eden sipariş yok");
        }else if(status==4){
            textViewEmptyMessage.setText("Hiç biten sipariş yok");

        }

        if (mOrders.size() > 0) {
            linearLayoutZeroOrders.setVisibility(View.GONE);
            adapter = new TailorOrderAdapter(mOrders, getActivity(), TailorOrderFragment.this);
            recyclerView.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

        } else {
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
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemLineRefresh, android.R.color.white);
        }

        if (menuItemLineLogout != null) {
            MyUtil.tintMenuIcon(getActivity().getApplicationContext(), menuItemLineLogout, android.R.color.white);
        }


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_refresh:
                if (status == 3) {
                    ((TailorHomeActivity) getActivity()).getProcessingOrderFragment();
                    return true;
                } else if (status == 4) {
                    ((TailorHomeActivity) getActivity()).getProcessedOrderFragment();
                    return true;
                }
            case R.id.item_logout:
                if (status == 3) {
                    ((TailorHomeActivity) getActivity()).logout();
                    return true;
                } else if (status == 4) {
                    ((TailorHomeActivity) getActivity()).logout();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void updateOrder(OrderDetailModel orderDetailModel) {
            showConfirmDialog(orderDetailModel);
    }

    private void showConfirmDialog(final OrderDetailModel orderDetailModel) {
        String message = "";
        if(orderDetailModel.getOrderStatus()==3){
            message="Terzi işlemi bittimi?";
        }else if(orderDetailModel.getOrderStatus()==4){
            message="Siparişi devam eden \nişlemlere eklemek üzeresiniz.";
        }
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText(message)
                .setConfirmText("Evet")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(orderDetailModel.getOrderStatus()==4){
                            orderDetailModel.setOrderStatus(3);
                        }else if(orderDetailModel.getOrderStatus()==3){
                            orderDetailModel.setOrderStatus(4);
                        }

                        EventBus.getDefault().post(orderDetailModel);
                        sDialog.dismissWithAnimation();
                    }
                })
                .showCancelButton(true)
                .setCancelText("Vazgeç!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    @Subscribe
    public void updateAdapter(OrderUpdateModel orderUpdateModel) {

        TailorOrderAdapter tailorOrderAdapter = (TailorOrderAdapter) adapter;
        tailorOrderAdapter.removeItemFromList(orderUpdateModel);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void setEmptyBackground(){
        recyclerView.setVisibility(View.GONE);
        linearLayoutZeroOrders.setVisibility(View.VISIBLE);
    }
}
