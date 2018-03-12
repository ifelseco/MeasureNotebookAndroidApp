package com.javaman.olcudefteri.orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 08.03.2018.
 */

public class CustomerDetailFragment extends Fragment {

    private CustomerDetailModel customerDetailModel;

    @BindView(R.id.tv_customer_name)
    TextView textViewCustomerName;

    @BindView(R.id.tv_customer_mobile_phone)
    TextView textViewCustomerMobilePhone;

    @BindView(R.id.tv_customer_fixed_tel)
    TextView textViewCustomerFixedPhone;

    @BindView(R.id.tv_customer_address)
    TextView textViewCustomerAddress;

    @BindView(R.id.tv_customer_newsletter)
    TextView textViewCustomerNewsletter;





    public CustomerDetailFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(OrderDetailActivity.ARG_CURRENT_CUSTOMER)) {
            customerDetailModel = getArguments().getParcelable(OrderDetailActivity.ARG_CURRENT_CUSTOMER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_detail_fragment, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
    }

    private void setView() {

        textViewCustomerName.setText(customerDetailModel.getNameSurname());
        textViewCustomerFixedPhone.setText(customerDetailModel.getFixedPhone());
        textViewCustomerMobilePhone.setText(customerDetailModel.getMobilePhone());
        textViewCustomerAddress.setText(customerDetailModel.getAddress());

        if(customerDetailModel.isNewsletterAccepted()){
            textViewCustomerNewsletter.setText("Bilgilendirme istiyor ");
        }else{
            textViewCustomerNewsletter.setText("Bilgilendirme istemiyor. ");

        }
    }
}
