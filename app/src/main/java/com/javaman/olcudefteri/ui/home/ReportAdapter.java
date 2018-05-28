package com.javaman.olcudefteri.ui.home;

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
import com.javaman.olcudefteri.ui.orders.dialogs.DashboardOrderDialog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{


    private List<OrderDetailModel> mOrders=new ArrayList<>();
    Context mContext;
    DashboardOrderDialog dashboardOrderDialog;

    public ReportAdapter(List<OrderDetailModel> mOrders, Context mContext, DashboardOrderDialog dashboardOrderDialog) {
        this.mOrders = mOrders;
        this.mContext = mContext;
        this.dashboardOrderDialog = dashboardOrderDialog;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_report_item, parent, false);
        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
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

    public class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_order_number)
        TextView tvOrderNumber;

        @BindView(R.id.tv_customer_name)
        TextView tvCustomerName;

        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;

        @BindView(R.id.tv_delivery_date)
        TextView tvDeliveryDate;

        @BindView(R.id.tv_measure_date)
        TextView tvMeasureDate;

        @BindView(R.id.tv_total_amount)
        TextView tvTotalAmount;

        @BindView(R.id.tv_deposit_amaount)
        TextView tvDepositAmount;

        @BindView(R.id.tv_remain)
        TextView tvRemainAmount;

        @BindView(R.id.linear_layout_measure)
        LinearLayout linearLayoutMeasure;

        @BindView(R.id.linear_layout_delivery)
        LinearLayout linearLayoutDelivery;

        @BindView(R.id.linear_layout_amount)
        LinearLayout linearLayoutAmaount;

        @BindView(R.id.linear_layout_order_date)
        LinearLayout linearLayoutOrderDate;

        @BindView(R.id.card_report_order_item)
        CardView cardViewReportOrderItem;


        public ReportViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardViewReportOrderItem.setOnClickListener(this);
        }

        public void bind(OrderDetailModel orderDetailModel, int position) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
            Calendar calendar = Calendar.getInstance();

            if(orderDetailModel.getCustomer().getNameSurname()!=null){
                if(!TextUtils.isEmpty(orderDetailModel.getCustomer().getNameSurname())){
                    tvCustomerName.setText(orderDetailModel.getCustomer().getNameSurname());
                }
            }

            if(orderDetailModel.getOrderNumber()!=null){
                if(!TextUtils.isEmpty(orderDetailModel.getOrderNumber())){
                    tvOrderNumber.setText(orderDetailModel.getOrderNumber());
                }
            }


            if(dashboardOrderDialog.getReportNumber()==1){
                linearLayoutAmaount.setVisibility(View.VISIBLE);
                double totalAmount=orderDetailModel.getTotalAmount();
                double depositAmount=orderDetailModel.getDepositeAmount();
                double remain=totalAmount-depositAmount;

                tvTotalAmount.setText("Toplam :"+String.format("%.2f",totalAmount)+" TL");
                tvDepositAmount.setText("Kapora :"+String.format("%.2f",depositAmount)+" TL");
                tvRemainAmount.setText("Kalan :"+String.format("%.2f",remain)+" TL");
            }else if(dashboardOrderDialog.getReportNumber()==2){
                linearLayoutDelivery.setVisibility(View.VISIBLE);
                linearLayoutOrderDate.setVisibility(View.VISIBLE);
                if(orderDetailModel.getOrderDate()!=null){
                    tvOrderDate.setText(findFullDateText(orderDetailModel.getOrderDate()));
                }else{
                    tvOrderDate.setText("");
                }

                if(orderDetailModel.getDeliveryDate()!=null){
                    String deliveryDate = simpleDateFormat.format(orderDetailModel.getDeliveryDate());
                    tvDeliveryDate.setText(deliveryDate);
                }else{
                    tvDeliveryDate.setText("");
                }

            }else if(dashboardOrderDialog.getReportNumber()==3){
                linearLayoutMeasure.setVisibility(View.VISIBLE);
                linearLayoutOrderDate.setVisibility(View.VISIBLE);
                if(orderDetailModel.getOrderDate()!=null){
                    tvOrderDate.setText(findFullDateText(orderDetailModel.getOrderDate()));
                }else{
                    tvOrderDate.setText("");
                }

                if(orderDetailModel.getMeasureDate()!=null){
                    linearLayoutMeasure.setVisibility(View.VISIBLE);
                    String measureDate = simpleDateFormat.format(orderDetailModel.getMeasureDate());
                    tvMeasureDate.setText(measureDate);
                }else{
                    tvMeasureDate.setText("");
                }
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
            if(v.getId()==R.id.card_report_order_item){
                dashboardOrderDialog.gotoOrderDetail(orderDetailModel.getId());
            }
        }
    }
}
