package com.javaman.olcudefteri.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.model.OrderSummaryModel;
import com.javaman.olcudefteri.presenter.ReportPresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.presenter.impl.ReportPresenterImpl;
import com.javaman.olcudefteri.ui.orders.OrderLineAdapter;
import com.javaman.olcudefteri.ui.orders.OrderLineFragment;
import com.javaman.olcudefteri.ui.tailor.TailorHomeActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.javaman.olcudefteri.view.ReportView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashboardOrderDialog extends DialogFragment implements View.OnClickListener,ReportView{

    @BindView(R.id.image_button_close)
    ImageButton btnClose;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.linear_layout_loading)
    LinearLayout linearLayoutLoading;

    @BindView(R.id.recycle_report)
    RecyclerView recyclerViewReport;

    private ReportPresenter mReportPresenter;
    SharedPreferenceHelper sharedPreferenceHelper;
    private OrderSummaryModel orderSummaryModel=new OrderSummaryModel();
    private int reportNumber;
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
        mReportPresenter=new ReportPresenterImpl(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dasboard_orders_layout, null);

        ButterKnife.bind(this, view);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewReport.setLayoutManager(layoutManager);
        recyclerViewReport.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        getReport();

        return view;
    }



    private void getReport() {
        Bundle bundle=getArguments();
        if(bundle!=null){
            if(bundle.containsKey(DashboardFragment.ARG_END_OF_DAY)){
                reportNumber=1;
                tvTitle.setText("Gün Sonu");
                getEndOfDay();
            }else if(bundle.containsKey(DashboardFragment.ARG_NEXT_DELIVERY)){
                reportNumber=2;
                tvTitle.setText("Yaklaşan Teslimat");
                nextDelivery();
            }else if(bundle.containsKey(DashboardFragment.ARG_NEXT_MEASURE)){
                reportNumber=3;
                tvTitle.setText("Yaklaşan Ölçü");
                nextMeasure();
            }else{
                dismiss();
            }
        }else{
            dismiss();
        }
    }

    @Override
    @OnClick(R.id.image_button_close)
    public void onClick(View v) {
        if(v.getId()==R.id.image_button_close){
            dismiss();
        }
    }

    @Override
    public void getEndOfDay() {
        String headerData=getSessionIdFromPref();
        mReportPresenter.getEndOfDay(headerData);
    }

    @Override
    public void nextMeasure() {
        String headerData=getSessionIdFromPref();
        mReportPresenter.getNextMeasure(headerData);
    }

    @Override
    public void nextDelivery() {
        String headerData=getSessionIdFromPref();
        mReportPresenter.getNextDelivery(headerData);
    }

    @Override
    public void getOrders(OrderSummaryModel orderSummaryModel) {
        this.orderSummaryModel=orderSummaryModel;
        setAdapter();


    }

    private void setAdapter() {
        adapter = new ReportAdapter(this.orderSummaryModel.getOrders(), getActivity(), DashboardOrderDialog.this);
        recyclerViewReport.setAdapter(adapter);
    }

    @Override
    public void logout() {

    }

    @Override
    public void checkSession() {

    }

    @Override
    public void navigateToLogin() {
        if(!getActivity().isFinishing()){
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void removeKeyFromPref(String key) {
        sharedPreferenceHelper.removeKey(key);
    }

    @Override
    public void showAlert(String message, boolean isToast) {
        if(!getActivity().isFinishing()){
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress(String message) {
        linearLayoutLoading.setVisibility(View.VISIBLE);
        recyclerViewReport.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        linearLayoutLoading.setVisibility(View.GONE);
        recyclerViewReport.setVisibility(View.VISIBLE);
    }

    public int getReportNumber() {
        return reportNumber;
    }
}
