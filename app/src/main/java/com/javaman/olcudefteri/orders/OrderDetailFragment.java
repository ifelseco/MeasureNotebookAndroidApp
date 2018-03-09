package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;
import com.javaman.olcudefteri.orders.presenter.OrdersPresenter;

import java.text.SimpleDateFormat;

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

    @BindView(R.id.tv_order_measure_date)
    TextView tvMeasureDate;

    @BindView(R.id.tv_order_delivery_date)
    TextView tvOrderDeliveryDate;

    @BindView(R.id.tv_order_mount_date)
    TextView tvOrderMountDate;

    @BindView(R.id.tv_order_username)
    TextView tvOrderUsername;

    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;

    @BindView(R.id.image_view_order_status)
    ImageView imageViewOrderStatus;

    @BindView(R.id.measure_date_layout)
    LinearLayout linearLayoutMeasureDate;

    @BindView(R.id.mount_date_layout)
    LinearLayout linearLayoutMountDate;

    @BindView(R.id.order_status_layout)
    LinearLayout linearLayoutOrderStatus;

    @BindView(R.id.linear_layout_header)
    LinearLayout linearLayoutHeader;


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
        ButterKnife.bind(this,rootView);
        initView();
        setView();
        return rootView;
    }

    public void initView(){
        linearLayoutMeasureDate.setVisibility(View.GONE);
        linearLayoutMountDate.setVisibility(View.GONE);
    }

    public void setView(){



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        if (orderDetailResponseModel != null) {
            tvOrderNo.setText(""+orderDetailResponseModel.getId());
            tvOrderUsername.setText(orderDetailResponseModel.getUserUsername());


            if (orderDetailResponseModel.getOrderStatus() == 0) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_gray);
                linearLayoutOrderStatus
                        .setBackgroundResource(R.drawable.rectangle_background_gray);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_gray);
                imageViewOrderStatus.setImageResource(R.drawable.ic_remove_circle_black_24dp);
                tvOrderStatus.setText("Eksik Sipariş");
            } else if (orderDetailResponseModel.getOrderStatus() == 1) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_accent);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_accent);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_accent);
                imageViewOrderStatus.setImageResource(R.drawable.ic_measure);
                tvOrderStatus.setText("Ölçüye gidilecek");
            } else if (orderDetailResponseModel.getOrderStatus() == 2) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_green);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_green);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_green);
                imageViewOrderStatus.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
                tvOrderStatus.setText("Sipariş kaydı alındı.");
            } else if (orderDetailResponseModel.getOrderStatus() == 3) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_pink);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_pink);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_pink);
                imageViewOrderStatus.setImageResource(R.drawable.ic_tailor);
                tvOrderStatus.setText("Sipariş terzide.");
            } else if (orderDetailResponseModel.getOrderStatus() == 4) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_purple);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_purple);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_purple);
                imageViewOrderStatus.setImageResource(R.drawable.ic_check_circle_black_24dp);
                tvOrderStatus.setText("Terzi işlemi bitti");
            } else if (orderDetailResponseModel.getOrderStatus() == 5) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_yellow);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_yellow);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_yellow);
                imageViewOrderStatus.setImageResource(R.drawable.ic_delivered);
                tvOrderStatus.setText("Sipariş teslim edildi");
            } else if (orderDetailResponseModel.getOrderStatus() == 6) {
                linearLayoutHeader.setBackgroundResource(R.drawable.rectangle_background_lime);
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_lime);
                imageViewOrderStatus.setBackgroundResource(R.drawable.rectangle_background_lime);
                imageViewOrderStatus.setImageResource(R.drawable.ic_local_offer_black_24dp);
                tvOrderStatus.setText("Sipariş teklifi.");
            }

            String orderDate = simpleDateFormat.format(orderDetailResponseModel.getOrderDate());
            tvOrderDate.setText(orderDate);

            if (orderDetailResponseModel.getDeliveryDate() != null && !orderDetailResponseModel.getDeliveryDate().equals("")) {
                String deliveryDate = simpleDateFormat.format(orderDetailResponseModel.getDeliveryDate());
                tvOrderDeliveryDate.setText(deliveryDate);
            }

            if (orderDetailResponseModel.getMountDate() != null && !orderDetailResponseModel.getMountDate().equals("")) {
                String mountDate = simpleDateFormat.format(orderDetailResponseModel.getMountDate());
                linearLayoutMountDate.setVisibility(View.VISIBLE);
                this.tvOrderMountDate.setText(mountDate);

            }

            if (orderDetailResponseModel.getMeasureDate() != null && !orderDetailResponseModel.getMeasureDate().equals("")) {
                String measureDate = simpleDateFormat.format(orderDetailResponseModel.getMeasureDate());
                linearLayoutMeasureDate.setVisibility(View.VISIBLE);
                this.tvMeasureDate.setText(measureDate);
            }




        }
    }

}
