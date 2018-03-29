package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
 * Tül kruvaze dialog
 */

public class CurtainDoubleNet extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener ,CalculateView{

    @BindView(R.id.editTextWidth)
    EditText etWidth;

    @BindView(R.id.editTextHeight)
    EditText etHeight;

    @BindView(R.id.editTextRightWidth)
    EditText etRigthWidth;

    @BindView(R.id.editTextLeftWidth)
    EditText etLeftWidth;

    @BindView(R.id.editTextVariant)
    EditText etVariant;

    @BindView(R.id.editTextPattern)
    EditText etPattern;

    @BindView(R.id.editTextAlias)
    EditText etAlias;

    @BindView(R.id.editTextOtherPile)
    EditText etOtherPile;

    @BindView(R.id.editTextDoubleNetUnitPrice)
    EditText etUnitprice;

    @BindView(R.id.editTextDoubleNetTotalPrice)
    EditText etTotalPrice;

    @BindView(R.id.editTextDoubleNetDesc)
    EditText etDesc;

    @BindView(R.id.textViewDoubleNetM)
    TextView tvTotalMeter;

    @BindView(R.id.textViewDoubleNetWindowWidth)
    TextView tvDoubleNetWindowWidth;

    @BindView(R.id.btnSave)
    ImageButton btnSave;

    @BindView(R.id.btnCancel)
    ImageButton btnCancel;

    @BindView(R.id.btnCalculate)
    ImageButton btnCalculate;

    @BindView(R.id.radiGroupPile)
    RadioGroup radioGroupPile;

    @BindView(R.id.progress_bar_calc)
    ProgressBar progressBarCalc;

    @BindView(R.id.progress_bar_save)
    ProgressBar progressBarSave;

    public static final int ARG_PRODUCT_VALUE = 6;
    SharedPreferenceHelper sharedPreferenceHelper;

    private AddOrderLinePresenter mAddOrderLinePresenter;

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
        View view = inflater.inflate(R.layout.curtain_double_net_layout, null);
        sharedPreferenceHelper=new SharedPreferenceHelper(getActivity().getApplicationContext());
        ButterKnife.bind(this,view);
        mAddOrderLinePresenter=new AddOrderLinePresenterImpl(this);
        initView();
        return view;
    }

    private void initView() {
        radioGroupPile.setOnCheckedChangeListener(this);
        etOtherPile.setOnFocusChangeListener(this);
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    @Override
    @OnClick({R.id.btnSave,R.id.btnCancel,R.id.btnCalculate})
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

            if (!etLeftWidth.getText().toString().isEmpty()) {
                double leftWidth = Double.parseDouble(etLeftWidth.getText().toString());
                orderLineDetailModel.setPropertyLeftWidth(leftWidth);
            }

            if (!etRigthWidth.getText().toString().isEmpty()) {
                double rightWidth = Double.parseDouble(etRigthWidth.getText().toString());
                orderLineDetailModel.setPropertyRightWidth(rightWidth);
            }


            if (!etUnitprice.getText().toString().isEmpty()) {
                double unitPrice = Double.parseDouble(etUnitprice.getText().toString());
                orderLineDetailModel.setUnitPrice(unitPrice);
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

            if(!etTotalPrice.getText().toString().isEmpty()){
                double lineAmount=Double.parseDouble(etTotalPrice.getText().toString());
                orderLineDetailModel.setLineAmount(lineAmount);
            }

            EventBus.getDefault().post(orderLineDetailModel);



            dismiss();
        } else if (view.getId() == R.id.btnCancel) {
            dismiss();
        } else {

            AddOrderLineDetailListModel addOrderLineDetailListModel = new AddOrderLineDetailListModel();
            OrderLineDetailModel orderLineDetailModel = new OrderLineDetailModel();
            ProductDetailModel productDetailModel = new ProductDetailModel();
            productDetailModel.setProductValue(ARG_PRODUCT_VALUE);
            orderLineDetailModel.setProduct(productDetailModel);

            if(TextUtils.isEmpty(etWidth.getText().toString())){
                etWidth.setError("En giriniz!");
            }else if(TextUtils.isEmpty(etUnitprice.getText().toString())){
                etUnitprice.setError("Birim fiyat giriniz!");
            }else if(radioGroupPile.getCheckedRadioButtonId() == -1 && TextUtils.isEmpty(etOtherPile.getText().toString())){
                Toast.makeText(getActivity(), "Pile sıklığı giriniz!", Toast.LENGTH_SHORT).show();
            }else{
                double unitPrice, pile;
                unitPrice = Double.parseDouble(etUnitprice.getText().toString());
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

                double doubleNetWidth = Double.parseDouble(etWidth.getText().toString());

                orderLineDetailModel.setUnitPrice(unitPrice);
                orderLineDetailModel.setPropertyWidth(doubleNetWidth);
                orderLineDetailModel.setSizeOfPile(pile);
                orderLines.add(orderLineDetailModel);
                addOrderLineDetailListModel.setOrderLineDetailModelList(orderLines);
                calculateOrderLine(addOrderLineDetailListModel);

            }



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
        if (v.getId() == R.id.editTextOtherPile) {
            if (hasFocus) {
                resetRadioButton();
            }
        } else {

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
       double totalM=calculationResponse.getUsedMaterial();
       double totalPrice=calculationResponse.getTotalAmount();

        tvTotalMeter.setText(String.format("%.2f", totalM));
        etTotalPrice.setText(String.format("%.2f", totalPrice));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddOrderLinePresenter.onDestroyCalculate();
    }
}

