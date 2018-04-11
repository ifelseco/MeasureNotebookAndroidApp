package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderDetailFragment extends Fragment {

    private OrderDetailResponseModel orderDetailResponseModel;
    private Long orderId;
    private OrdersPresenter mOrdersPresenter;

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


        if (getArguments().containsKey(OrderDetailActivity.ARG_CURRENT_ORDER)) {
            orderDetailResponseModel = getArguments().getParcelable(OrderDetailActivity.ARG_CURRENT_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setView();
        return rootView;
    }


    public void setView() {


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


        tvOrderNo.setText("" + orderDetailResponseModel.getId());
        tvOrderDate.setText(orderDate);
        tvOrderTime.setText("" + hours + ":" + minutes);
        tvOrderUsername.setText(orderDetailResponseModel.getUserUsername());

        if (orderDetailResponseModel.getMeasureDate() != null && !orderDetailResponseModel.getMeasureDate().equals("")) {
            linearLayoutMeasureDate.setVisibility(View.VISIBLE);
            String measureDate=simpleDateFormat.format(orderDetailResponseModel.getMeasureDate());
            tvMeasureDate.setText(measureDate);
        }

        if(orderDetailResponseModel.isMountExist()){
            linearLayoutMountDate.setVisibility(View.VISIBLE);
            checkBoxIsMount.setChecked(true);

        }

        Currency currency=Currency.getInstance(new Locale("tr","TR"));


        tvOrderTotalAmount.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderDetailResponseModel.getTotalAmount()));
        tvOrderDeposit.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderDetailResponseModel.getDepositeAmount()));

        double remainAmount=orderDetailResponseModel.getTotalAmount()-orderDetailResponseModel.getDepositeAmount();

        tvOrderRemain.setText(""+currency.getSymbol()+" "+String.format("%.2f",remainAmount));





    }
}


