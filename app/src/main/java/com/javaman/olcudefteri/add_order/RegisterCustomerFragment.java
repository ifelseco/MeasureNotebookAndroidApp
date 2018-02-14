package com.javaman.olcudefteri.add_order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 14.12.2017.
 */

public class RegisterCustomerFragment extends Fragment {

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextSurname)
    EditText editTextSurname;

    @BindView(R.id.editTextMobilePhone)
    EditText editTextMobilePhone;

    @BindView(R.id.editTextFixedPhone)
    EditText editTextFixedPhone;

    @BindView(R.id.editTextAddress)
    EditText editTextAddress;

    @BindView(R.id.checkBoxQuestion)
    CheckBox checkBoxQuestion;

    @BindView(R.id.resetFormButton)
    Button resetButton;

    @BindView(R.id.takeMeasureButton)
    Button regSaveBtn;


    private boolean isCustomerRegister = false;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };



    private boolean checkFieldsForEmptyValues() {

        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String mobilePhone = editTextMobilePhone.getText().toString();
        String fixedPhone = editTextFixedPhone.getText().toString();
        String addreess = editTextAddress.getText().toString();

        if (name.equals("") || surname.equals("")) {
            Toast.makeText(getActivity(), "Gerekli bilgileri doldurunuz.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobilePhone.equals("") || fixedPhone.equals("")) {
            Toast.makeText(getActivity(), "Gerekli bilgileri doldurunuz.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            regSaveBtn.setEnabled(true);
            return true;

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_customer_layout, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Müşteri bilgilerini gir.");

        editTextName.addTextChangedListener(textWatcher);
        editTextSurname.addTextChangedListener(textWatcher);
        editTextMobilePhone.addTextChangedListener(textWatcher);
        editTextFixedPhone.addTextChangedListener(textWatcher);
        editTextAddress.addTextChangedListener(textWatcher);

        regSaveBtn.setEnabled(false);

        if (savedInstanceState != null) {
            editTextName.setText(savedInstanceState.getString("name"));
            editTextSurname.setText(savedInstanceState.getString("surname"));
            editTextMobilePhone.setText(savedInstanceState.getString("mobilePhone"));
            editTextFixedPhone.setText(savedInstanceState.getString("fixedPhone"));
            editTextAddress.setText(savedInstanceState.getString("address"));
            checkBoxQuestion.setChecked(savedInstanceState.getBoolean("question"));
            regSaveBtn.setEnabled(savedInstanceState.getBoolean("saveButtonStatus"));

        }

        SharedPreferences prefs=getActivity().getSharedPreferences("customerForm",Context.MODE_PRIVATE);

        if(prefs!=null){
            editTextName.setText(prefs.getString("name",""));
            editTextSurname.setText(prefs.getString("surname",""));
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
        outState.putString("surname", editTextSurname.getText().toString());
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
        editor2.putString("surname",editTextSurname.getText().toString());
        editor2.putString("mobilePhone",editTextMobilePhone.getText().toString());
        editor2.putString("fixedPhone",editTextFixedPhone.getText().toString());
        editor2.putString("address",editTextAddress.getText().toString());
        editor2.putBoolean("question",checkBoxQuestion.isChecked());
        editor2.putBoolean("saveButtonStatus",regSaveBtn.isEnabled());
        editor2.commit();


    }

    @OnClick({R.id.takeMeasureButton , R.id.resetFormButton})
    public void onClick(View view){
        int id=view.getId();
        if(id==R.id.takeMeasureButton){

            if(checkFieldsForEmptyValues()){
                isCustomerRegister=true;
                CustomerOrderEvent customerOrderEvent=new CustomerOrderEvent();
                customerOrderEvent.setOrderRegistered(isCustomerRegister);
                EventBus.getDefault().post(customerOrderEvent);

            }else{
                Toast.makeText(getActivity(), "Gerekli bilgileri doldurunuz.", Toast.LENGTH_SHORT).show();
            }

        }else{
            editTextName.setText("");
            editTextSurname.setText("");
            editTextFixedPhone.setText("");
            editTextMobilePhone.setText("");
            editTextAddress.setText("");
            checkBoxQuestion.setChecked(false);

            regSaveBtn.setEnabled(false);
        }
    }


}
