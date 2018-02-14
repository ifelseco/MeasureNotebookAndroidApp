package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javaman on 13.01.2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder>{

    List<Order> mOrders;
    LayoutInflater inflater;

    public OrderAdapter(Context context , List<Order> mOrders){
        //inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);
        this.mOrders=mOrders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.order_item,parent,false);
         MyViewHolder holder=new MyViewHolder(view);
         return holder;
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
         Order order=mOrders.get(position);
         holder.setData(order,position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvOrderNo,tvNameSurname,tvMobilPhone,tvOrderStatus,tvOrderRemainDate;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvOrderNo=itemView.findViewById(R.id.tv_order_id);
            tvNameSurname=itemView.findViewById(R.id.tv_order_customer_name);
            tvMobilPhone=itemView.findViewById(R.id.tv_order_tel);
            tvOrderStatus=itemView.findViewById(R.id.tv_order_status);
            tvOrderRemainDate=itemView.findViewById(R.id.tv_order_remain_time);

        }

        public void setData(Order order, int position) {
            this.tvOrderNo.setText(String.valueOf(order.getId()));
            this.tvMobilPhone.setText(order.getCustomer().getMobilePhone());
            this.tvNameSurname.setText(order.getCustomer().getName()+" "+order.getCustomer().getSurname());

            if(order.getOrderStatus()==1){
                this.tvOrderStatus.setText("HAZIR");
            }else if(order.getOrderStatus()==2){
                this.tvOrderStatus.setText("TERZİDE");
                this.tvOrderStatus.setTextColor(Color.parseColor("#FF9800"));
            }else{
                this.tvOrderStatus.setText("TESLİM EDİLDİ");
                this.tvOrderStatus.setTextColor(Color.parseColor("#2196F3"));
            }
        }
    }
}
