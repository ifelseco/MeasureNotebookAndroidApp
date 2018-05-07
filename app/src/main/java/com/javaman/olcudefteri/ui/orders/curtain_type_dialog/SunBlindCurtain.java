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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
 * Güneşlik dialog
 */

public class SunBlindCurtain extends DialogFragment implements View.OnClickListener,CalculateView{


    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.editTextSunBlinfDesc) EditText etDesc;
    @BindView(R.id.editTextSunBlindUnitPrice) EditText etUnitPrice;
    @BindView(R.id.textViewSunBlindTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.textViewSunBlindUsedMaterial) TextView tvTotalM;
    @BindView(R.id.progress_bar_calc) ProgressBar progressBarCalc;
    @BindView(R.id.editTextVariant) EditText etVariant;
    @BindView(R.id.editTextPattern) EditText etPattern;
    @BindView(R.id.editTextAlias) EditText etAlias;
    private AddOrderLinePresenter mAddOrderLinePresenter;
    public static final int ARG_PRODUCT_VALUE = 1;
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
        View view=inflater.inflate(R.layout.sun_blid_curtain,null);
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this,view);
        setCancelable(false);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    @OnClick({R.id.btnSave , R.id.btnCancel , R.id.btnCalculate})
    public void onClick(View view) {
        List<OrderLineDetailModel> orderLines = new ArrayList<>();

        if (view.getId()==R.id.btnSave){
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
                orderLineDetailModel.setPropertyHeight(height);            }

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
            }else{
                double width=Double.parseDouble(etWidth.getText().toString());
                double unitPrice=Double.parseDouble(etUnitPrice.getText().toString());
                orderLineDetailModel.setPropertyWidth(width);
                orderLineDetailModel.setUnitPrice(unitPrice);
                orderLines.add(orderLineDetailModel);
                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                calculateOrderLine(addOrderLineDetailListModel);
            }

        }
    }


    @Override
    public void calculateOrderLine(AddOrderLineDetailListModel orderLineDetailListModel) {
        String sessionId = getSessionIdFromPref();
        mAddOrderLinePresenter.calculateOrderLine(orderLineDetailListModel, sessionId);
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
        double totalPrice = calculationResponse.getTotalAmount();
        double totalM= calculationResponse.getUsedMaterial();
        tvTotalPrice.setText(String.format("%.2f",totalPrice)+" TL");
        tvTotalM.setText(String.format("%.2f",totalM)+" m");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }
}

