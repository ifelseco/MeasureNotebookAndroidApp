package com.javaman.olcudefteri.ui.orders.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.ReportPresenter;
import com.javaman.olcudefteri.presenter.impl.ReportPresenterImpl;
import com.javaman.olcudefteri.ui.home.CustomersFragment;
import com.javaman.olcudefteri.ui.home.DashboardFragment;
import com.javaman.olcudefteri.ui.home.ReportAdapter;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.ui.orders.CustomerOrdersAdapter;
import com.javaman.olcudefteri.ui.orders.OrderAdapter;
import com.javaman.olcudefteri.ui.orders.OrderDetailActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.javaman.olcudefteri.view.ReportView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerOrdersDialog extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.image_button_close)
    ImageButton btnClose;

    @BindView(R.id.tv_order_count)
    TextView tvOrderCount;

    @BindView(R.id.recycle_customer_orders)
    RecyclerView recyclerVievCustomerOrders;

    SharedPreferenceHelper sharedPreferenceHelper;
    private OrderSummaryModel orderSummaryModel=new OrderSummaryModel();
    RecyclerView.Adapter adapter;

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey(CustomersFragment.ARG_CUSTOMER_ORDERS)){
                orderSummaryModel=bundle.getParcelable(CustomersFragment.ARG_CUSTOMER_ORDERS);

            }else{
                dismiss();
            }
        }else{
            dismiss();
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_orders_layout, null);

        ButterKnife.bind(this, view);
        if(orderSummaryModel.getOrders().size()>0){
            tvOrderCount.setText("Sipariş sayısı: "+orderSummaryModel.getOrders().size());
        }else{
            tvOrderCount.setText("Sipariş yok.");
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerVievCustomerOrders.setLayoutManager(layoutManager);
        setAdapter();
        return view;
    }




    @Override
    @OnClick(R.id.image_button_close)
    public void onClick(View v) {
        if(v.getId()==R.id.image_button_close){
            dismiss();
        }
    }




    private void setAdapter() {
        adapter = new CustomerOrdersAdapter(this.orderSummaryModel.getOrders(), getActivity(), CustomerOrdersDialog.this);
        recyclerVievCustomerOrders.setAdapter(adapter);
    }


    public void gotoOrderDetail(Long id) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ARG_CURRENT_ORDER , id);
        getActivity().startActivity(intent);
        dismiss();
    }
}
