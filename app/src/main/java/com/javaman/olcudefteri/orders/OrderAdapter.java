package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.response_model.OrderDetailResponseModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 13.01.2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<OrderDetailResponseModel> mOrders;
    OrdersActivity ordersActivity;
    LayoutInflater inflater;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private boolean mTwoPane;


    public OrderAdapter(Context context, List<OrderDetailResponseModel> mOrders,boolean mTwoPane) {
        Log.i("track", " in adapter constructor");
        //inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(context);
        ordersActivity = (OrdersActivity) context;
        this.mOrders = mOrders;
        this.mTwoPane=mTwoPane;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("track", " in onCreateViewHolder");
        MyViewHolder holder;
        View view = inflater.inflate(R.layout.order_item, parent, false);
        holder = new MyViewHolder(view, ordersActivity);

        return holder;
    }

    @Override
    public int getItemCount() {
        if (mOrders != null) {
            return mOrders.size();
        } else {
            return 0;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("track", " in onBindViewHoılder");
        OrderDetailResponseModel order = mOrders.get(position);
        holder.itemView.setTag(order);

        if (!ordersActivity.isActionModeActive) {
            holder.checkBoxSelectOrder.setVisibility(View.GONE);
            itemStateArray.clear();
        } else {
            holder.checkBoxSelectOrder.setVisibility(View.VISIBLE);
            holder.checkBoxSelectOrder.setChecked(false);
        }

        holder.bind(order, position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_order_number)
        TextView tvOrderNo;

        @BindView(R.id.tv_custmer_namesurname)
        TextView tvNameSurname;

        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;

        @BindView(R.id.tv_mount_date)
        TextView tvMountDate;

        @BindView(R.id.tv_measure_date)
        TextView tvMeasureDate;

        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;

        @BindView(R.id.tv_delivery_date)
        TextView tvDeliveryDate;

        @BindView(R.id.checkbox_select_order)
        CheckBox checkBoxSelectOrder;

        @BindView(R.id.linear_layout_measure_date)
        LinearLayout linearLayoutMeasureDate;

        @BindView(R.id.linear_layout_mount_date)
        LinearLayout linearLayoutMountDate;

        @BindView(R.id.card_order_item)
        CardView cardViewOrder;

        OrdersActivity ordersActivity;


        public MyViewHolder(View itemView, OrdersActivity ordersActivity) {


            super(itemView);
            Log.i("track", " in MyViewHolder constructor");
            ButterKnife.bind(this, itemView);
            checkBoxSelectOrder.setOnClickListener(this);
            cardViewOrder.setOnLongClickListener(ordersActivity);
            cardViewOrder.setOnClickListener(this);
            linearLayoutMeasureDate.setVisibility(View.GONE);
            linearLayoutMountDate.setVisibility(View.GONE);
            this.ordersActivity = ordersActivity;

        }

        public void bind(OrderDetailResponseModel order, int position) {
            Log.i("track", " in bind method");
            this.tvOrderNo.setText(String.valueOf(order.getId()));
            this.tvNameSurname.setText(order.getCustomer().getNameSurname());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String orderDate = simpleDateFormat.format(order.getOrderDate());
            this.tvOrderDate.setText(orderDate);
            Log.i("Item Array :", itemStateArray.toString());

            if (!itemStateArray.get(position, false)) {
                checkBoxSelectOrder.setChecked(false);
            } else {
                checkBoxSelectOrder.setChecked(true);
            }

            if (order.getDeliveryDate() != null && !order.getDeliveryDate().equals("")) {
                String deliveryDate = simpleDateFormat.format(order.getDeliveryDate());
                tvDeliveryDate.setText(deliveryDate);
            }

            if (order.getMountDate() != null && !order.getMountDate().equals("")) {
                String mountDate = simpleDateFormat.format(order.getMountDate());
                linearLayoutMountDate.setVisibility(View.VISIBLE);
                this.tvMountDate.setText(mountDate);

            }

            if (order.getMeasureDate() != null && !order.getMeasureDate().equals("")) {
                String measureDate = simpleDateFormat.format(order.getMeasureDate());
                linearLayoutMeasureDate.setVisibility(View.VISIBLE);
                this.tvMeasureDate.setText(measureDate);
            }


            //renklendirme işlemleri yapılacak...

            if (order.getOrderStatus() == 0) {
                this.tvOrderStatus.setText("Eksik Sipariş");
            } else if (order.getOrderStatus() == 1) {
                this.tvOrderStatus.setText("Ölçüye gidilecek");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (order.getOrderStatus() == 2) {
                this.tvOrderStatus.setText("Sipariş kaydı alındı.");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (order.getOrderStatus() == 3) {
                this.tvOrderStatus.setText("Sipariş terzide.");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (order.getOrderStatus() == 4) {
                this.tvOrderStatus.setText("Terzi işlemi bitti");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (order.getOrderStatus() == 5) {
                this.tvOrderStatus.setText("Sipariş teslim edildi");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            } else if (order.getOrderStatus() == 6) {
                this.tvOrderStatus.setText("Sipariş teklifi.");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            }
        }


        @Override
        public void onClick(View view) {

            if(view.getId()==R.id.checkbox_select_order){
                //checkbox click
                int adapterPosition = getAdapterPosition();
                if (!itemStateArray.get(adapterPosition, false)) {
                    checkBoxSelectOrder.setChecked(true);
                    itemStateArray.put(adapterPosition, true);
                    ordersActivity.prepareSelection(view, getAdapterPosition());
                } else {
                    checkBoxSelectOrder.setChecked(false);
                    itemStateArray.put(adapterPosition, false);
                    ordersActivity.prepareSelection(view, getAdapterPosition());
                }
            }else{
               OrderDetailResponseModel orderDetailResponseModel= (OrderDetailResponseModel) view.getTag();
                if(!ordersActivity.isActionModeActive){
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(OrderDetailFragment.ARG_ITEM_ID, orderDetailResponseModel.getId());
                        OrderDetailFragment fragment = new OrderDetailFragment();
                        fragment.setArguments(arguments);
                        ordersActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.order_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();

                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra(OrderDetailFragment.ARG_ITEM_ID , orderDetailResponseModel.getId());
                        context.startActivity(intent);
                    }
                }
            }


        }


    }

    public void deleteSelectedItems(ArrayList<OrderDetailResponseModel> selectedOrderList) {
        for (OrderDetailResponseModel orderDetailResponseModel : selectedOrderList){
            mOrders.remove(orderDetailResponseModel);
        }
        notifyDataSetChanged();
    }




}
