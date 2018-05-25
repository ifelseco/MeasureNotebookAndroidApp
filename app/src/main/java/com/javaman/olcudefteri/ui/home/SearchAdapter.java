package com.javaman.olcudefteri.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.OrderDetailModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.ui.orders.CustomerDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearcViewHolder> {

    private List<CustomerDetailModel> mCustomers;
    Context mContext;
    CustomersFragment customersFragment;

    public SearchAdapter(List<CustomerDetailModel> customers, Context context, CustomersFragment customerDetailFragment) {
        this.mCustomers=customers;
        this.mContext=context;
        this.customersFragment=customerDetailFragment;
    }

    @NonNull
    @Override
    public SearcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_search_item, parent, false);
        return new SearcViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearcViewHolder holder, int position) {
        CustomerDetailModel customerDetailModel = mCustomers.get(position);
        holder.itemView.setTag(customerDetailModel);
        holder.bind(customerDetailModel,position);
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
        if(mCustomers!=null){
            return mCustomers.size();
        }else{
            return 0;
        }

    }

    public void clearAdapter() {
        mCustomers.clear();
        notifyDataSetChanged();
    }


    public class SearcViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.tv_customer_name)
        TextView textViewCustomerName;

        @BindView(R.id.image_button_detail)
        ImageButton imageButtonDetail;

        public SearcViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageButtonDetail.setOnClickListener(this);
        }

        public void bind(CustomerDetailModel customerDetailModel, int position) {
            textViewCustomerName.setText(customerDetailModel.getNameSurname());
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.image_button_detail){
                PopupMenu popupMenu=new PopupMenu(mContext,imageButtonDetail);
                popupMenu.inflate(R.menu.menu_customer_action);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            CustomerDetailModel customerDetailModel= (CustomerDetailModel) itemView.getTag();
            switch(item.getItemId()){

                case R.id.menu_item_add_order:
                    customersFragment.addOrder(customerDetailModel);
                    break;
                case R.id.menu_item_old_orders:
                    customersFragment.showCustomerOrders(customerDetailModel.getId());
                    break;
            }
            return false;
        }
    }
}
