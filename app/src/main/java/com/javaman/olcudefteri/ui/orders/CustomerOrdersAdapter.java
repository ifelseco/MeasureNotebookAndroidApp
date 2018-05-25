package com.javaman.olcudefteri.ui.orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.OrderDetailModel;
import com.javaman.olcudefteri.ui.orders.dialogs.CustomerOrdersDialog;
import com.javaman.olcudefteri.ui.orders.dialogs.DashboardOrderDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.CustomerOrderViewHolder>{


    private List<OrderDetailModel> mOrders=new ArrayList<>();
    Context mContext;
    CustomerOrdersDialog customerOrdersDialog;

    public CustomerOrdersAdapter(List<OrderDetailModel> mOrders, Context mContext, CustomerOrdersDialog customerOrdersDialog) {
        this.mOrders = mOrders;
        this.mContext = mContext;
        this.customerOrdersDialog = customerOrdersDialog;
    }

    @NonNull
    @Override
    public CustomerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_order_item, parent, false);
        return new CustomerOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderViewHolder holder, int position) {
        OrderDetailModel orderDetailModel = mOrders.get(position);
        holder.itemView.setTag(orderDetailModel);
        holder.bind(orderDetailModel,position);
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
    public int getItemCount() {
        if(mOrders!=null){
            return mOrders.size();
        }else{
            return 0;
        }

    }

    public class CustomerOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_order_number)
        TextView tvOrderNumber;



        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;

        @BindView(R.id.card_order_item)
        CardView cardViewCustomerOrder;




        public CustomerOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardViewCustomerOrder.setOnClickListener(this);
        }

        public void bind(OrderDetailModel orderDetailModel, int position) {

            if(orderDetailModel.getOrderNumber()!=null){
                if(!TextUtils.isEmpty(orderDetailModel.getOrderNumber())){
                    tvOrderNumber.setText(orderDetailModel.getOrderNumber());
                }
            }

            if(orderDetailModel.getOrderDate()!=null){
                tvOrderDate.setText(findFullDateText(orderDetailModel.getOrderDate()));
            }else{
                tvOrderDate.setText("");
            }

        }

        //Utill method
        public String findFullDateText(Date date){
            if(date!=null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                String hoursText,minutesText,timeText;

                String dateText=simpleDateFormat.format(date);

                if(hours<10){
                    hoursText="0"+hours;
                }else{
                    hoursText=String.valueOf(hours);
                }

                if(minutes<10){
                    minutesText="0"+hours;
                }else{
                    minutesText=String.valueOf(minutes);
                }

                timeText=hoursText+":"+minutesText;

                String fullDate=dateText+"  "+timeText;

                return fullDate;

            }else{
                return "";
            }
        }

        @Override
        public void onClick(View v) {

            OrderDetailModel orderDetailModel=(OrderDetailModel)itemView.getTag();

            if(v.getId()==R.id.card_order_item){
                customerOrdersDialog.gotoOrderDetail(orderDetailModel.getId());
            }
        }
    }
}
