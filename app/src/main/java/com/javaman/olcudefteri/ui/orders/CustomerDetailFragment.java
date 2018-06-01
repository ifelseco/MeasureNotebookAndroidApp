package com.javaman.olcudefteri.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by javaman on 08.03.2018.
 */

public class CustomerDetailFragment extends Fragment implements View.OnClickListener{

    private CustomerDetailModel customerDetailModel;

    @BindView(R.id.tv_customer_name)
    TextView textViewCustomerName;

    @BindView(R.id.tv_customer_mobile_phone)
    TextView textViewCustomerMobilePhone;

    @BindView(R.id.tv_customer_fixed_tel)
    TextView textViewCustomerFixedPhone;

    @BindView(R.id.tv_customer_address)
    TextView textViewCustomerAddress;

    @BindView(R.id.checkboxNewsletter)
    CheckBox checkBoxNewsletter;

    @BindView(R.id.linear_layout_newsletter)
    LinearLayout linearLayoutNewsLetter;

    @BindView(R.id.image_button_call)
    ImageButton imageButtonCall;

    @BindView(R.id.image_button_sms)
    ImageButton imageButtonSms;

    @BindView(R.id.image_button_navigation)
    ImageButton imageButtonNavigation;


    public CustomerDetailFragment() {
    }


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
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(customerDetailModel);
    }

    private void setView(CustomerDetailModel customerDetailModel) {

        imageButtonCall.setOnClickListener(this);
        imageButtonSms.setOnClickListener(this);
        imageButtonNavigation.setOnClickListener(this);

        textViewCustomerName.setText(customerDetailModel.getNameSurname());


        if (customerDetailModel.getFixedPhone() != null) {
            if (customerDetailModel.getFixedPhone().length() == 11) {
                textViewCustomerFixedPhone.setText(String.format(String.format("(%s) %s-%s-%s", customerDetailModel.getFixedPhone().substring(0, 4), customerDetailModel.getFixedPhone().substring(4, 7),
                        customerDetailModel.getFixedPhone().substring(7, 9), customerDetailModel.getFixedPhone().substring(9, 11))));
            } else if (customerDetailModel.getFixedPhone().length() == 10) {
                textViewCustomerFixedPhone.setText(String.format(String.format("(%s) %s-%s-%s", customerDetailModel.getFixedPhone().substring(0, 3), customerDetailModel.getFixedPhone().substring(3, 6),
                        customerDetailModel.getFixedPhone().substring(6, 8), customerDetailModel.getFixedPhone().substring(8, 10))));
            } else {
                textViewCustomerFixedPhone.setText(customerDetailModel.getFixedPhone());
            }
        } else {
            textViewCustomerFixedPhone.setText("");
        }

        if (customerDetailModel.getMobilePhone() != null) {
            if (customerDetailModel.getMobilePhone().length() == 11) {
                textViewCustomerMobilePhone.setText(String.format(String.format("(%s) %s-%s-%s", customerDetailModel.getMobilePhone().substring(0, 4), customerDetailModel.getMobilePhone().substring(4, 7),
                        customerDetailModel.getMobilePhone().substring(7, 9), customerDetailModel.getMobilePhone().substring(9, 11))));
            } else if (customerDetailModel.getMobilePhone().length() == 10) {
                textViewCustomerMobilePhone.setText(String.format(String.format("(%s) %s-%s-%s", customerDetailModel.getMobilePhone().substring(0, 3), customerDetailModel.getMobilePhone().substring(3, 6),
                        customerDetailModel.getMobilePhone().substring(6, 8), customerDetailModel.getMobilePhone().substring(8, 10))));
            } else {
                textViewCustomerMobilePhone.setText(customerDetailModel.getMobilePhone());
            }
        } else {
            textViewCustomerMobilePhone.setText("");
        }


        textViewCustomerAddress.setText(customerDetailModel.getAddress());

        if (customerDetailModel.isNewsletterAccepted()) {
            linearLayoutNewsLetter.setVisibility(View.VISIBLE);
            checkBoxNewsletter.setChecked(true);
        }
    }

    @Subscribe
    public void updateCustomerView(CustomerDetailModel customerDetailModel){
        setView(customerDetailModel);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.image_button_call:
                String callNumber1 = customerDetailModel.getMobilePhone();
                String callNumber2 = customerDetailModel.getFixedPhone();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);

                if (!callNumber1.isEmpty()) {

                    intentCall.setData(Uri.parse("tel:" + callNumber1));

                } else {
                    intentCall.setData(Uri.parse("tel:" + callNumber2));
                }
                startActivity(intentCall);
                break;
            case R.id.image_button_sms:
                String smsNumber = customerDetailModel.getMobilePhone();
                Uri uri = Uri.parse("smsto:" + smsNumber);
                Intent intentSms = new Intent(Intent.ACTION_SENDTO, uri);
                intentSms.putExtra("sms_body", "");
                startActivity(intentSms);
                break;
            case R.id.image_button_navigation:
                if (!TextUtils.isEmpty(customerDetailModel.getAddress())) {

                    String address=customerDetailModel.getAddress();
                    Uri adressUri=Uri.parse("google.navigation:q="+address);
                    Intent intentAddress = new Intent(Intent.ACTION_VIEW, adressUri);
                    startActivity(intentAddress);

                } else {
                    StyleableToast.makeText(getActivity(),"Adres bilgisi kayıtlı değil",R.style.info_toast_style).show();
                }
                break;
            default:
                break;


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);

    }


}
