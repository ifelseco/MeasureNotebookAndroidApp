package com.javaman.olcudefteri.ui.orders.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.presenter.CustomerPresenter;
import com.javaman.olcudefteri.presenter.impl.CustomerPresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.ui.orders.OrderDetailActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.javaman.olcudefteri.view.AddOrderView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by javaman on 18.12.2017.
 * Güneşlik dialog
 */
public class CustomerUpdateDialog extends DialogFragment implements View.OnClickListener,AddOrderView, View.OnFocusChangeListener {


    SharedPreferenceHelper sharedPreferenceHelper;
    CustomerDetailModel customerDetailModel = new CustomerDetailModel();

    @BindView(R.id.editTextName)
    EditText etNameSurname;

    @BindView(R.id.editTextMobilePhone)
    EditText etMobilePhone;

    @BindView(R.id.editTextFixedPhone)
    EditText etFixedPhone;

    @BindView(R.id.editTextAddress)
    EditText etAddress;

    @BindView(R.id.checkBoxQuestion)
    CheckBox checkBoxQuestion;

    @BindView(R.id.pb_update_customer)
    ProgressBar pbUpdateCustomer;

    @BindView(R.id.btn_cancel)
    Button imageButtonCancel;

    @BindView(R.id.btn_save)
    Button imageButtonSave;

    private CustomerPresenter mCustomerPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomerPresenter=new CustomerPresenterImpl(this);
        Bundle arguments = getArguments();

        if (arguments != null) {
            if (arguments.containsKey(OrderDetailActivity.ARG_GOTO_UPDATE_CUSTOMER_FROM_ORDER_DETAIL)) {
                customerDetailModel = arguments.getParcelable(OrderDetailActivity.ARG_GOTO_UPDATE_CUSTOMER_FROM_ORDER_DETAIL);
            }
        } else {
            dismiss();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_update_dialog, null);
        sharedPreferenceHelper = new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this, view);
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        initView();

        return view;
    }

    private void initView() {

        etMobilePhone.setOnFocusChangeListener(this);
        etFixedPhone.setOnFocusChangeListener(this);

        if(!TextUtils.isEmpty(customerDetailModel.getNameSurname())){
            etNameSurname.setText(customerDetailModel.getNameSurname());
        }

        if(!TextUtils.isEmpty(customerDetailModel.getMobilePhone())){
            etMobilePhone.setText(customerDetailModel.getMobilePhone());
        }

        if(!TextUtils.isEmpty(customerDetailModel.getFixedPhone())){
            etFixedPhone.setText(customerDetailModel.getFixedPhone());
        }

        if(!TextUtils.isEmpty(customerDetailModel.getAddress())){
            etAddress.setText(customerDetailModel.getAddress());
        }

        checkBoxQuestion.setChecked(customerDetailModel.isNewsletterAccepted());


    }







    @Override
    @OnClick({R.id.btn_cancel,R.id.btn_save})
    public void onClick(View view) {
        if(view.getId()==R.id.btn_save){
            updateCustomer(customerDetailModel);
        }else if(view.getId()==R.id.btn_cancel){
            dismiss();
        }
    }



    @Override
    public void updateData(CustomerDetailModel customerDetailModel) {
        EventBus.getDefault().post(customerDetailModel);
        dismiss();
    }

    public void updateCustomer(CustomerDetailModel customerDetailModel) {
        customerDetailModel.setNameSurname(etNameSurname.getText().toString());
        customerDetailModel.setAddress(etAddress.getText().toString());
        String mobilePhone=etMobilePhone.getText().toString().replaceAll("[^0-9]","");
        String fixedPhone=etFixedPhone.getText().toString().replaceAll("[^0-9]","");
        customerDetailModel.setMobilePhone(mobilePhone);
        customerDetailModel.setFixedPhone(fixedPhone);
        customerDetailModel.setNewsletterAccepted(checkBoxQuestion.isChecked());
        String headerData=getSessionIdFromPref();
        mCustomerPresenter.updateCustomer(customerDetailModel,headerData);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        pbUpdateCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbUpdateCustomer.setVisibility(View.GONE);
    }

    @Override
    public void setNameEmptyError() {
        etNameSurname.setError("Ad soyad boş geçilemez");
    }

    @Override
    public void setPhoneEmptyError() {
        etMobilePhone.setError("En az bir telefon bilgisi girmelisiniz.");
        etFixedPhone.setError("En az bir telefon bilgisi girmelisiniz.");

    }

    @Override
    public void setPhoneFormatError(boolean isMobile, boolean isFixed) {
        if(isMobile && !isFixed){
            etMobilePhone.setError("Cep telefonu hatalı girildi");
        }else if(!isMobile && isFixed){
            etFixedPhone.setError("Sabit telefon hatalı girildi");
        }else if(!isMobile && !isFixed){

        }else{
            etMobilePhone.setError("Cep telefonu hatalı girildi");
            etFixedPhone.setError("Sabit telefon hatalı girildi");
        }
    }

    @Override
    public void navigateToOrder(AddCustomerResponse addCustomerResponse) {

    }

    @Override
    public void showAlert(String message) {
        StyleableToast.makeText(getActivity(),message,R.style.info_toast_style).show();
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }

    @Override
    public void navigateLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.editTextMobilePhone){
            if(hasFocus){
                etMobilePhone.setHint("(05xx)-xxx-xx-xx");
            }else{
                etMobilePhone.setHint("");
            }
        }else if(v.getId()==R.id.editTextFixedPhone){
            if(hasFocus){
                etFixedPhone.setHint("(0xxx)-xxx-xx-xx");
            }else{
                etFixedPhone.setHint("");
            }
        }
    }
}

