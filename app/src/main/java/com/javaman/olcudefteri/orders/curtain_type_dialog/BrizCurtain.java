package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Briz perde dialog
 */


public class BrizCurtain extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {



    @BindView(R.id.editTextOtherPile) EditText etOtherPile;
    @BindView(R.id.editTextBrizWidth) EditText etBrizWidth;
    @BindView(R.id.editTextBrizHeight) EditText etBrizHeight;
    @BindView(R.id.editTextFarbelaWidth) EditText etFarbelaWidth;
    @BindView(R.id.editTextFarbelaHeight) EditText etFarbelaHeight;
    @BindView(R.id.editTextBrizUnitPrice) EditText etUnitprice;
    @BindView(R.id.editTextBrizTotalPrice) EditText etTotalPrice;
    @BindView(R.id.radiGroupPile) RadioGroup radioGroupPile;
    @BindView(R.id.textViewBrizM) TextView tvTotalM;
    @BindView(R.id.editTextPattern) EditText etPattern;
    @BindView(R.id.editTextVariant) EditText etVariant;
    @BindView(R.id.editTextAlias) EditText etAlias;
    @BindView(R.id.editTextBrizDesc) EditText etDesc;
    @BindView(R.id.progress_bar_calc) ProgressBar progressCalc;
    @BindView(R.id.progress_bar_save) ProgressBar progressSave;
    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    double pile,totalM,unitPrice,totalPrice ;

    private void resetRadioButton() {

        radioGroupPile.clearCheck();

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
        View view=inflater.inflate(R.layout.briz_curtain_layout,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        radioGroupPile.setOnCheckedChangeListener(this);
        etOtherPile.setOnFocusChangeListener(this);
        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    @OnClick({R.id.btnSave,R.id.btnCalculate,R.id.btnCancel})
    public void onClick(View view) {
        if (view.getId()==R.id.btnSave){
            dismiss();
        }else if(view.getId()==R.id.btnCancel){
            dismiss();
        }else{

            if(!etBrizWidth.getText().toString().equals("") &&
                    !etUnitprice.getText().toString().equals("") &&
                    (radioGroupPile.getCheckedRadioButtonId()!=-1 || !etOtherPile.getText().toString().equals(""))){

                unitPrice=Double.parseDouble(etUnitprice.getText().toString());

                if(radioGroupPile.getCheckedRadioButtonId()!=-1){
                    int checkedId=radioGroupPile.getCheckedRadioButtonId();
                    if(checkedId==R.id.radioButton2){
                        pile=2;
                    }else if(checkedId==R.id.radioButton2_5){
                        pile=2.5;
                    }else{
                        pile=3;
                    }
                }else{
                    pile=Double.parseDouble(etOtherPile.getText().toString());
                }

                double brizWidth=Double.parseDouble(etBrizWidth.getText().toString());

                totalPrice=calculate(brizWidth,pile,unitPrice);
                totalM=(brizWidth/100)*pile;

                tvTotalM.setText(String.format("%.2f",totalM));
                etTotalPrice.setText(String.format("%.2f",totalPrice));

            }else{
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public double calculate(double brizWidth , double pile , double unitPrice){
        double widthLast=brizWidth/100;
        return widthLast*pile*unitPrice;
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioButton2:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton2_5:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton3:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.editTextOtherPile){
            if (hasFocus) {
                resetRadioButton();
            }
        }else{

        }
    }
}

