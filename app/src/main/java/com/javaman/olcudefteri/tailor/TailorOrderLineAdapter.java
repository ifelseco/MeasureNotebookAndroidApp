package com.javaman.olcudefteri.tailor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.response.OrderDetailResponseModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TailorOrderLineAdapter extends RecyclerView.Adapter<TailorOrderLineAdapter.TailorOrderLineViewHolder> {
    
    List<OrderLineDetailModel> mOrderLines;
    TailorOrderDetailActivity tailorOrderDetailActivity;
    LayoutInflater inflater;

    public TailorOrderLineAdapter(Context context, List<OrderLineDetailModel> orderLineDetailList) {
        mOrderLines=orderLineDetailList;
        tailorOrderDetailActivity=(TailorOrderDetailActivity)context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TailorOrderLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TailorOrderLineViewHolder holder;
        View view = inflater.inflate(R.layout.tailor_order_line, parent, false);
        holder = new TailorOrderLineViewHolder(view, tailorOrderDetailActivity);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TailorOrderLineViewHolder holder, int position) {
        OrderLineDetailModel orderLine = mOrderLines.get(position);
        holder.itemView.setTag(orderLine);
        holder.bind(orderLine, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TailorOrderLineViewHolder extends RecyclerView.ViewHolder {

        TailorOrderDetailActivity tailorOrderDetailActivity;

        @BindView(R.id.linear_layout_detail)
        LinearLayout linearLayoutDetail;

        @BindView(R.id.linear_layout_briz)
        LinearLayout linearLayoutBriz;

        @BindView(R.id.linear_layout_pile)
        LinearLayout linearLayoutPile;

        @BindView(R.id.linear_layout_fon)
        LinearLayout linearLayoutFon;

        @BindView(R.id.linear_layout_kruvaze)
        LinearLayout linearLayoutKruvaze;

        @BindView(R.id.linear_layout_farbela)
        LinearLayout linearLayoutFarbela;



        public TailorOrderLineViewHolder(View itemView, TailorOrderDetailActivity tailorOrderDetailActivity) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.tailorOrderDetailActivity=tailorOrderDetailActivity;
            
        }

        public void bind(OrderLineDetailModel orderLine, int position) {
            setDetailLayout(orderLine.getProduct().getProductValue());
        }

        public void setDetailLayout(int productValue) {

        }
    }
}
