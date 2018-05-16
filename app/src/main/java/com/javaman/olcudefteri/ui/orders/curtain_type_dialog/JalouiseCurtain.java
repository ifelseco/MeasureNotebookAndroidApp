package com.javaman.olcudefteri.ui.orders.curtain_type_dialog;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.model.AddOrderLineDetailListModel;
import com.javaman.olcudefteri.model.OrderLineDetailModel;
import com.javaman.olcudefteri.model.ProductDetailModel;
import com.javaman.olcudefteri.model.CalculationResponse;
import com.javaman.olcudefteri.presenter.AddOrderLinePresenter;
import com.javaman.olcudefteri.presenter.impl.AddOrderLinePresenterImpl;
import com.javaman.olcudefteri.view.CalculateView;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Jaluzi Dialog
 */

public class JalouiseCurtain extends DialogFragment implements View.OnClickListener,CalculateView{

    @BindView(R.id.btnCancel) Button btnCancel;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.editTextJalousieUnitPrice) EditText etUnitPrice;
    @BindView(R.id.editTextVariant) EditText etVariant;
    @BindView(R.id.editTextPattern) EditText etPattern;
    @BindView(R.id.editTextAlias) EditText etAlias;
    @BindView(R.id.editTextDesc) EditText etDesc;
    @BindView(R.id.textViewJalousieM2) TextView tvTotalM2;
    @BindView(R.id.textViewJalousieTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.radio_group_direction) RadioGroup radioGroupDirection;
    @BindView(R.id.progress_bar_calc) ProgressBar progressBarCalc;

    private AddOrderLinePresenter mAddOrderLinePresenter;
    public static final int ARG_PRODUCT_VALUE = 4;
    SharedPreferenceHelper sharedPreferenceHelper;


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
        View view=inflater.inflate(R.layout.jalouise_curtain,null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        ButterKnife.bind(this,view);
        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }


    @Override
    @OnClick({R.id.btnSave,R.id.btnCalculate,R.id.btnCancel})
    public void onClick(View view) {
        List<OrderLineDetailModel> orderLines = new ArrayList<>();

        if (view.getId() == R.id.btnSave) {
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel=new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);
            if (!etWidth.getText().toString().isEmpty()) {
                double width = Double.parseDouble(etWidth.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);
            }

            if (!etHeight.getText().toString().isEmpty()) {
                double height = Double.parseDouble(etHeight.getText().toString());
                orderLineDetailModel.setPropertyHeight(height);
            }

            if (radioGroupDirection.getCheckedRadioButtonId() != -1) {
                int  direction;
                int checkedId = radioGroupDirection.getCheckedRadioButtonId();
                if (checkedId == R.id.radioButtonJaluziLeft) {
                    direction = 1;
                } else if (checkedId == R.id.radioButtonJaluziRight) {
                    direction = 2;
                } else {
                    direction = 0;
                }

                orderLineDetailModel.setDirection(direction);
            }

            if (!etUnitPrice.getText().toString().isEmpty()) {
                double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                orderLineDetailModel.setUnitPrice(unitPrice);
            }



            if (!etPattern.getText().toString().isEmpty()) {
                String pattern =etPattern.getText().toString();
                productDetailModel.setPatternCode(pattern);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etVariant.getText().toString().isEmpty()) {
                String variant=etVariant.getText().toString();
                productDetailModel.setVariantCode(variant);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etAlias.getText().toString().isEmpty()) {
                String alias=etAlias.getText().toString();
                productDetailModel.setAliasName(alias);
                orderLineDetailModel.setProduct(productDetailModel);
            }

            if (!etDesc.getText().toString().isEmpty()) {
                String desc=etDesc.getText().toString();
                orderLineDetailModel.setLineDescription(desc);
            }



            EventBus.getDefault().post(orderLineDetailModel);
            dismiss();
        }else if(view.getId()==R.id.btnCancel){
            dismiss();
        }else{

            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if(TextUtils.isEmpty(etWidth.getText().toString())){
                etWidth.setError("En giriniz!");
            }else if(TextUtils.isEmpty(etUnitPrice.getText().toString())){
                etUnitPrice.setError("Birim fiyat giriniz!");
            }else if(TextUtils.isEmpty(etHeight.getText().toString())){
                etHeight.setError("Boy giriniz!");
            } else{

                double unitPrice = Double.parseDouble(etUnitPrice.getText().toString());
                double width = Double.parseDouble(etWidth.getText().toString());
                double height=Double.parseDouble(etHeight.getText().toString());

                orderLineDetailModel.setUnitPrice(unitPrice);
                orderLineDetailModel.setPropertyWidth(width);
                orderLineDetailModel.setPropertyHeight(height);
                orderLines.add(orderLineDetailModel);
                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                calculateOrderLine(addOrderLineDetailListModel);
            }

        }
    }

    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId=getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel,sessionId);
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBarCalc.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarCalc.setVisibility(View.GONE);
    }

    @Override
    public String getSessionIdFromPref() {
        String xAuthToken=sharedPreferenceHelper.getStringPreference("sessionId",null);
        return xAuthToken;
    }


    @Override
    public void updateAmount(CalculationResponse calculationResponse) {
        double totalM2=calculationResponse.getUsedMaterial();
        double totalPrice=calculationResponse.getTotalAmount();
        tvTotalM2.setText(String.format("%.2f",totalM2)+" m2");
        tvTotalPrice.setText(String.format("%.2f", totalPrice)+" TL");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }
}

