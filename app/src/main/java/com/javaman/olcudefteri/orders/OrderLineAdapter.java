package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 13.03.2018.
 */

public class OrderLineAdapter extends RecyclerView.Adapter<OrderLineAdapter.OrderLineViewHolder> {

    private List<OrderLineDetailModel> orderLines=new ArrayList<>();
    Context mContext;
    OrderLineFragment orderLineFragment;

    public OrderLineAdapter(List<OrderLineDetailModel> orderLines , Context context ,OrderLineFragment orderLineFragment ) {
        this.orderLines = orderLines;
        this.mContext=context;
        this.orderLineFragment=orderLineFragment;
    }

    @Override
    public OrderLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_line_item, parent, false);
        return new OrderLineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final OrderLineViewHolder holder, int position) {
        OrderLineDetailModel orderLineDetailModel=orderLines.get(position);
        holder.itemView.setTag(orderLineDetailModel);
        holder.bind(orderLineDetailModel,position);



    }

    @Override
    public int getItemCount() {
        if (orderLines != null) {
            return orderLines.size();
        } else {
            return 0;
        }

    }

    public void removeItemFromList(OrderLineDetailModel orderLineDetailModel){
        orderLines.remove(orderLineDetailModel);
        notifyDataSetChanged();

    }


    public class OrderLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.tv_location_name) TextView tvLocationName;
        @BindView(R.id.tv_location_type) TextView tvLocationType;
        @BindView(R.id.tv_product_value) TextView tvProductValue;
        @BindView(R.id.tv_product_alias) TextView tvProductALias;
        @BindView(R.id.tv_product_pattern) TextView tvProductPattern;
        @BindView(R.id.tv_product_variant) TextView tvProductVariant;
        @BindView(R.id.tv_product_width) TextView tvProductWidth;
        @BindView(R.id.tv_product_height) TextView tvProductHeight;
        @BindView(R.id.tv_used_material) TextView tvUsedMaterial;
        @BindView(R.id.tv_unit_price) TextView tvUnitPrice;
        @BindView(R.id.tv_line_amount) TextView tvLineAmount;

        @BindView(R.id.image_button_line_detail)
        ImageButton imageButtonLineDetail;

        @BindView(R.id.image_button_line_option)
        ImageButton imageButtonLineOption;

        @BindView(R.id.image_button_close)
        ImageButton imageButtonClose;

        @BindView(R.id.card_order_line_item)
        CardView cardView;



        public OrderLineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imageButtonLineOption.setVisibility(View.VISIBLE);
            imageButtonLineDetail.setVisibility(View.VISIBLE);
            imageButtonClose.setVisibility(View.GONE);

            imageButtonLineDetail.setOnClickListener(this);
            imageButtonLineOption.setOnClickListener(this);
            cardView.setOnClickListener(this);

        }

        public void bind(OrderLineDetailModel orderLineDetailModel, int position) {


            tvLocationName.setText(orderLineDetailModel.getLocationName());
            tvLocationType.setText(orderLineDetailModel.getLocationType());
            tvProductALias.setText("İsim :"+orderLineDetailModel.getProduct().getAliasName());
            tvProductPattern.setText("Desen :"+orderLineDetailModel.getProduct().getPatternCode());
            tvProductVariant.setText("Variant :"+orderLineDetailModel.getProduct().getVariantCode());
            tvProductWidth.setText("En :"+orderLineDetailModel.getPropertyWidth());
            tvProductHeight.setText("Boy :"+orderLineDetailModel.getPropertyHeight());

            Currency currency=Currency.getInstance(new Locale("tr","TR"));
            tvUnitPrice.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderLineDetailModel.getUnitPrice()));
            tvLineAmount.setText(""+currency.getSymbol()+" "+String.format("%.2f",orderLineDetailModel.getLineAmount()));
            int productValue=orderLineDetailModel.getProduct().getProductValue();
            String meterUnit;
            if(
                    productValue==2 ||
                    productValue==3||
                    productValue==4||
                    productValue==5||
                    productValue==10){
                meterUnit="m2";
            }else{
                meterUnit="m";
            }

            tvUsedMaterial.setText(String.format("%.2f",orderLineDetailModel.getUsedMaterial())+" "+meterUnit);


            String[] productArray=mContext.getResources().getStringArray(R.array.curtains);
            switch(orderLineDetailModel.getProduct().getProductValue()){
                case 0:
                    tvProductValue.setText(productArray[0]);
                    break;
                case 1:
                    tvProductValue.setText(productArray[1]);
                    break;
                case 2:
                    tvProductValue.setText(productArray[2]);
                    break;

                case 3:
                    tvProductValue.setText(productArray[3]);
                    break;
                case 4:
                    tvProductValue.setText(productArray[4]);
                    break;
                case 5:
                    tvProductValue.setText(productArray[5]);
                    break;
                case 6:
                    tvProductValue.setText(productArray[6]);
                    break;
                case 7:
                    tvProductValue.setText(productArray[7]);
                    break;
                case 8:
                    tvProductValue.setText(productArray[8]);
                    break;
                case 9:
                    tvProductValue.setText(productArray[9]);
                    break;
                case 10:
                    tvProductValue.setText(productArray[10]);
                    break;

            }
        }


        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.image_button_line_option){
                PopupMenu popupMenu=new PopupMenu(mContext,imageButtonLineOption);
                popupMenu.inflate(R.menu.menu_line_action);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }else if(v.getId()==R.id.image_button_line_detail){
                OrderLineDetailModel orderLineDetailModel= (OrderLineDetailModel) itemView.getTag();
                orderLineFragment.openBottomSheet(orderLineDetailModel);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            OrderLineDetailModel orderLineDetailModel= (OrderLineDetailModel) itemView.getTag();
            switch(item.getItemId()){

                case R.id.menu_item_delete_line:

                    orderLineFragment.deleteOrderLne(orderLineDetailModel);
                    break;
                case R.id.menu_item_update_line:

                    orderLineFragment.updateOrderLine(orderLineDetailModel);
                    break;
            }
            return false;
        }
    }
}
