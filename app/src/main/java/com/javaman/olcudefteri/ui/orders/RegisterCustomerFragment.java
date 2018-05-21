package com.javaman.olcudefteri.ui.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AddCustomerModel;
import com.javaman.olcudefteri.model.CustomerDetailModel;
import com.javaman.olcudefteri.model.AddCustomerResponse;
import com.javaman.olcudefteri.presenter.AddOrderPresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderPresenterImpl;
import com.javaman.olcudefteri.ui.login.LoginActivity;
import com.javaman.olcudefteri.view.AddOrderView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 14.12.2017.
 */

public class RegisterCustomerFragment extends Fragment implements AddOrderView {

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextMobilePhone)
    EditText editTextMobilePhone;

    @BindView(R.id.editTextFixedPhone)
    EditText editTextFixedPhone;

    @BindView(R.id.editTextAddress)
    EditText editTextAddress;

    @BindView(R.id.checkBoxQuestion)
    CheckBox checkBoxQuestion;

    @BindView(R.id.cehckBoxTobeMeasured)
    CheckBox cehckBoxTobeMeasured;

    @BindView(R.id.pb_add_customer)
    ProgressBar progressBarAddCustomer;


    @BindView(R.id.takeMeasureButton)
    Button regSaveBtn;

    SharedPreferenceHelper sharedPreferenceHelper;

    private AddOrderPresenter mAddOrderPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){



        View view = inflater.inflate(R.layout.register_customer_layout, container, false);
        ButterKnife.bind(this,view);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderPresenter=new AddOrderPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Müşteri bilgilerini gir.");



        if (savedInstanceState != null) {
            editTextName.setText(savedInstanceState.getString("name"));
            editTextMobilePhone.setText(savedInstanceState.getString("mobilePhone"));
            editTextFixedPhone.setText(savedInstanceState.getString("fixedPhone"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            checkBoxQuestion.setChecked(savedInstanceState.getBoolean("question"));
            regSaveBtn.setEnabled(savedInstanceState.getBoolean("saveButtonStatus"));

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        outState.putString("name", editTextName.getText().toString());
        outState.putString("mobilePhone", editTextMobilePhone.getText().toString());
        outState.putString("fixedPhone", editTextFixedPhone.getText().toString());
        outState.putString("address", editTextAddress.getText().toString());
        outState.putBoolean("question", checkBoxQuestion.isChecked());
        outState.putBoolean("saveButtonStatus",regSaveBtn.isEnabled());

        Log.d("MesajXXXXXX : ","RegisterCustomerFragment onSaveInstanceState metodu çalıştı....");

    }

    @OnClick(R.id.takeMeasureButton)
    public void onClick(View view){
        int id=view.getId();
        if(id==R.id.takeMeasureButton){

            CustomerDetailModel customerDetailModel=new CustomerDetailModel();
            customerDetailModel.setNameSurname(editTextName.getText().toString());
            customerDetailModel.setAddress(editTextAddress.getText().toString());
            customerDetailModel.setMobilePhone(editTextMobilePhone.getText().toString());
            customerDetailModel.setFixedPhone(editTextFixedPhone.getText().toString());
            customerDetailModel.setNewsletterAccepted(checkBoxQuestion.isChecked());

            AddCustomerModel addCustomerModel=new AddCustomerModel();
            addCustomerModel.setCustomerDetailModel(customerDetailModel);
            addCustomerModel.setOrderStatus(cehckBoxTobeMeasured.isChecked()==true ? 1:0);

            String sessionId=getSessionIdFromPref();

            mAddOrderPresenter.addCustomer(addCustomerModel,sessionId);

        }
    }


    @Override
    public void showProgress() {
        progressBarAddCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarAddCustomer.setVisibility(View.GONE);
    }

    @Override
    public void setNameEmptyError() {
        editTextName.setError("Ad soyad boş geçilemez");
    }

    @Override
    public void setPhoneEmptyError() {
        editTextMobilePhone.setError("En az bir telefon bilgisi girmelisiniz.");
        editTextFixedPhone.setError("En az bir telefon bilgisi girmelisiniz.");

    }

    @Override
    public void navigateToOrder(AddCustomerResponse addCustomerResponse) {
        Intent intent=new Intent(getActivity(),AddOrderActivity.class);
        intent.putExtra(AddOrderActivity.ARG_ADD_ORDER,addCustomerResponse);
        startActivity(intent);
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
    public void onDestroy() {
        super.onDestroy();
        mAddOrderPresenter.onDestroy();
    }


}
