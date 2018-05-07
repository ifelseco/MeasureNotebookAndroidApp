package com.javaman.olcudefteri.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.login.LoginActivity;
import com.javaman.olcudefteri.event.OrderDeleteEvent;
import com.javaman.olcudefteri.event.OrderUpdateEvent;
import com.javaman.olcudefteri.model.OrderUpdateModel;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.presenter.OrderPresenter;
import com.javaman.olcudefteri.presenter.impl.OrderPresenterImpl;
import com.javaman.olcudefteri.view.OrderView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class OrderDetailFragment extends Fragment implements OrderView{

    private OrderDetailResponseModel orderDetailResponseModel;
    private Long orderId;
    private OrderPresenter mOrderPresenter;
    SweetAlertDialog pDialog;
    SharedPreferenceHelper sharedPreferenceHelper;
    String[] orderStatus;
    Currency currency=Currency.getInstance(new Locale("tr","TR"));

    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;

    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;

    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;

    @BindView(R.id.tv_order_measure_date)
    TextView tvMeasureDate;

    @BindView(R.id.tv_order_delivery_date)
    TextView tvOrderDeliveryDate;

    @BindView(R.id.tv_order_username)
    TextView tvOrderUsername;

    @BindView(R.id.tv_order_total_amount)
    TextView tvOrderTotalAmount;

    @BindView(R.id.tv_order_deposit)
    TextView tvOrderDeposit;

    @BindView(R.id.tv_order_remain)
    TextView tvOrderRemain;

    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

    @BindView(R.id.checkboxIsMount)
    CheckBox checkBoxIsMount;

    @BindView(R.id.measure_date_layout)
    LinearLayout linearLayoutMeasureDate;

    @BindView(R.id.linear_layout_order_mount)
    LinearLayout linearLayoutMountDate;


    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderPresenter=new OrderPresenterImpl(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());

        if (getArguments().containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)) {
            orderDetailResponseModel = getArguments().getParcelable(OrderDetailActivity.ARG_CURRENT_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        orderStatus=getActivity().getResources().getStringArray(R.array.order_status);
        setView();
        return rootView;
    }


    public void setView() {

        pDialog=new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        String orderDate = simpleDateFormat.format(orderDetailResponseModel.getOrderDate());
        if (orderDetailResponseModel.getDeliveryDate() != null && !orderDetailResponseModel.getDeliveryDate().equals("")) {
            String deliveryDate = simpleDateFormat.format(orderDetailResponseModel.getDeliveryDate());
            tvOrderDeliveryDate.setText(deliveryDate);
        }else{
            tvOrderDeliveryDate.setText("");
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderDetailResponseModel.getOrderDate());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String hoursString,minutesString;

        tvOrderNo.setText("" + orderDetailResponseModel.getId());
        tvOrderDate.setText(orderDate);
        if(hours<10){
            hoursString="0"+hours;
        }else{
            hoursString=""+hours;
        }

        if(minutes<10){
            minutesString="0"+minutes;
        }else{
            minutesString=""+minutes;
        }


        String fullTime=hoursString+":"+minutesString;

        tvOrderTime.setText(fullTime);
        tvOrderUsername.setText(orderDetailResponseModel.getUserNameSurname());
        tvOrderStatus.setText(orderStatus[orderDetailResponseModel.getOrderStatus()]);
        if (orderDetailResponseModel.getMeasureDate() != null && !orderDetailResponseModel.getMeasureDate().equals("")) {
            linearLayoutMeasureDate.setVisibility(View.VISIBLE);
            String measureDate=simpleDateFormat.format(orderDetailResponseModel.getMeasureDate());
            tvMeasureDate.setText(measureDate);
        }

        if(orderDetailResponseModel.isMountExist()){
            linearLayoutMountDate.setVisibility(View.VISIBLE);
            checkBoxIsMount.setChecked(true);

        }




        tvOrderTotalAmount.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderDetailResponseModel.getTotalAmount()));
        tvOrderDeposit.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderDetailResponseModel.getDepositeAmount()));

        double remainAmount=orderDetailResponseModel.getTotalAmount()-orderDetailResponseModel.getDepositeAmount();

        tvOrderRemain.setText(""+currency.getSymbol()+" "+String.format("%.2f",remainAmount));





    }




    @Override
    @Subscribe
    public void deleteOrder(OrderDeleteEvent orderDeleteEvent) {
        showConfirmDialog(orderDeleteEvent.getOrderId());
    }

    private void showConfirmDialog(final long orderId) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Onaylıyor musunuz?")
                .setContentText("Sipariş kalıcı olarak silinecek.")
                .setConfirmText("Evet")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        String headerData=getSessionIdFromPref();
                        mOrderPresenter.orderDelete(orderId,headerData);
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

    @Override
    @Subscribe
    public void updateOrder(OrderUpdateModel orderUpdateModel) {
        orderUpdateModel.setId(orderDetailResponseModel.getId());
        String headerData=getSessionIdFromPref();
        mOrderPresenter.orderUpdate(orderUpdateModel,headerData);
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void navigateToLogin() {

        startActivity(new Intent(getActivity() , LoginActivity.class));

    }

    @Override
    public void navigateToOrders() {
        startActivity(new Intent(getActivity() , OrdersActivity.class));
    }

    @Override
    public void checkSession() {

    }

    @Override
    public void showAlert(String message,boolean isError) {
        if(isError){

            SweetAlertDialog pDialog= new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("Hata...");
            pDialog.setContentText(message);
            pDialog.setConfirmText("Kapat");
            pDialog.setCancelable(true);
            pDialog.show();

        }else{
            SweetAlertDialog pDialog=new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText(message);
            pDialog.setConfirmText("Kapat");
            pDialog.setCancelable(true);
            pDialog.show();
        }
    }

    @Override
    public void updateView(OrderUpdateModel orderUpdateModel) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String mDeliveryDate, mMeasureDate;




        if(orderUpdateModel.getDeliveryDate()!=null){
            mDeliveryDate=sdf.format(orderUpdateModel.getDeliveryDate());
            tvOrderDeliveryDate.setText(mDeliveryDate);
        }else{
            mDeliveryDate="";
            tvOrderDeliveryDate.setText(mDeliveryDate);
        }


        if(orderUpdateModel.getMeasureDate()!=null){
            linearLayoutMeasureDate.setVisibility(View.VISIBLE);
            mMeasureDate=sdf.format(orderUpdateModel.getMeasureDate());
            tvMeasureDate.setText(mMeasureDate);

        }else{
            mMeasureDate="";
            tvMeasureDate.setText(mMeasureDate);
        }


        if(orderUpdateModel.getTotalAmount()==0){
            tvOrderTotalAmount.setText("0");
        }else{
            tvOrderTotalAmount.setText(""+orderUpdateModel.getTotalAmount());
        }


        if(orderUpdateModel.getDepositeAmount()==0){
            tvOrderDeposit.setText("0");
        }else{
            tvOrderDeposit.setText(""+orderUpdateModel.getDepositeAmount());
        }


        if(orderUpdateModel.isMountExist()){
            checkBoxIsMount.setChecked(true);
        }else{
            checkBoxIsMount.setChecked(false);
        }

        tvOrderStatus.setText(orderStatus[orderUpdateModel.getOrderStatus()]);

        double remainAmount=orderUpdateModel.getTotalAmount()-orderUpdateModel.getDepositeAmount();

        tvOrderRemain.setText(""+currency.getSymbol()+" "+String.format("%.2f",remainAmount));
        OrderUpdateEvent event=new OrderUpdateEvent();
        event.setOrderUpdateModel(orderUpdateModel);
        EventBus.getDefault().post(event);

    }

    @Override
    public void showProgress(String message) {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOrderPresenter.onDestroy();
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



}


