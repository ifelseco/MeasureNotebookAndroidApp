package com.javaman.olcudefteri.tailor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.OrderDetailActivity;
import com.javaman.olcudefteri.orders.OrderLineAdapter;
import com.javaman.olcudefteri.orders.model.OrderDetailModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.OrderUpdateModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TailorOrderAdapter extends RecyclerView.Adapter<TailorOrderAdapter.TailorOrderViewHolder> {

    private List<OrderDetailModel> mOrders;
    Context mContext;
    TailorOrderFragment mTailorOrderFragment;

    public TailorOrderAdapter(List<OrderDetailModel> orders, Context context, TailorOrderFragment tailorOrderFragment) {
        this.mContext=context;
        this.mOrders=orders;
        this.mTailorOrderFragment=tailorOrderFragment;

    }

    @NonNull
    @Override
    public TailorOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tailor_order_item, parent, false);
        return new TailorOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TailorOrderViewHolder holder, int position) {
        OrderDetailModel orderDetailModel=mOrders.get(position);
        holder.itemView.setTag(orderDetailModel);
        holder.bind(orderDetailModel,position);
    }

    @Override
    public int getItemCount() {
        if (mOrders != null) {
            return mOrders.size();
        } else {
            return 0;
        }
    }

    public void removeItemFromList(OrderUpdateModel orderUpdateModel){

        for (OrderDetailModel orderDetailModel:mOrders) {
            if(orderDetailModel.getId()==orderUpdateModel.getId()){
                mOrders.remove(orderDetailModel);
                break;
            }
        }


        notifyDataSetChanged();
        if(mOrders.size()==0){
            mTailorOrderFragment.setEmptyBackground();
        }

    }

    public void updateList(List<OrderDetailModel> mOrders){

    }



    public class TailorOrderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_order_number)
        TextView tvOrderNo;

        @BindView(R.id.tv_custmer_namesurname)
        TextView tvNameSurname;


        @BindView(R.id.tv_delivery_date)
        TextView tvDeliveryDate;


        @BindView(R.id.btn_tailor_order_finish)
        ImageButton imageButtonFinish;

        @BindView(R.id.btn_tailor_order_detail)
        ImageButton imageButtonDetail;

        @BindView(R.id.btn_tailor_order_back)
        ImageButton imageButtonBack;




        public TailorOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageButtonBack.setVisibility(View.GONE);
        }

        public void bind(OrderDetailModel orderDetailModel, int position) {

            if(orderDetailModel.getOrderStatus()==4){
                imageButtonBack.setVisibility(View.VISIBLE);
                imageButtonFinish.setVisibility(View.GONE);
            }else if(orderDetailModel.getOrderStatus()==3){
                imageButtonBack.setVisibility(View.GONE);
                imageButtonFinish.setVisibility(View.VISIBLE);
            }

            tvOrderNo.setText(String.valueOf(orderDetailModel.getId()));

            tvNameSurname.setText(orderDetailModel.getCustomer().getNameSurname());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");

            if (orderDetailModel.getDeliveryDate() != null && !orderDetailModel.getDeliveryDate().equals("")) {
                String deliveryDate = simpleDateFormat.format(orderDetailModel.getDeliveryDate());
                tvDeliveryDate.setText(deliveryDate);
            }
        }

        @OnClick({R.id.btn_tailor_order_detail,R.id.btn_tailor_order_finish,R.id.btn_tailor_order_back})
        public void onClick(View view){
            int id=view.getId();
            OrderDetailModel orderDetailModel= (OrderDetailModel) itemView.getTag();
            if(id==R.id.btn_tailor_order_detail){
                Context context = view.getContext();
                Intent intent = new Intent(context, TailorOrderDetailActivity.class);
                intent.putExtra(TailorOrderDetailActivity.ARG_CURRENT_ORDER , orderDetailModel.getId());
                context.startActivity(intent);
            }else if(id==R.id.btn_tailor_order_finish){
                mTailorOrderFragment.updateOrder(orderDetailModel);
            }else if(id==R.id.btn_tailor_order_back){
                mTailorOrderFragment.updateOrder(orderDetailModel);
            }
        }
    }
}
