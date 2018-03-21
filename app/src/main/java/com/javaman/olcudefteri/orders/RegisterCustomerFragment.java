package com.javaman.olcudefteri.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.javaman.olcudefteri.orders.model.AddCustomerModel;
import com.javaman.olcudefteri.orders.model.CustomerDetailModel;
import com.javaman.olcudefteri.orders.model.response.AddCustomerResponse;
import com.javaman.olcudefteri.orders.presenter.AddOrderPresenter;
import com.javaman.olcudefteri.orders.presenter.AddOrderPresenterImpl;
import com.javaman.olcudefteri.orders.view.AddOrderView;

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





    private boolean isCustomerRegister = false;
    private AddOrderPresenter mAddOrderPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){



        View view = inflater.inflate(R.layout.register_customer_layout, container, false);
        ButterKnife.bind(this,view);
        mAddOrderPresenter=new AddOrderPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Müşteri bilgilerini gir.");


        regSaveBtn.setEnabled(false);

        if (savedInstanceState != null) {
            editTextName.setText(savedInstanceState.getString("name"));
            editTextMobilePhone.setText(savedInstanceState.getString("mobilePhone"));
            editTextFixedPhone.setText(savedInstanceState.getString("fixedPhone"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            checkBoxQuestion.setChecked(savedInstanceState.getBoolean("question"));
            regSaveBtn.setEnabled(savedInstanceState.getBoolean("saveButtonStatus"));

        }

        SharedPreferences prefs=getActivity().getSharedPreferences("customerForm",Context.MODE_PRIVATE);

        if(prefs!=null){
            editTextName.setText(prefs.getString("name",""));
            editTextMobilePhone.setText(prefs.getString("mobilePhone",""));
            editTextFixedPhone.setText(prefs.getString("fixedPhone",""));
            editTextAddress.setText(prefs.getString("address",""));
            checkBoxQuestion.setChecked(prefs.getBoolean("question",false));
            regSaveBtn.setEnabled(prefs.getBoolean("saveButtonStatus",true));
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



    @Override
    public void onPause() {

        super.onPause();

        Log.d("MesajXXXXXX : ","RegisterCustomerFragment Pause metodu çalıştı....");

        SharedPreferences prefCustomerForm = getActivity().getSharedPreferences("customerForm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = prefCustomerForm.edit();
        editor2.putString("name",editTextName.getText().toString());
        editor2.putString("mobilePhone",editTextMobilePhone.getText().toString());
        editor2.putString("fixedPhone",editTextFixedPhone.getText().toString());
        editor2.putString("address",editTextAddress.getText().toString());
        editor2.putBoolean("question",checkBoxQuestion.isChecked());
        editor2.putBoolean("saveButtonStatus",regSaveBtn.isEnabled());
        editor2.commit();


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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkSession() {

        /*SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Session",Context.MODE_PRIVATE);
        String sessionId=sharedPreferences.getString("sessionId",null);

        if(!(sessionId!=null && sessionId.equals(""))){
            startActivity(new Intent(getActivity() , LoginActivity.class));
        }*/

    }

    @Override
    public String getSessionIdFromPref() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Session",Context.MODE_PRIVATE);
        String sessionId=sharedPreferences.getString("sessionId",null);
        return sessionId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderPresenter.onDestroy();
    }
}
