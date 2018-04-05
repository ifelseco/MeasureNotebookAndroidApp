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

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.orders.model.OrderLineDetailModel;
import com.javaman.olcudefteri.orders.model.ProductDetailModel;
import com.javaman.olcudefteri.orders.model.response.CalculationResponse;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.orders.presenter.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.orders.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Farbela dialog
 */

public class FarbelaCurtain extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.editTextOtherPile) EditText etOtherPile;
    @BindView(R.id.radiGroupPile) RadioGroup radioGroupPile;
    @BindView(R.id.editTextWidth) EditText editTextWidth;
    @BindView(R.id.editTextHeight) EditText editTextHeight;
    @BindView(R.id.editTextFarbelaModel) EditText editTextModel;
    @BindView(R.id.editTextFarbelaDesc) EditText editTextDesc;
    @BindView(R.id.editTextPattern) EditText editTextPattern;
    @BindView(R.id.editTextVariant) EditText editTextVariant;
    @BindView(R.id.editTextAlias) EditText editTextAlias;
    @BindView(R.id.editTextFarbelaTotalPrice) EditText editTextTotalPrice;
    @BindView(R.id.progress_bar_save) ProgressBar progressBarSave;

    public static final int ARG_PRODUCT_VALUE = 8;



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
        View view=inflater.inflate(R.layout.farbela_curtain,null);
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
    @OnClick({R.id.btnSave,R.id.btnCancel})
    public void onClick(View view) {
        if (view.getId()==R.id.btnSave){
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel=new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if (!editTextWidth.getText().toString().isEmpty()) {
                double width = Double.parseDouble(editTextWidth.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);
            }

            if (!editTextHeight.getText().toString().isEmpty()) {
                orderLineDetailModel.setPropertyModelName(editTextHeight.getText().toString());
            }

            if (!editTextModel.getText().toString().isEmpty()) {
                double height = Double.parseDouble(editTextHeight.getText().toString());
                orderLineDetailModel.setPropertyHeight(height);
            }

            if (radioGroupPile.getCheckedRadioButtonId() != -1 || !etOtherPile.getText().toString().isEmpty()) {
                double pile;
                if (radioGroupPile.getCheckedRadioButtonId() != -1) {
                    int checkedId = radioGroupPile.getCheckedRadioButtonId();
                    if (checkedId == R.id.radioButton2) {
                        pile = 2;
                    } else if (checkedId == R.id.radioButton2_5) {
                        pile = 2.5;
                    } else {
                        pile = 3;
                    }
                } else {
                    pile = Double.parseDouble(etOtherPile.getText().toString());
                }

                orderLineDetailModel.setSizeOfPile(pile);
            }

            if (!editTextPattern.getText().toString().isEmpty()) {
                String pattern =editTextPattern.getText().toString();
                productDetailModel.setPatternCode(pattern);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!editTextVariant.getText().toString().isEmpty()) {
                String variant=editTextVariant.getText().toString();
                productDetailModel.setVariantCode(variant);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!editTextAlias.getText().toString().isEmpty()) {
                String alias=editTextAlias.getText().toString();
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!editTextDesc.getText().toString().isEmpty()) {
                String desc=editTextDesc.getText().toString();
                orderLineDetailModel.setLineDescription(desc);
            }

            if(!editTextTotalPrice.getText().toString().isEmpty()){
                double lineAmount=Double.parseDouble(editTextTotalPrice.getText().toString());
                orderLineDetailModel.setLineAmount(lineAmount);
            }
            EventBus.getDefault().post(orderLineDetailModel);
            dismiss();
        }else{
            dismiss();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
