package com.javaman.olcudefteri.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.OrderDetailResponseModel;
import com.javaman.olcudefteri.model.OrderSummaryPageReponseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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


    public OrderAdapter(Context context, List<OrderDetailResponseModel> mOrders) {
        Log.i("track", " in adapter constructor");
        //inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(context);
        ordersActivity = (OrdersActivity) context;
        this.mOrders = mOrders;
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
        Log.i("track", " in onBindViewHolder");
        OrderDetailResponseModel order = mOrders.get(position);
        holder.itemView.setTag(order);
        holder.bind(order, position);
    }

    public void deleteSelectedItems(ArrayList<OrderDetailResponseModel> selectedOrderList) {
        for (OrderDetailResponseModel orderDetailResponseModel : selectedOrderList){
            mOrders.remove(orderDetailResponseModel);

        }
        notifyDataSetChanged();
        if(mOrders.size()==0){
            ordersActivity.setEmptyBacground();
        }
    }

    public void updateList(List<OrderDetailResponseModel> orderList){
        mOrders.clear();
        mOrders=orderList;
        notifyDataSetChanged();

    }


    public void updateList(OrderSummaryPageReponseModel orderSummaryPageReponseModel){

        if(orderSummaryPageReponseModel.getOrderDetailPage().getNumber()>0){
            mOrders.addAll(orderSummaryPageReponseModel.getOrderDetailPage().getContent());
        }else{
            mOrders.clear();
            mOrders=orderSummaryPageReponseModel.getOrderDetailPage().getContent();
        }

        notifyDataSetChanged();

    }



    public void clearList(){
        mOrders.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_order_number)
        TextView tvOrderNo;

        @BindView(R.id.tv_custmer_namesurname)
        TextView tvNameSurname;

        @BindView(R.id.tv_order_status)
        TextView tvOrderStatus;


        @BindView(R.id.tv_order_date)
        TextView tvOrderDate;

        @BindView(R.id.tv_delivery_date)
        TextView tvDeliveryDate;

        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;

        @BindView(R.id.image_view_order_status2)
        ImageView imageViewOrderStatus;

        @BindView(R.id.card_order_item)
        CardView cardViewOrder;

        @BindView(R.id.linear_layout_order_status)
        LinearLayout linearLayoutOrderStatus;

        @BindView(R.id.linear_layout_order_mount)
        LinearLayout linearLayoutOrderMount;


        OrdersActivity ordersActivity;


        public MyViewHolder(View itemView, OrdersActivity ordersActivity) {


            super(itemView);
            Log.i("track", " in MyViewHolder constructor");
            ButterKnife.bind(this, itemView);
            cardViewOrder.setOnClickListener(this);
            this.ordersActivity = ordersActivity;

        }

        public void bind(OrderDetailResponseModel order, int position) {
            tvOrderNo.setText(order.getOrderNumber());
            Log.i("cust-track",""+order.getCustomer().getId());
            tvNameSurname.setText(order.getCustomer().getNameSurname());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
            String orderDate = simpleDateFormat.format(order.getOrderDate());
            tvOrderDate.setText(orderDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(order.getOrderDate());
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);


            tvOrderTime.setText(""+hours+" : "+minutes);

            if (order.getDeliveryDate() != null && !order.getDeliveryDate().equals("")) {
                String deliveryDate = simpleDateFormat.format(order.getDeliveryDate());
                tvDeliveryDate.setText(deliveryDate);
            }





            if (order.isMountExist()) {
                linearLayoutOrderMount.setVisibility(View.VISIBLE);

            }



            //renklendirme işlemleri yapılacak...

            if (order.getOrderStatus() == 0) {
                tvOrderStatus.setText("Eksik");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_gray);
                imageViewOrderStatus.setImageResource(R.drawable.ic_remove_circle_black_24dp);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));

            } else if (order.getOrderStatus() == 1) {
                tvOrderStatus.setText("Ölçüye gidilecek");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_purple);
                imageViewOrderStatus.setImageResource(R.drawable.ic_measure);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));
            } else if (order.getOrderStatus() == 2) {
                tvOrderStatus.setText("Sipariş kaydı alındı.");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_yellow);
                imageViewOrderStatus.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));
            } else if (order.getOrderStatus() == 3) {
                tvOrderStatus.setText("Terzide");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_pink);
                imageViewOrderStatus.setImageResource(R.drawable.ic_tailor);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));
            } else if (order.getOrderStatus() == 4) {
                tvOrderStatus.setText("Hazır");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_green);
                imageViewOrderStatus.setImageResource(R.drawable.ic_check_circle_black_24dp);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));
            } else if (order.getOrderStatus() == 5) {
                tvOrderStatus.setText("Teslim Edildi");
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_primary);
                imageViewOrderStatus.setImageResource(R.drawable.ic_delivered);
                imageViewOrderStatus.setColorFilter(ordersActivity.getResources().getColor(R.color.primaryTextColor));
            } else if (order.getOrderStatus() == 6) {
                tvOrderStatus.setText("Teklif");
                tvOrderStatus.setTextColor(ordersActivity.getResources().getColor(R.color.secondaryTextColor));
                linearLayoutOrderStatus.setBackgroundResource(R.drawable.rectangle_background_lime);
                imageViewOrderStatus.setImageResource(R.drawable.ic_local_offer_black_24dp);
            }
        }


        @Override
        public void onClick(View view) {
            OrderDetailResponseModel orderDetailResponseModel= (OrderDetailResponseModel) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra(OrderDetailActivity.ARG_CURRENT_ORDER , orderDetailResponseModel.getId());
            context.startActivity(intent);
        }


    }

}
